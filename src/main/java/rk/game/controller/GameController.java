package rk.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import rk.game.core.GameServerDispatcher;
import rk.game.model.Cell;
import rk.game.model.CreaturesStack;
import rk.game.core.WaitingGameQueueService;
import rk.game.model.Player;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@MessageMapping("queue/game")
public class GameController {

    @Autowired
    private WaitingGameQueueService waitingGameQueueService;

    @Autowired
    private GameServerDispatcher dispatcher;

    @MessageMapping(value = ".start")
    public void gameStart(Principal p, @Payload @Valid List<CreaturesStack> creatureChoice) {
        String username = p.getName();
        Player player = new Player();
        player.setUsername(username);
        player.setCreatures(creatureChoice);
        waitingGameQueueService.addPlayer(player);
    }

    @MessageMapping(value = ".creatures")
    public void getCreatures(){

    }

    @MessageMapping(value = ".step")
    public void userStep(Principal p, @Payload @Valid Cell cell){
        dispatcher.userStep(p.getName(), cell);
    }
}
