package rk.game.core;

import org.springframework.stereotype.Service;
import rk.game.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameServerDispatcher {

    private Map<String, GameServer> servers = new HashMap<>();
    private Map<String, Player> players = new HashMap<>();


    public void runGame(List<Player> players){
        GameServer server = new GameServer(players);
        players.forEach(player -> {
            this.players.put(player.getUsername(), player);
            servers.put(player.getUsername(), server);
        });
    }

    public GameServer getServer(String username) {
        return servers.get(username);
    }

    public Player getPlayer(String username) {
        return players.get(username);
    }

    public void removePlayer(String name) {
        servers.remove(name);
        players.remove(name);
    }
}
