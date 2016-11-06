package rk.game.command;

import lombok.Data;
import rk.game.dto.QueuePlace;
import rk.game.model.CreaturesStack;

import java.util.List;

@Data
public class CreatureQueueCommand implements Command {

    private final String type = "creatureQueue";

    private QueuePlace queue[];

    public CreatureQueueCommand(List<QueuePlace> queue) {
        this.queue = queue.toArray(new QueuePlace[queue.size()]);
    }
}
