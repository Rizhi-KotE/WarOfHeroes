package rk.game.command;

import lombok.Data;
import rk.game.model.Cell;

@Data
public class CreatureDiedCommand implements Command {
    private final String type = "creatureDied";

    Cell cell;

    public CreatureDiedCommand(Cell cell) {
        this.cell = cell;
    }
}
