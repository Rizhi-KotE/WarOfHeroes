package rk.game.dto;

import lombok.Data;
import rk.game.model.CreaturesStack;

@Data
public class QueuePlace {
    private CreaturesStack stack;

    private boolean isEnemy;

    public QueuePlace(CreaturesStack stack, boolean isEnemy) {
        this.stack = stack;
        this.isEnemy = isEnemy;
    }
}
