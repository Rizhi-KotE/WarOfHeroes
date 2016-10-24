package rk.game.command;

import lombok.Data;
import rk.game.model.CreaturesStack;

@Data
public class AddCreatureCommand {
    private final String type = "addCreature";
    private int x;
    private int y;
    private CreaturesStack stack;
}
