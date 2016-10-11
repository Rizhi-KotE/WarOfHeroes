package rk.game.core;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import rk.game.model.Cell;
import rk.game.model.Field;
import rk.game.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
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
    private SimpMessagingTemplate template;

    private Field field;

    public void startGame() {
        initField(players.get(0), RIGHT);
        currentPlayer = players.get(0);
        for (Player player : players) {
            template.convertAndSendToUser(player.getUsername(), "/queue/game.run", fieldMap.get(player).getMatrix());
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

    public void userStep(Cell cell) {
        template.convertAndSendToUser(currentPlayer.getUsername(), "/queue/game.answer", cell);
    }
}
