package rk.game.model;

import gnu.trove.map.hash.TObjectDoubleHashMap;
import javafx.util.Pair;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class Field {

    enum AvailableCell {
        withCreature, withoutCreature
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

    public List<Cell> getAvailableAria(CreaturesStack creature) {
        Cell startCell = creatures.get(creature);
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
                if (weight < weights.get(cell) && weight <= creature.getCreature().getSpeed()) {
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
                if (matrix[x][y].getStack() == null || flag == AvailableCell.withCreature)
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

    public void moveCreature(CreaturesStack stack, Cell cell){
        Cell outputCell = creatures.get(stack);
        outputCell.setStack(null);
        Cell inputCell = matrix[cell.getX()][cell.getY()];
        matrix[cell.getX()][cell.getY()].setStack(stack);
        creatures.put(stack, inputCell);
    }

    public void moveCreature(CreaturesStack stack, int x, int y){
        Cell outputCell = creatures.get(stack);
        outputCell.setStack(null);
        Cell inputCell = matrix[x][y];
        inputCell.setStack(stack);
        creatures.put(stack, inputCell);
    }

    public Map<CreaturesStack, List<Cell>> getAvailableEnemies(CreaturesStack creature) {
        List<Cell> cells = getAvailableAria(creature);
        return getAvailableEnemies(cells);
    }

    public Map<CreaturesStack, List<Cell>> getAvailableEnemies(List<Cell> cells) {
        HashMap<CreaturesStack, Set<Cell>> availableEnemies = new HashMap<>();
        for (Cell cell : cells) {
            List<Cell> neighbours = getNeighbourCells(cell, AvailableCell.withCreature);
            for (Cell neighbor : neighbours) {
                if (neighbor.getStack() != null) {
                    Set<Cell> cellSet = availableEnemies.get(neighbor.getStack());
                    if (cellSet == null) {
                        cellSet = new HashSet<>();
                    }
                    cellSet.add(cell);
                    availableEnemies.put(neighbor.getStack(), cellSet);
                }
            }
        }
        return availableEnemies.entrySet()
                .stream()
                .collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue().stream().collect(Collectors.toList())));
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
