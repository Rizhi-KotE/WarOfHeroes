package rk.game.model;

import lombok.Data;

@Data
public class Field {
    private Cell[][] matrix = new Cell[10][10];

    public Field (){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                matrix[i][j] = new Cell(i, j);
            }
        }
    }
}
