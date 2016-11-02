package rk.game.dto;

import lombok.Data;
import rk.game.model.Creature;

@Data
public class CreatureChoise {
    public int size;

    public Creature creature;
}
