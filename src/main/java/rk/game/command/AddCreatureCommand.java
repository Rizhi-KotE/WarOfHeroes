package rk.game.command;

import lombok.Data;
import rk.game.model.CreaturesStack;

@Data
public class AddCreatureCommand implements Command {
    private final String type = "addCreature";
    private int x;
    private int y;
    private boolean own;
    private CreaturesStack stack;

    public AddCreatureCommand(CreaturesStack stack, int x, int y) {
        this.x = x;
        this.y = y;
        this.stack = stack;
    }
}
