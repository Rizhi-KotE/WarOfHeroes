package rk.game.core;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rk.game.command.GetCreatureCommand;
import rk.game.command.MoveCreatureCommand;
import rk.game.command.PlacingCommand;
import rk.game.controller.GameController;
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

    private Player currentPlayer;

    @Autowired
    private Field field;

    private CreaturesQueue queue;

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
        controller.gameAnswer(currentPlayer, cell);
    }

    public Player getPlayer(String name) {
        return players.stream().reduce(null, (result, player) -> name.equals(player.getUsername()) ? player : result);
    }

    public MoveCreatureCommand makeStep(String name, Cell cell) {
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
        Map<Player, List<GetCreatureCommand>> creatures = players.stream()
                .collect(Collectors.toMap(player -> player, player ->
                        player.getCreatures().stream()
                                .map(stack -> {
                                    Cell cell = field.getCell(stack);
                                    GetCreatureCommand command = new GetCreatureCommand(stack, cell.x, cell.y);
                                    command.setOwn(currentPlayer.equals(player));
                                    return command;
                                }).collect(Collectors.toList())
                ));
        return creatures.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }
}
