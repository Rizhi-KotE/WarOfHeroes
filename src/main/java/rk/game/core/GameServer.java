package rk.game.core;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import rk.game.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Component
@Scope(value = "prototype")
public class GameServer {
    private List<Player> players;

    @Autowired
    private SimpMessagingTemplate template;

    public void startGame() {
        for (Player player: players){
            List<String> message = new ArrayList<>();
            for(Player annotherPlayer: players){
                if(!annotherPlayer.equals(player)){
                    message.add(annotherPlayer.getUsername());
                    break;
                }
            }
            template.convertAndSendToUser(player.getUsername(),"/queue/game.run", message);
        }
    }
}
