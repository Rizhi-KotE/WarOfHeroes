package rk.game.core;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import rk.game.model.Cell;
import rk.game.model.CreaturesStack;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

@Component
@Scope(value = "prototype")
@Getter
public class CreaturesQueue {
    private LinkedList<CreaturesStack> queue = new LinkedList<>();

    public CreaturesStack getCurrentCreature() {
        return queue.getFirst();
    }

    public void popCreature() {
        queue.addLast(queue.pop());
        if (queue.getFirst() != null)
            queue.getFirst().newStep();
    }

    public void removeCreature(CreaturesStack stack) {
        queue.remove(stack);
    }

    public void addAll(Collection<CreaturesStack> collection){
        queue.addAll(collection);
    }
}
