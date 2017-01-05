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

    private int health = -1;
    private int speed;

    public void changeHealth(int delta) {
        if(health == -1){
            health = creature.getHealth() * size;
        }
        health += delta;
        size = health / creature.getHealth() + (health % creature.getHealth() == 0 ? 0 : 1);
    }

    public CreaturesStack() {

    }

    public CreaturesStack(Creature creature, int i) {
        this.creature = creature;
        this.size = i;
        health = creature.getHealth() * i;
        newStep();
    }

    public int damage(CreaturesStack stack){
        int pureDamage = creature.getDamage() * size;
        double coef = 0.1 * (creature.getAttack() - stack.creature.getDefence());
        int fullDamage = pureDamage + (int) (pureDamage * coef);
        changeHealth(-fullDamage);
        return fullDamage;
    }

    public boolean isAlive() {
        return size > 0;
    }

    public int getSpeed() {
        return speed;
    }

    public void newStep() {
        speed = creature.getSpeed();
    }

    public void move(int distance) {
        speed = speed - distance < 0 ? 0 : speed - distance;
    }
}
