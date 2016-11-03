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

    private Map<String, GameServer> map = new HashMap<>();
    private Map<String, Player> players = new HashMap<>();


    public void runGame(List<Player> players){
        GameServer server = new GameServer(players);
        players.forEach(player -> {
            this.players.put(player.getUsername(), player);
            map.put(player.getUsername(), server);
        });
    }

    public GameServer getServer(String username) {
        return map.get(username);
    }

    public Player getPlayer(String username) {
        return players.get(username);
    }
}
