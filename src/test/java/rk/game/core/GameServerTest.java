package rk.game.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rk.game.command.Command;
import rk.game.dto.AttackMessage;
import rk.game.model.Cell;
import rk.game.model.Creature;
import rk.game.model.CreaturesStack;
import rk.game.model.Player;
import rk.game.services.CreaturesService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GameServerTest {

    private GameServer server;

    private Player firstPlayer = new Player();

    private Player secondPlayer = new Player();

    private Creature creature;

    @Before
    public void onStart() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        creature = mapper.readValue(new File("./src/test/resources/imps.crt"), Creature.class);
        firstPlayer.setCreatures(Arrays.asList(new CreaturesStack(creature, 10)));
        secondPlayer.setCreatures(Arrays.asList(new CreaturesStack(creature, 10)));
        server = new GameServer(Arrays.asList(firstPlayer, secondPlayer));
    }

    @Test
    public void testAttackMessage() throws Exception {
        server.messageMove(new Cell(5, 5));
        server.messageWait();
        server.messageMove(new Cell(6, 6));
        Map<Player, List<Command>> playerListMap = server.messageAttack(new AttackMessage(new Cell(5, 5)));
        assertTrue(playerListMap.get(firstPlayer).size() > 2);
    }


}