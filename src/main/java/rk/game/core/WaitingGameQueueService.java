package rk.game.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import rk.game.controller.GameController;
import rk.game.model.Creature;
import rk.game.model.CreaturesStack;
import rk.game.model.Player;
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
        if (waitingQueue.size() < 2) {
            return "wait";
        }
        List<Player> playersToNewGame = Arrays.asList(waitingQueue.pollFirst(), waitingQueue.pollFirst());
        dispatcher.runGame(playersToNewGame);
        return "gameStart";
    }
}
