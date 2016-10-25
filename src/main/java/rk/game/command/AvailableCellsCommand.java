package rk.game.command;

import lombok.Data;
import rk.game.model.Cell;

import java.util.List;

@Data
public class AvailableCellsCommand implements Command{
    private final String type = "availableCells";

    private List<Cell> cells;

    private Cell currentCell;

    public AvailableCellsCommand(Cell cell, List<Cell> cells) {
        this.cells = cells;
        this.currentCell = cell;
    }
}
