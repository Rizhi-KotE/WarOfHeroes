package rk.game.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Player {
    private String username;
    List<CreaturesStack> creatures;
}
