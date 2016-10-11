package rk.game.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rk.game.model.Cell;
import rk.game.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameServerDispatcher {

    Map<String, GameServer> map = new HashMap<>();

    @Autowired
    private GameServer server;

    public void runGame(List<Player> players){
        server.setPlayers(players);
        players.forEach(player -> map.put(player.getUsername(), server));
        server.startGame();
    }

    public void userStep(String name, Cell cell) {
        map.get(name).userStep(cell);
    }
}
