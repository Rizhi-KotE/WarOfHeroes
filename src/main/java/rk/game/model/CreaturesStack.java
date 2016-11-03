package rk.game.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import rk.game.model.Creature;

@Getter
@Setter
public class CreaturesStack {
    private Creature creature;
    private int size;

    private int health;

    public void changeHealth(int delta) {
        health += delta;
        size = health / creature.getHealth() + health % creature.getHealth() == 0 ? 0 : 1;
    }

    public CreaturesStack() {

    }

    public CreaturesStack(Creature creature, int i) {
        this.creature = creature;
        this.size = i;
        health = creature.getHealth() * i;
    }

    public boolean isAlive() {
        return size > 0;
    }
}
