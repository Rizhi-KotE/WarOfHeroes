package rk.game.command;

import lombok.Data;
import lombok.Getter;
import rk.game.model.Cell;

@Getter
public class ChangeTurnCommand implements Command {

    private final String type = "changeTurn";

    private Cell currentCell;

    private boolean yourTurn;

    public ChangeTurnCommand(Cell currentCell, boolean yourTurn) {
        this.currentCell = currentCell;
        this.yourTurn = yourTurn;
    }
}
