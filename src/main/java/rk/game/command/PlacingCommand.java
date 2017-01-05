package rk.game.command;

import lombok.Data;

import java.util.List;

@Data
public class PlacingCommand implements Command{
    private final String type = "startPlacing";
    private List<AddCreatureCommand> list;

    public PlacingCommand(List<AddCreatureCommand> list) {
        this.list = list;
    }
}
