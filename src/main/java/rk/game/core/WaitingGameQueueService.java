package rk.game.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import rk.game.model.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class WaitingGameQueueService {

    @Autowired
    private GameServerDispatcher dispatcher;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private LinkedList<Player> waitingQueue = new LinkedList<>();

    public void addPlayer(Player player) {
        waitingQueue.add(player);
        messagingTemplate.convertAndSendToUser(player.getUsername(), "/queue/game", Arrays.asList(player.getUsername(), "wait"));
        runGame();
    }

    private void runGame() {
        if (waitingQueue.size() < 1) {
            return;
        }
        List<Player> playersToNewGame = Arrays.asList(waitingQueue.pollFirst());
        dispatcher.runGame(playersToNewGame);
    }
}
