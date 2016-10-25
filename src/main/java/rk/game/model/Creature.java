package rk.game.model;

import lombok.Data;

@Data
public class Creature {
    private String name;
    private int attack;
    private int defence;
    private int damage;
    private int shoots;
    private int speed;
    private int health;
    private int mana;
    private String image;
}
