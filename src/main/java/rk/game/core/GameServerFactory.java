package rk.game.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rk.game.model.Player;

import java.util.List;

@Service
public class GameServerFactory {
    @Autowired
    GameServer gameServer;

    void startGame(List<Player> players) {
        gameServer.setPlayers(players);
        gameServer.startGame();
    }
}
