package rk.game.dto;

import lombok.Data;
import rk.game.model.Cell;

@Data
public class AttackMessage {
    private Cell targetCell;

    public AttackMessage() {
    }

    public AttackMessage(Cell targetCell) {
        this.targetCell = targetCell;
    }
}
