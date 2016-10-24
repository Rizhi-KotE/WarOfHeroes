package rk.game.command;

import lombok.Data;

import java.util.List;

@Data
public class StartPlacingCommand {
    final String type = "startPlacing";
    List<AddCreatureCommand> list;

    public StartPlacingCommand(List<AddCreatureCommand> list) {
        this.list = list;
    }
}
