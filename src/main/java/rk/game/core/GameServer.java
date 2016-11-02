package rk.game.core;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rk.game.command.*;
import rk.game.controller.GameController;
import rk.game.dto.AttackMessage;
import rk.game.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Service
@Scope(value = "prototype")
public class GameServer {
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    private List<Player> players;

    @Autowired
    private Field field;

    private CreaturesQueue queue;
    private Map<CreaturesStack, Player> creaturesToPlayers = new HashMap<>();

    @Autowired
    public GameServer(CreaturesQueue queue){
        this.queue = queue;
    }

    @Autowired
    private GameServerDispatcher dispatcher;


    public void setPlayers(List<Player> players) {
        this.players = players;
        for (Player player : players) {
            queue.addAll(player.getCreatures());
            for (CreaturesStack creature : player.getCreatures()) {
                creaturesToPlayers.put(creature, player);
            }
        }
        placeCreatures();
    }

    @Autowired
    private GameController controller;

    public void startGame() {
        for (Player player : players) {
            controller.sendMessage(player, Arrays.asList("startGame"));
        }
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

    public Player getPlayer(String name) {
        return players.stream().reduce(null, (result, player) -> name.equals(player.getUsername()) ? player : result);
    }

    public MoveCreatureCommand makeStep(Player player, Cell cell) {
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
        return command;
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

    public DamageCommand damageCreature(CreaturesStack stack, Cell targetCell) {
        Cell attackingCell = field.getCell(stack);
        int damage = calcDamage(attackingCell.getStack(), targetCell.getStack());
        targetCell.getStack().changeHealth(-damage);

        return new DamageCommand(attackingCell, targetCell, damage);
    }

    public Map<Player, List<Command>> attack(Player player, AttackMessage message) throws IllegalAccessError {
        if (!getCurrentPlayer().equals(player))
            throw new IllegalAccessError("не твой ход");
        List<Command> list = new ArrayList<>();
        MoveCreatureCommand moveCommand = makeStep(player, message.getAttackCell());
        list.add(moveCommand);
        list.add(damageCreature(moveCommand.getStack(), field.getCell(moveCommand.getInX(), moveCommand.getInY())));
        return players.stream().collect(Collectors.toMap(p -> p, p -> list));
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
