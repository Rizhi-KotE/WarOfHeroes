package rk.game.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class Field {
    private Cell[][] matrix = new Cell[10][10];
    private Map<CreaturesStack, Cell> creatures = new HashMap();

    public Field (){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                matrix[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell getCell(CreaturesStack stack){
        return creatures.get(stack);
    }

    public void moveCreature(CreaturesStack stack, Cell cell){
        Cell currentCell = matrix[cell.getX()][cell.getY()];
        matrix[cell.getX()][cell.getY()].setStack(stack);
        creatures.put(stack, currentCell);
    }

    public void moveCreature(CreaturesStack stack, int x, int y){
        Cell currentCell = matrix[x][y];
        currentCell.setStack(stack);
        creatures.put(stack, currentCell);
    }

    public void removeCreature(CreaturesStack stack){
        Cell cell = creatures.remove(stack);
        matrix[cell.getX()][cell.getY()].setStack(null);
    }

    public void addCreature(CreaturesStack stack, Cell cell){
        Cell currentCreature = matrix[cell.getX()][cell.getY()];
        currentCreature.setStack(stack);
        creatures.put(stack, cell);
    }


    public void addCreature(CreaturesStack stack, int x, int y){
        Cell cell = matrix[x][y];
        cell.setStack(stack);
        creatures.put(stack, cell);
    }
}
