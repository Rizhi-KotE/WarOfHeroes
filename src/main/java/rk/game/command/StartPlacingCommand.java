package rk.game.command;

import lombok.Data;

import java.util.List;

@Data
public class StartPlacingCommand {
    private final String type = "startPlacing";
    private List<AddCreatureCommand> list;

    public StartPlacingCommand(List<AddCreatureCommand> list) {
        this.list = list;
    }
}
