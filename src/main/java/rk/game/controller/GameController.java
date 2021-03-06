package rk.game.controller;

import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rk.game.command.*;
import rk.game.core.GameServer;
import rk.game.core.GameServerDispatcher;
import rk.game.dto.AttackMessage;
import rk.game.model.Cell;
import rk.game.model.CreaturesStack;
import rk.game.core.WaitingGameQueueService;
import rk.game.model.Player;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

import static com.sun.org.apache.xml.internal.serializer.utils.Utils.messages;

@RestController
public class GameController {

    @Autowired
    private WaitingGameQueueService waitingGameQueueService;

    @Autowired
    private GameServerDispatcher dispatcher;

    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping(value = "/game/start", method = RequestMethod.POST)
    public List<String> gameStart(Principal p, @RequestBody @Valid ArrayList<CreaturesStack> creatureChoice) {
        String username = p.getName();
        Player player = new Player(username, creatureChoice);
        return Arrays.asList(waitingGameQueueService.addPlayer(player));
    }

    @MessageMapping(value = "/queue/game.creatures")
    @SendToUser(value = "/queue/game.message")
    public void getCreaturesPlaces(Principal principal) {
        GameServer server = dispatcher.getServer(principal.getName());
        Player player = dispatcher.getPlayer(principal.getName());
        if (server != null) {
            Map<Player, List<Command>> messages = server.readyToPlay(player);
            messages.forEach((p, commands) -> sendMessage(p, commands));
        }
    }

    @MessageMapping(value = "/queue/game.moveMessage")
    public void MoveCreature(Principal principal, @Payload Cell cell) {
        GameServer server = dispatcher.getServer(principal.getName());
        Map<Player, List<Command>> messages = server.messageMove(cell);
        messages.forEach((p, commands) -> sendMessage(p, commands));
    }

    @MessageMapping(value = "/queue/game.attackMessage")
    public void attackCreature(Principal principal, AttackMessage message) {
        GameServer server = dispatcher.getServer(principal.getName());
        Map<Player, List<Command>> messages = server.messageAttack(message);
        messages.forEach((p, commands) -> sendMessage(p, commands));

    }

    @MessageMapping(value = "queue/game.finish")
    public void finishGame(Principal principal){
        dispatcher.removePlayer(principal.getName());
    }

    @MessageMapping(value = "/queue/game.waitMessage")
    public void messageWait(Principal principal) {
        GameServer server = dispatcher.getServer(principal.getName());
        Map<Player, List<Command>> messages = server.messageWait();
        messages.forEach((p, commands) -> sendMessage(p, commands));
    }

    @MessageMapping(value = "/queue/game.availableCells")
    @SendToUser(value = "/queue/game.message")
    public Object getAvailableCells(Principal principal, Cell cell){
        GameServer server = dispatcher.getServer(principal.getName());
        return server.getAvailableCellsCommand(cell);
    }

    @MessageMapping(value = "queue/game.currentAvailableCells")
    @SendToUser(value = "/queue/game.message")
    public Object getCurrentAvailableCells(Principal principal) {
        GameServer server = dispatcher.getServer(principal.getName());
        return server.getAvailableCellsCommand();
    }

    private void sendMessage(Player player, Object message){
        template.convertAndSendToUser(player.getUsername(), "/queue/game.message", message);
    }
}