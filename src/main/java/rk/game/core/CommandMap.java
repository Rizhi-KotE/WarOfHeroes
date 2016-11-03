package rk.game.core;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import rk.game.command.Command;
import rk.game.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class CommandMap {
    Map<Player, List<Command>> map = new HashMap<>();

    CommandMap(List<Player> players){
        for(Player player: players){
            map.put(player, new ArrayList<>());
        }
    }

    public void addCommand(Command command) {
        for(Player player: map.keySet()){
            map.get(player).add(command);
        }
    }

    public void addCommands(List<Command> commands){
        for(Player player: map.keySet()){
            map.get(player).addAll(commands);
        }
    }

    public void addCommand(Player player, Command command){
        map.get(player).add(command);
    }

    public void clean(){
        for(Player player: map.keySet()){
            map.put(player, new ArrayList<>());
        }
    }
}
