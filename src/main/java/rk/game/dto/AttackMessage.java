package rk.game.dto;

import lombok.Data;
import rk.game.model.Cell;

@Data
public class AttackMessage {
    private Cell attackCell;
    private Cell targetCell;
}
