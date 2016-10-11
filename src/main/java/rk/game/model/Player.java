package rk.game.model;

import lombok.Data;

import java.util.List;

@Data
public class Player {
    private String username;
    List<CreaturesStack> creatures;
}
