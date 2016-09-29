package rk.game.model;

import lombok.Data;

import java.util.List;

@Data
public class Game {
    List<Player> players;

    boolean isGameReady() {
        return players.size() == 2;
    }
}
