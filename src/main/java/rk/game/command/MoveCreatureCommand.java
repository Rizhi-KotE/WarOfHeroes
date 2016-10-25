package rk.game.command;

import lombok.Data;
import rk.game.model.Cell;
import rk.game.model.CreaturesStack;

@Data
public class MoveCreatureCommand implements Command{
    private final String type = "moveCreature";
    private int inX;
    private int inY;
    private int outX;
    private int outY;
    private CreaturesStack stack;
}
