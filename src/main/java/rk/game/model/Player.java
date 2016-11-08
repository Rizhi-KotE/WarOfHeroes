package rk.game.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Player {
    private String username;
    private List<CreaturesStack> creatures;

    public Player(String username, List<CreaturesStack> creatures) {
        this.username = username;
        this.creatures = new ArrayList<>(creatures);
    }
}
