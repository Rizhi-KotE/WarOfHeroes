package rk.game.core;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rk.game.command.AddCreatureCommand;
import rk.game.command.MoveCreatureCommand;
import rk.game.command.StartPlacingCommand;
import rk.game.controller.GameController;
import rk.game.model.*;

import java.util.*;

@Data
@Service
@Scope(value = "prototype")
public class GameServer {
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    private List<Player> players;

    private Player currentPlayer;

    @Autowired
    private Field field;

    private CreaturesQueue queue;

    @Autowired
    GameServer(CreaturesQueue queue){
        this.queue = queue;
    }

    @Autowired
    private GameServerDispatcher dispatcher;


    public void setPlayers(List<Player> players) {
        this.players = players;
        for (Player player : players) {
            placingCreatures(player);
            queue.addAll(player.getCreatures());
        }
    }

    @Autowired
    private GameController controller;

    public void startGame() {
        for (Player player : players) {
            controller.startGame(player.getUsername(), Arrays.asList("start"));
        }
    }

    public StartPlacingCommand placingCreatures(Player player) {
        int side = players.indexOf(player);
        int j = 9 * (side % 2);
        int i = 0;
        List<AddCreatureCommand> list = new ArrayList<>(10);
        for (CreaturesStack creature : player.getCreatures()) {
            AddCreatureCommand creatureCommand = new AddCreatureCommand();
            creatureCommand.setX(i);
            creatureCommand.setY(j);
            creatureCommand.setStack(creature);
            list.add(creatureCommand);
            field.addCreature(creature, i, j);
            i++;
        }
        return new StartPlacingCommand(list);
    }

    public void userStep(Cell cell) {
        controller.gameAnswer(currentPlayer, cell);
    }

    public Player getPlayer(String name) {
        return players.stream().reduce(null, (result, player) -> name.equals(player.getUsername()) ? player : result);
    }

    public MoveCreatureCommand makeStep(String name, Cell cell) {
        CreaturesStack stack = queue.getCurrentCreature();
        queue.popCreature();
        Cell currentCell = field.getCell(stack);
        MoveCreatureCommand command = new MoveCreatureCommand();
        command.setOutX(currentCell.x);
        command.setOutY(currentCell.y);
        command.setInX(cell.x);
        command.setInY(cell.y);
        command.setStack(stack);
        return command;
    }
}
