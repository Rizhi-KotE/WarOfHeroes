package rk.game.core;

import lombok.Data;
import rk.game.command.*;
import rk.game.dto.AttackMessage;
import rk.game.dto.QueuePlace;
import rk.game.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class GameServer {
    enum GameState {
        START_STATE,
        CAN_NOT_ATTACK,
        CAN_ATTACK,
        END_GAME;
    }

    private GameState state = GameState.START_STATE;
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
        initServer();
    }

    private void initServer() {
        for (Player player : players) {
            queue.addAll(player.getCreatures());
            for (CreaturesStack creature : player.getCreatures()) {
                creaturesToPlayers.put(creature, player);
            }
        }
        placeCreatures();
        changeCreature();
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

    private void moveCreatureOut() {
        CreaturesStack nextCreature = queue.getCurrentCreature();
        Player nextPlayer = creaturesToPlayers.get(nextCreature);
        commandMap.addCommand(nextPlayer, new TypedCommand("yourMove"));
        players.stream()
                .filter(player -> !player.equals(nextPlayer))
                .forEach(player -> commandMap.addCommand(player, new TypedCommand("wait")));
        AvailableEnemiesCommand availableEnemiesCommand = getAvailableEnemiesCommand(0);
        if (availableEnemiesCommand.getAvailableEnemies().size() == 0) {
            changeCreature();
            return;
        }
        List<Cell> availableAria = field.getAvailableAria(field.getCell(nextCreature), 0);
        commandMap.addCommand(new AvailableCellsCommand(field.getCell(nextCreature), availableAria));
        commandMap.addCommand(availableEnemiesCommand);
    }


    public Map<Player, List<Command>> messageAttack(AttackMessage message) throws IllegalAccessError {
        commandMap.clean();
        switch (state) {
            case CAN_ATTACK: {
                Cell currentCell = field.getCell(queue.getCurrentCreature());
                damageCreature(currentCell, message.getTargetCell());
                break;
            }
            default:
                break;
        }
        return commandMap.getMap();
    }

    public Map<Player, List<Command>> messageMove(Cell cell) {
        commandMap.clean();
        switch (state) {
            case CAN_NOT_ATTACK: {
                moveCreature(cell);
                moveCreatureOut();
                break;
            }
            case CAN_ATTACK: {
                moveCreature(cell);
                moveCreatureOut();
                break;
            }
            default:
                break;
        }
        return commandMap.getMap();
    }

    public Map<Player, List<Command>> messageWait() {
        commandMap.clean();
        switch (state) {
            case CAN_NOT_ATTACK:
                changeCreature();
                break;
            case CAN_ATTACK:
                changeCreature();
                break;
            default:
                break;
        }
        return commandMap.getMap();
    }

    private void moveCreature(Cell cell) {
        CreaturesStack stack = queue.getCurrentCreature();
        Cell currentCell = field.getCell(stack);
        field.moveCreature(stack, cell);
        MoveCreatureCommand command = new MoveCreatureCommand();
        command.setOutX(currentCell.x);
        command.setOutY(currentCell.y);
        command.setInX(cell.x);
        command.setInY(cell.y);
        command.setStack(stack);
        commandMap.addCommand(command);
    }

    public List<GetCreatureCommand> getCreaturesPlaces(Player currentPlayer) {
        return creaturesToPlayers.keySet().stream().map(stack -> {
            Cell cell = field.getCell(stack);
            return new GetCreatureCommand(stack, cell.x, cell.y);
        }).collect(Collectors.toList());
    }

    private AvailableEnemiesCommand getAvailableEnemiesCommand(int distance) {
        Cell target = field.getCell(queue.getCurrentCreature());
        return getAvailableEnemiesCommand(target.x, target.y, distance);
    }

    private AvailableEnemiesCommand getAvailableEnemiesCommand(int x, int y, int distance) {
        Cell target = field.getCell(x, y);
        if (target.getStack() == null) {
            return new AvailableEnemiesCommand(new ArrayList<>());
        }
        List<Cell> availableEnemies = field.getAvailableEnemies(target, distance);
        Player currentPlayer = creaturesToPlayers.get(target.getStack());
        availableEnemies = availableEnemies.stream()
                .filter(cell -> !currentPlayer.getCreatures().contains(cell.getStack()))
                .collect(Collectors.toList());
        return new AvailableEnemiesCommand(availableEnemies);
    }

    private void gameEndOut() {
        CreaturesStack lastOver = queue.getCurrentCreature();
        Player victorious = creaturesToPlayers.get(lastOver);
        commandMap.addCommand(victorious, new TypedCommand("win"));
        players.stream()
                .filter(player -> !player.equals(victorious))
                .forEach(player -> commandMap.addCommand(player, new TypedCommand("lose")));
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
            return;
        }
        if (!attackingStack.isAlive()) {
            performCreatureDeath(attackingCell, attackingStack);
        } else {
            changeCreature();
        }
    }

    private void changeCreature() {
        queue.popCreature();
        CreaturesStack nextCreature = queue.getCurrentCreature();
        Player nextPlayer = creaturesToPlayers.get(nextCreature);
        List<QueuePlace> queuePlaces = queue.getQueue()
                .stream()
                .map(stack -> new QueuePlace(stack, creaturesToPlayers.get(stack).equals(nextPlayer)))
                .collect(Collectors.toList());
        commandMap.addCommand(nextPlayer, new ChangeTurnCommand(field.getCell(nextCreature), true));
        commandMap.addCommand(nextPlayer, new CreatureQueueCommand(queuePlaces));
        players.stream()
                .filter(player -> !player.equals(nextPlayer))
                .forEach(player -> {
                    commandMap.addCommand(player, new ChangeTurnCommand(field.getCell(nextCreature), false));
                    List<QueuePlace> places = queue.getQueue()
                            .stream()
                            .map(stack -> new QueuePlace(stack, creaturesToPlayers.get(stack).equals(player)))
                            .collect(Collectors.toList());
                    commandMap.addCommand(player, new CreatureQueueCommand(places));
                });
        commandMap.addCommand(getAvailableCellsCommand());
        AvailableEnemiesCommand availableEnemiesCommand = getAvailableEnemiesCommand(nextCreature.getCreature().getSpeed());
        commandMap.addCommand(availableEnemiesCommand);
        if (availableEnemiesCommand.getAvailableEnemies().size() == 0) {
            setState(GameState.CAN_NOT_ATTACK);
        } else {
            setState(GameState.CAN_ATTACK);
        }
    }

    private void performCreatureDeath(Cell cell, CreaturesStack stack) {
        queue.removeCreature(stack);
        field.removeCreature(stack);
        commandMap.addCommand(new CreatureDiedCommand(cell));
        Player attackingPlayer = creaturesToPlayers.get(stack);
        creaturesToPlayers.remove(stack);
        attackingPlayer.getCreatures().remove(stack);
        if (attackingPlayer.getCreatures().size() == 0) {
            setState(GameState.END_GAME);
            gameEndOut();
        }else {
            setState(GameState.CAN_NOT_ATTACK);
            changeCreature();
        }
    }

    public AvailableCellsCommand getAvailableCellsCommand() {
        CreaturesStack creaturesStack = queue.getCurrentCreature();
        Cell cell = field.getCell(creaturesStack);
        return getAvailableCellsCommand(cell);
    }

    public AvailableCellsCommand getAvailableCellsCommand(Cell target) {
        target = field.getCell(target.x, target.y);
        if (target.getStack() == null) {
            return new AvailableCellsCommand(target, new ArrayList<>());
        }
        List<Cell> cells = field.getAvailableAria(target, target.getStack().getCreature().getSpeed());
        return new AvailableCellsCommand(target, cells);
    }

    public Player getCurrentPlayer() {
        return creaturesToPlayers.get(queue.getCurrentCreature());
    }
}
