package rk.game.core;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import rk.game.command.AddCreatureCommand;
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

    private Map<Player, Field> fieldMap;


    public void setPlayers(List<Player> players) {
        this.players = players;
        fieldMap = new HashMap<>();
        for (Player player : players) {
            fieldMap.put(player, new Field());
        }
    }

    @Autowired
    private GameController controller;

    public void startGame() {
        for (Player player : players) {
            controller.startGame(player.getUsername(), Arrays.asList("start"));
        }
    }

    private void initField(Player player, int side) {
        Field field = fieldMap.get(player);
        if (side == RIGHT) {
            for (int line = 0; line < 3; line++) {
                for (int column = 0; column < 10; column++) {
                    field.getMatrix()[line][column].setAvailable(true);
                }
            }
        } else if (side == LEFT) {
            for (int line = 9; line >= 7; line--) {
                for (int column = 0; column < 10; column++) {
                    field.getMatrix()[line][column].setAvailable(true);
                }
            }
        }
    }

    public StartPlacingCommand placingCreatures(Player player) {
        Cell[][] matrix = fieldMap.get(player).getMatrix();
        int side = players.indexOf(player);
        int j = 9 * (side % 2);
        int i = 0;
        List<AddCreatureCommand> list = new ArrayList<>(10);
        for (CreaturesStack creature : player.getCreatures()) {
            AddCreatureCommand creatureCommand = new AddCreatureCommand();
            creatureCommand.setX(i);
            creatureCommand.setY(j);
            creatureCommand.setCreature(creature);
            list.add(creatureCommand);
            matrix[i][j].setStack(creature);
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
}
