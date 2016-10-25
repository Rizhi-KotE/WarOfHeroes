package rk.game.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class FieldTest {
    private Field field = new Field();
    private CreaturesStack stack;


    @Before
    public void onStart() {
        Creature creature = new Creature();
        creature.setSpeed(2);
        stack = new CreaturesStack();
        stack.setCreature(creature);
        field.addCreature(stack, 5, 5);

        CreaturesStack enemy = new CreaturesStack();
        field.addCreature(enemy, 7, 5);
    }

    @Test
    public void getAvailableAria() throws Exception {
        List<Cell> cells = field.getAvailableAria(stack);
        printMatrix(cells);
        assertEquals(cells.size(), 13);
    }

    @Test
    /*
    * get all available creatures(reflective)*/
    public void getAvailableEnemies() throws Exception {
        List<Cell> cells = field.getAvailableAria(stack);
        Map<CreaturesStack, List<Cell>> map = field.getAvailableEnemies(cells);
        printMatrix(map.values().stream().flatMap(List::stream).collect(Collectors.toList()));
        assertEquals(map.size(), 2);
    }

    private void printMatrix(List<Cell> cells) throws JsonProcessingException {
        StringBuilder builder = new StringBuilder();
        int matrix[][] = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (field.getMatrix()[i][j].getStack() != null) {
                    matrix[i][j] = 2;
                }
            }
        }
        for(Cell cell: cells){
            matrix[cell.x][cell.y] |= 1;
        }
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++)
                builder.append(matrix[i][j]);
            builder.append("\n");
        }
        System.out.print(builder.toString());
    }

}