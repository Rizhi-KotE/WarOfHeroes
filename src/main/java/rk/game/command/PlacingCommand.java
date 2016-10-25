package rk.game.command;

import lombok.Data;

import java.util.List;

@Data
public class PlacingCommand implements Command{
    private final String type = "startPlacing";
    private List<GetCreatureCommand> list;

    public PlacingCommand(List<GetCreatureCommand> list) {
        this.list = list;
    }
}
