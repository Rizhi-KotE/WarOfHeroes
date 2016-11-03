package rk.game.core;

import lombok.Data;
import rk.game.command.*;
import rk.game.dto.AttackMessage;
import rk.game.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class GameServer {
    enum GameState {
        StartState,
        FullStep,
        AttackStep,
        EndState;
    }

    private GameState state = GameState.StartState;
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    private List<Player> players;

    private final Field field;

    private final CommandMap commandMap;

    private final CreaturesQueue queue;
    private final Map<CreaturesStack, Player> creaturesToPlayers;

    public GameServer(List<Player> players) {
        this.players = players;
        queue = new CreaturesQueue();
        field = new Field();
        commandMap = new CommandMap(players);
        creaturesToPlayers = new HashMap<>();
        changeState(GameState.FullStep);
    }

    private void initServer() {
        for (Player player : players) {
            queue.addAll(player.getCreatures());
            for (CreaturesStack creature : player.getCreatures()) {
                creaturesToPlayers.put(creature, player);
            }
        }
        placeCreatures();
    }

    private void placeCreatures() {
        for (Player player : players) {
            int side = players.indexOf(player);
            int j = 9 * (side % 2);
            int i = 0;
            for (CreaturesStack creature : player.getCreatures()) {
                field.addCreature(creature, i, j);
                i++;
            }
        }
    }

    private void changeState(GameState nextState) {
        switch (state) {
            case AttackStep: {
                switch (nextState) {
                    case FullStep: {
                        changeCreatureOut();
                        break;
                    }
                    case EndState: {
                        gameEndOut();
                        break;
                    }
                }
                break;
            }
            case FullStep: {
                switch (nextState) {
                    case FullStep:
                        changeCreatureOut();
                        break;
                    case AttackStep:
                        moveCreatureOut();
                }
                break;
            }
            case StartState: {
                switch (nextState) {
                    case FullStep:
                        initServer();
                        break;
                }
                break;
            }
        }
        state = nextState;
    }

    private void moveCreatureOut() {
        CreaturesStack nextCreature = queue.getCurrentCreature();
        Player nextPlayer = creaturesToPlayers.get(nextCreature);
        commandMap.addCommand(nextPlayer, new TypedCommand("yourMove"));
        players.stream()
                .filter(player -> !player.equals(nextPlayer))
                .forEach(player -> commandMap.addCommand(player, new TypedCommand("wait")));
        commandMap.addCommands(getAvailableCells());
    }

    private void gameEndOut() {
        CreaturesStack lastOver = queue.getCurrentCreature();
        Player victorious = creaturesToPlayers.get(lastOver);
        commandMap.addCommand(victorious, new TypedCommand("win"));
        players.stream()
                .filter(player -> !player.equals(victorious))
                .forEach(player -> commandMap.addCommand(player, new TypedCommand("lose")));
    }

    private void changeCreatureOut() {
        queue.popCreature();
        CreaturesStack nextCreature = queue.getCurrentCreature();
        Player nextPlayer = creaturesToPlayers.get(nextCreature);
        commandMap.addCommand(nextPlayer, new TypedCommand("yourMove"));
        players.stream()
                .filter(player -> !player.equals(nextPlayer))
                .forEach(player -> commandMap.addCommand(player, new TypedCommand("wait")));
        commandMap.addCommands(getAvailableCells());
    }

    public Map<Player, List<Command>> messageAttack(AttackMessage message) throws IllegalAccessError {
        commandMap.clean();
        switch (state) {
            case AttackStep:
                damageCreature(message.getAttackCell(), message.getTargetCell());
                break;
            default:
                break;
        }
        return commandMap.getMap();
    }

    public Map<Player, List<Command>> messageMove(Cell cell) {
        commandMap.clean();
        switch (state) {
            case FullStep:
                moveCreature(cell);
                break;
            default:
                break;
        }
        return commandMap.getMap();
    }

    public Map<Player, List<Command>> messageWait() {
        commandMap.clean();
        switch (state) {
            case FullStep:
                changeState(GameState.AttackStep);
                break;
            case AttackStep:
                changeState(GameState.AttackStep);
                break;
            default:
                break;
        }
        return commandMap.getMap();
    }

    private void moveCreature(Cell cell) {
        CreaturesStack stack = queue.getCurrentCreature();
        queue.popCreature();
        Cell currentCell = field.getCell(stack);
        field.moveCreature(stack, cell);
        MoveCreatureCommand command = new MoveCreatureCommand();
        command.setOutX(currentCell.x);
        command.setOutY(currentCell.y);
        command.setInX(cell.x);
        command.setInY(cell.y);
        command.setStack(stack);
        commandMap.addCommand(command);
        changeState(GameState.AttackStep);
    }

    public List<GetCreatureCommand> getCreaturesPlaces(Player currentPlayer) {
        return creaturesToPlayers.keySet().stream().map(stack -> {
            Cell cell = field.getCell(stack);
            GetCreatureCommand command = new GetCreatureCommand(stack, cell.x, cell.y);
            return command;
        }).collect(Collectors.toList());
    }

    public int calcDamage(CreaturesStack attackingStack, CreaturesStack target) {
        int pureDamage = attackingStack.getCreature().getDamage() * attackingStack.getSize();
        double coef = 0.1 * (attackingStack.getCreature().getAttack() - attackingStack.getCreature().getDefence());
        return pureDamage + (int) (pureDamage * coef);
    }

    public void damageCreature(Cell outputCell, Cell inputCell) {
        Cell attackingCell = field.getCell(outputCell.x, outputCell.y);
        Cell targetCell = field.getCell(inputCell.x, inputCell.y);
        CreaturesStack attackingStack = attackingCell.getStack();
        CreaturesStack targetStack = targetCell.getStack();
        int damage = calcDamage(attackingStack, targetStack);
        targetStack.changeHealth(-damage);
        commandMap.addCommand(new DamageCommand(attackingCell, targetCell, damage));
        if (targetStack.isAlive()) {
            int rebuff = calcDamage(targetStack, attackingStack);
            attackingStack.changeHealth(-rebuff);
            commandMap.addCommand(new DamageCommand(targetCell, attackingCell, damage));
        } else {
            performCreatureDeath(targetCell, targetStack);
        }
        if (!attackingStack.isAlive()) {
            performCreatureDeath(attackingCell, attackingStack);
        } else {
            changeState(GameState.FullStep);
        }
    }

    private void performCreatureDeath(Cell cell, CreaturesStack stack) {
        queue.removeCreature(stack);
        field.removeCreature(stack);
        commandMap.addCommand(new CreatureDiedCommand(cell));
        Player attackingPlayer = creaturesToPlayers.get(stack);
        attackingPlayer.getCreatures().remove(stack);
        if (attackingPlayer.getCreatures().size() == 0) {
            changeState(GameState.EndState);
        }
    }

    public List<Command> getAvailableCells() {
        CreaturesStack creaturesStack = queue.getCurrentCreature();
        Cell cell = field.getCell(creaturesStack);
        return getAvailableCells(cell);
    }

    public List<Command> getAvailableCells(Cell target) {
        Cell cell = field.getCell(target.x, target.y);
        if (cell.getStack() == null) {
            return new ArrayList<>();
        }
        List<Cell> cells = field.getAvailableAria(cell.getStack());
        AvailableCellsCommand availableCellsCommand = new AvailableCellsCommand(target, cells);
        Map<CreaturesStack, List<Cell>> availableEnemies = field.getAvailableEnemies(cells);
        AvailableEnemiesCommand availableEnemiesCommand =
                new AvailableEnemiesCommand(availableEnemies);
        return Arrays.asList(availableCellsCommand, availableEnemiesCommand);
    }

    public Player getCurrentPlayer() {
        return creaturesToPlayers.get(queue.getCurrentCreature());
    }
}
