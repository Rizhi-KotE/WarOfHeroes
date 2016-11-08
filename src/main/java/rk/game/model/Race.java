package rk.game.model;

import lombok.Data;

import java.util.List;

@Data
public class Race {
    private String name;
    private String image;
    private List<Creature> creatures;
}
