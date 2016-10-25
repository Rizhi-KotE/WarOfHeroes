package rk.game.command;

import lombok.Data;
import rk.game.model.CreaturesStack;

@Data
public class GetCreatureCommand {
    private final String type = "addCreature";
    private int x;
    private int y;
    private boolean own;
    private CreaturesStack stack;

    public GetCreatureCommand(CreaturesStack stack, int x, int y) {
        this.x = x;
        this.y = y;
        this.stack = stack;
    }
}
