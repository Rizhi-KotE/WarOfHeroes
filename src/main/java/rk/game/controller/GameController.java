package rk.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rk.game.command.StartPlacingCommand;
import rk.game.core.GameServer;
import rk.game.core.GameServerDispatcher;
import rk.game.model.Cell;
import rk.game.model.CreaturesStack;
import rk.game.core.WaitingGameQueueService;
import rk.game.model.Player;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
public class GameController {

    @Autowired
    private WaitingGameQueueService waitingGameQueueService;

    @Autowired
    private GameServerDispatcher dispatcher;

    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping(value = "/game/start", method = RequestMethod.POST)
    public List<String> gameStart(Principal p, @RequestBody @Valid List<CreaturesStack> creatureChoice) {
        String username = p.getName();
        Player player = new Player();
        player.setUsername(username);
        player.setCreatures(creatureChoice);
        return Arrays.asList(waitingGameQueueService.addPlayer(player));
    }

    @RequestMapping(value = "/game/creatures")
    public List<CreaturesStack> getCreatures(Principal principal){
        GameServer server = dispatcher.getServer(principal.getName());
        return server.getPlayer(principal.getName()).getCreatures();
    }

    @MessageMapping(value = "/queue/game.creatures")
    @SendToUser(value = "/queue/game.message")
    public StartPlacingCommand getCreaturesPlacing(Principal principal){
        GameServer server = dispatcher.getServer(principal.getName());
        Player player = dispatcher.getPlayer(principal.getName());
        return server.placingCreatures(player);
    }

    @RequestMapping(value = "/queue/game/step")
    public void userStep(Principal p, @RequestBody Cell cell){
        dispatcher.userStep(p.getName(), cell);
    }

    public void startGame(String username, Object message){
        template.convertAndSendToUser(username, "/queue/game.run", message);
    }

    public void gameAnswer(Player player, Cell cell) {
        template.convertAndSendToUser(player.getUsername(), "/queue/game.answer", cell);
    }

    public void sendMessage(Player player, Object message){
        template.convertAndSendToUser(player.getUsername(), "/queue/game.message", message);
    }
}
