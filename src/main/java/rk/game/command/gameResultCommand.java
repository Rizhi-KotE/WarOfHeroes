package rk.game.command;

import lombok.Getter;

@Getter
public class gameResultCommand implements Command {

    private final String type;

    public gameResultCommand(String message) {
        type = message;
    }
}
