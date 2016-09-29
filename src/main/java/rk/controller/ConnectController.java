package rk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import rk.game.core.WaitingGameQueueService;
import rk.game.model.Player;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ConnectController {

    @Autowired
    private WaitingGameQueueService waitingGameQueueService;

    @MessageMapping(value = "/queue/game.start")
    public void gameStart(Principal p) {
        String username = p.getName();
        Player player = new Player();
        player.setUsername(username);
        waitingGameQueueService.addPlayer(player);
    }
}
