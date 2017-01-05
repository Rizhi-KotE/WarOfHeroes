package rk.game.core;

import gnu.trove.map.hash.TObjectDoubleHashMap;
import lombok.Data;
import org.springframework.stereotype.Component;
import rk.game.model.Cell;
import rk.game.model.CreaturesStack;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
@Component
public class Field {

    enum AvailableCell {
        withCreature(cell -> cell.getStack() != null),
        withoutCreature(cell -> cell.getStack() == null);

        private Predicate<Cell> predicate;

        public boolean test(Cell cell) {
            return predicate.test(cell);
        }

        AvailableCell(Predicate<Cell> predicate) {
            this.predicate = predicate;
        }
    }

    public static final int SIZE = 10;
    public static final int RADIX = 3;
    public static final int OFFSET = 1;
    public static final int COMBINES_AMOUT = 9;
    private Cell[][] matrix = new Cell[SIZE][SIZE];
    private Map<CreaturesStack, Cell> creatures = new HashMap();

    public Field (){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrix[i][j] = new Cell(i, j);
            }
        }
    }

    public List<Cell> getAvailableAria(Cell startCell, int maxDistance) {
        TObjectDoubleHashMap<Cell> weights = new TObjectDoubleHashMap<>(10, 0.5f, Double.MAX_VALUE - 100);
        LinkedList<Cell> queue = new LinkedList<>();
        queue.add(startCell);
        weights.put(startCell, 0);
        while (!queue.isEmpty()) {
            Cell currentCell = queue.pop();
            List<Cell> availableCells = getNeighbourCells(currentCell, AvailableCell.withoutCreature);
            for (Cell cell : availableCells) {
                double distance = getDistance(currentCell, cell);
                double weight = weights.get(currentCell) + distance;
                if (weight < weights.get(cell) && weight <= maxDistance) {
                    weights.put(cell, weight);
                    queue.push(cell);
                }
            }
        }
        return weights.keySet().stream().collect(Collectors.toList());
    }

    private double getDistance(Cell currentCell, Cell cell) {
        return Point.distance(currentCell.x, currentCell.y, cell.x, cell.y);
    }

    private List<Cell> getNeighbourCells(Cell cell, AvailableCell flag) {
        List<Cell> out = new ArrayList<>();
        for (int i = 0; i < COMBINES_AMOUT; i++) {
            int deltaX = i % RADIX - OFFSET;
            int deltaY = i / RADIX - OFFSET;
            if (deltaX == 0 && deltaY == 0) {
                continue;
            }
            int x = cell.x + deltaX;
            int y = cell.y + deltaY;
            if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
                if (flag.test(matrix[x][y]))
                    out.add(matrix[x][y]);
            }
        }
        return out;
    }

    public Cell getCell(CreaturesStack stack){
        return creatures.get(stack);
    }

    public Cell getCell(int x, int y){
        return matrix[x][y];
    }

    public int moveCreature(CreaturesStack stack, Cell cell) {
        Cell outputCell = creatures.get(stack);
        outputCell.setStack(null);
        Cell inputCell = matrix[cell.getX()][cell.getY()];
        matrix[cell.getX()][cell.getY()].setStack(stack);
        creatures.put(stack, inputCell);
        return (int) Math.ceil(Point.distance(outputCell.x, outputCell.y, cell.x, cell.y));
    }

    public CreaturesStack getCreature(Cell cell){
        return matrix[cell.x][cell.y].getStack();
    }

    public void moveCreature(CreaturesStack stack, int x, int y){
        Cell outputCell = creatures.get(stack);
        outputCell.setStack(null);
        Cell inputCell = matrix[x][y];
        inputCell.setStack(stack);
        creatures.put(stack, inputCell);
    }

    public List<Cell> getAvailableEnemies(Cell cell, int distance) {
        List<Cell> cells = getAvailableAria(cell, distance);
        return getAvailableEnemies(cells);
    }

    public List<Cell> getAvailableEnemies(List<Cell> cells) {
        return cells.stream()
                .map(cell -> getNeighbourCells(cell, AvailableCell.withCreature))
                .flatMap(List::stream)
                .collect(Collectors.toSet())
                .stream().collect(Collectors.toList());
    }

    public void removeCreature(CreaturesStack stack){
        Cell cell = creatures.remove(stack);
        matrix[cell.getX()][cell.getY()].setStack(null);
    }

    public void addCreature(CreaturesStack stack, int x, int y){
        Cell cell = matrix[x][y];
        cell.setStack(stack);
        creatures.put(stack, cell);
    }
}
