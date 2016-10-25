package rk.game.model;

import lombok.Data;

@Data
public class Creature {
    private String name;
    private int attack;
    private int defence;
    private String damage;
    private int shoots;
    private int speed;
    private int health;
    private int mana;
    private String image;
}
