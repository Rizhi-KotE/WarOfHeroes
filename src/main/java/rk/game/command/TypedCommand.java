package rk.game.command;

import lombok.Data;

@Data
public class TypedCommand implements Command {

    private String type;

    public TypedCommand(String type) {
        this.type = type;
    }
}
