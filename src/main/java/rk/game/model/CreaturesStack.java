package rk.game.model;

import lombok.Data;
import rk.game.model.Creature;

@Data
public class CreaturesStack {
    private Creature creature;
    private int size;

    public CreaturesStack(Creature creature, int i) {
        this.creature = creature;
        this.size = i;
    }

    public CreaturesStack() {

    }
}
