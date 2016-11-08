package rk.game.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import rk.game.controller.GameController;
import rk.game.model.Creature;
import rk.game.model.CreaturesStack;
import rk.game.model.Player;
import rk.game.model.Race;
import rk.game.services.CreaturesService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitingGameQueueService {

    @Autowired
    private GameServerDispatcher dispatcher;

    private LinkedList<Player> waitingQueue = new LinkedList<>();

    public String addPlayer(Player player) {
        waitingQueue.add(player);
        if (waitingQueue.size() < 1) {
            return "wait";
        }
        List<Player> playersToNewGame = Arrays.asList(waitingQueue.pollFirst(), addWhippingBoy());
        dispatcher.runGame(playersToNewGame);
        return "gameStart";
    }

    @Autowired
    private CreaturesService service;

    private Player addWhippingBoy() {
        Race race = service.getRaces().get(0);
//        List<CreaturesStack> stacks = race.getCreatures().stream().map(creature -> new CreaturesStack(creature, 10))
//                .collect(Collectors.toList());
        List<CreaturesStack> stacks = Arrays.asList(new CreaturesStack(race.getCreatures().get(0), 10));
        Player whippingBoy = new Player("whipping", stacks);
        return whippingBoy;
    }


}
