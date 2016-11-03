package rk.game.command;

import lombok.Data;
import rk.game.model.Cell;
import rk.game.model.CreaturesStack;

@Data
public class DamageCommand implements Command {
    private final String type = "damage";

    private Cell currentCell;
    private Cell targetCell;
    private int damage;

    public DamageCommand(Cell currentCell, Cell targetCell, int damage) {

        this.currentCell = currentCell;
        this.targetCell = targetCell;
        this.damage = damage;
    }
}
