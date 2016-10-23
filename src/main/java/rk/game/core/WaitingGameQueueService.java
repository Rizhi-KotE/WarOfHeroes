package rk.game.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import rk.game.controller.GameController;
import rk.game.model.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
        List<Player> playersToNewGame = Arrays.asList(waitingQueue.pollFirst());
        dispatcher.runGame(playersToNewGame);
        return "game_start";
    }
}
