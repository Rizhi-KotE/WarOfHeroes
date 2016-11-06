package rk.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Cell {
    @JsonIgnore
    private CreaturesStack stack;
    private boolean available = false;
    public int x;
    public int y;

    public Cell(){

    }

    public Cell(int x, int y){
        this.x = x;
        this.y = y;
    }
}
