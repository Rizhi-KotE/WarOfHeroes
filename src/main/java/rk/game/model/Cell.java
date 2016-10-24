package rk.game.model;

import lombok.Data;

@Data
public class Cell {
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

    public boolean isAvailable(){
        return available && stack == null;
    }
}
