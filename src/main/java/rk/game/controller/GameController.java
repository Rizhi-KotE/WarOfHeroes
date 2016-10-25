package rk.game.controller;

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
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;

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

    @MessageMapping(value = "/queue/game.creatures")
    @SendToUser(value = "/queue/game.message")
    public PlacingCommand getCreaturesPlacing(Principal principal){
        GameServer server = dispatcher.getServer(principal.getName());
        Player player = dispatcher.getPlayer(principal.getName());
        return new PlacingCommand(server.getCreaturesPlaces(player));
    }

    @MessageMapping(value = "/queue/game.move")
    @SendToUser(value = "/queue/game.message")
    public MoveCreatureCommand MoveCreature(Principal principal, @Payload Cell cell){
        GameServer server = dispatcher.getServer(principal.getName());
        Player player = dispatcher.getPlayer(principal.getName());
        return server.makeStep(player, cell);
    }

    @MessageMapping(value = "/queue/game.attack")
    public void attackCreature(Principal principal, AttackMessage message) {
        GameServer server = dispatcher.getServer(principal.getName());
        Player player = dispatcher.getPlayer(principal.getName());
        Map<Player, List<Command>> messages = server.attack(player, message);
        messages.forEach((p, commands) ->
                template.convertAndSendToUser(p.getUsername(), "/queue/game.message", commands));
    }

    @MessageMapping(value = "/queue/game.availableCells")
    @SendToUser(value = "/queue/game.message")
    public Object getAvailableCells(Principal principal, @Payload Cell cell) throws IllegalFormatException {
        GameServer server = dispatcher.getServer(principal.getName());
        CreaturesStack currentCreature = server.getQueue().getCurrentCreature();
        if (cell.getStack() == null) {
            cell = server.getField().getCell(currentCreature);
        }
        List<Cell> cells = server.getField().getAvailableAria(cell.getStack());
        AvailableCellsCommand availableCellsCommand = new AvailableCellsCommand(cell, cells);
        Map<CreaturesStack, List<Cell>> availableEnemies = server.getField().getAvailableEnemies(cells);
        availableEnemies.remove(currentCreature);
        AvailableEnemiesCommand availableEnemiesCommand =
                new AvailableEnemiesCommand(availableEnemies);
        return Arrays.asList(availableCellsCommand, availableEnemiesCommand);
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
