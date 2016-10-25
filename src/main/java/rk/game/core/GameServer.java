package rk.game.core;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rk.game.command.Command;
import rk.game.command.DamageCommand;
import rk.game.command.GetCreatureCommand;
import rk.game.command.MoveCreatureCommand;
import rk.game.controller.GameController;
import rk.game.dto.AttackMessage;
import rk.game.model.*;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiringInspection")
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
            controller.startGame(player.getUsername(), Arrays.asList("startRequest"));
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

    public void userStep(Cell cell) {
        controller.gameAnswer(getCurrentPlayer(), cell);
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

    public DamageCommand damageCreature(AttackMessage message) {
        Cell attackingCell = field.getCell(message.getAttackCell().x, message.getAttackCell().y);
        Cell targetCell = field.getCell(message.getTargetCell().x, message.getTargetCell().y);
        int damage = calcDamage(attackingCell.getStack(), targetCell.getStack());
        targetCell.getStack().changeHealth(-damage);

        return new DamageCommand(attackingCell, targetCell, damage);
    }

    public Map<Player, List<Command>> attack(Player player, AttackMessage message) throws IllegalAccessError {
        if (!getCurrentPlayer().equals(player))
            throw new IllegalAccessError("не твой ход");
        List<Command> list = new ArrayList<>();
        list.add(makeStep(player, message.getAttackCell()));
        list.add(damageCreature(message));
        return players.stream().collect(Collectors.toMap(p -> p, p -> list));
    }

    public Player getCurrentPlayer() {
        return creaturesToPlayers.get(queue.getCurrentCreature());
    }
}
