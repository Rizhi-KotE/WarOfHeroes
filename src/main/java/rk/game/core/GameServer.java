package rk.game.core;

import lombok.Data;
import rk.game.command.*;
import rk.game.dto.AttackMessage;
import rk.game.dto.QueuePlace;
import rk.game.model.Cell;
import rk.game.model.CreaturesStack;
import rk.game.model.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class GameServer {

    public static final double DIAGONAL_LENGTH = 1.8;

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
    }

    private void initServer() {
        for (Player player : players) {
            queue.addAll(player.getCreatures());
            for (CreaturesStack creature : player.getCreatures()) {
                creaturesToPlayers.put(creature, player);
            }
        }
        placeCreatures();
        commandMap.addCommand(new PlacingCommand(getCreaturesPlaces()));
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

    public Map<Player, List<Command>> readyToPlay(Player player) {
        switch (state) {
            case START_STATE:
                initServer();
        }
        commandMap.addCommands(getCreaturesPlaces());
        return getOutput();
    }

    public Map<Player, List<Command>> messageAttack(AttackMessage message) throws IllegalAccessError {
        commandMap.clean();
        Cell currentCell = field.getCell(queue.getCurrentCreature());
        Cell targetCell = message.getTargetCell();
        if(Point.distance(targetCell.x, targetCell.y, currentCell.x, currentCell.y) > DIAGONAL_LENGTH){
            return getOutput();
        }
        switch (state) {
            case CAN_ATTACK: {
                damageCreature(currentCell, message.getTargetCell());
                break;
            }
            default:
                break;
        }
        return getOutput();
    }

    public Map<Player, List<Command>> messageMove(Cell cell) {
        commandMap.clean();
        switch (state) {
            case CAN_NOT_ATTACK: {
                moveCreature(cell);
                List<Cell> availableEnemies = getAvailableEnemies(0);
                if (availableEnemies.size() == 0) {
                    changeCreature();
                }
                break;
            }
            case CAN_ATTACK: {
                moveCreature(cell);
                List<Cell> availableEnemies = getAvailableEnemies(0);
                if (availableEnemies.size() == 0) {
                    changeCreature();
                }
                break;
            }
            default:
                break;
        }
        return getOutput();
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
        return getOutput();
    }

    public Map<Player, List<Command>> getOutput(){
        switch (state){
            case START_STATE:
                commandMap.clean();
                commandMap.addCommand(new TypedCommand("wait"));
                break;
            case END_GAME:
                commandMap.clean();
                gameEndOut();
                break;
            default:
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
                List<Cell> availableEnemies = getAvailableEnemies(nextCreature.getSpeed());
                commandMap.addCommand(new AvailableEnemiesCommand(availableEnemies));
                break;
        }
        return commandMap.getMap();
    }

    private void moveCreature(Cell cell) {
        CreaturesStack stack = queue.getCurrentCreature();
        Cell currentCell = field.getCell(stack);
        int distance = field.moveCreature(stack, cell);
        stack.move(distance);
        MoveCreatureCommand command = new MoveCreatureCommand();
        command.setOutX(currentCell.x);
        command.setOutY(currentCell.y);
        command.setInX(cell.x);
        command.setInY(cell.y);
        command.setStack(stack);
        commandMap.addCommand(command);
    }

    public List<AddCreatureCommand> getCreaturesPlaces() {
        return creaturesToPlayers.keySet().stream().map(stack -> {
            Cell cell = field.getCell(stack);
            return new AddCreatureCommand(stack, cell.x, cell.y);
        }).collect(Collectors.toList());
    }

    private List<Cell> getAvailableEnemies(int distance) {
        Cell target = field.getCell(queue.getCurrentCreature());
        return getAvailableEnemies(target.x, target.y, distance);
    }

    private List<Cell> getAvailableEnemies(int x, int y, int distance) {
        Cell target = field.getCell(x, y);
        if (target.getStack() == null) {
            return new ArrayList<>();
        }
        List<Cell> availableEnemies = field.getAvailableEnemies(target, distance);
        Player currentPlayer = creaturesToPlayers.get(target.getStack());
        availableEnemies = availableEnemies.stream()
                .filter(cell -> !currentPlayer.getCreatures().contains(cell.getStack()))
                .collect(Collectors.toList());
        return availableEnemies;
    }

    private void gameEndOut() {
        CreaturesStack lastOver = queue.getCurrentCreature();
        Player victorious = creaturesToPlayers.get(lastOver);
        commandMap.addCommand(victorious, new gameResultCommand("win"));
        players.stream()
                .filter(player -> !player.equals(victorious))
                .forEach(player -> commandMap.addCommand(player, new gameResultCommand("lose")));
    }

    private void damageCreature(Cell outputCell, Cell inputCell) {
        Cell attackingCell = field.getCell(outputCell.x, outputCell.y);
        Cell targetCell = field.getCell(inputCell.x, inputCell.y);
        CreaturesStack attackingStack = attackingCell.getStack();
        CreaturesStack targetStack = targetCell.getStack();
        int damage = attackingStack.damage(targetStack);
        commandMap.addCommand(new DamageCommand(attackingCell, targetCell, damage));
        if (targetStack.isAlive()) {
            int rebuff = targetStack.damage(attackingStack);
            commandMap.addCommand(new DamageCommand(targetCell, attackingCell, rebuff));
        } else {            performCreatureDeath(targetCell, targetStack);
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
        if (getAvailableEnemies(nextCreature.getSpeed()).size() == 0) {
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
        attackingPlayer.removeCreature(stack);
        if (attackingPlayer.getCreatures().size() == 0) {
            setState(GameState.END_GAME);
            gameEndOut();
        }else {
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
        List<Cell> cells = field.getAvailableAria(target, target.getStack().getSpeed());
        return new AvailableCellsCommand(target, cells);
    }
}
