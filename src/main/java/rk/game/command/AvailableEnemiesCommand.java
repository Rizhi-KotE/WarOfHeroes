package rk.game.command;

import javafx.util.Pair;
import lombok.Data;
import rk.game.model.Cell;
import rk.game.model.CreaturesStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class AvailableEnemiesCommand implements Command{
    private final String type = "availableEnemies";

    List<Pair<CreaturesStack, List<Cell>>> enemiesToCell;

    public AvailableEnemiesCommand(Map<CreaturesStack, List<Cell>> enemiesToCell) {
        this.enemiesToCell = enemiesToCell.entrySet().stream()
                .map(creaturesStackSetEntry -> {
                    return new Pair<>(creaturesStackSetEntry.getKey(), creaturesStackSetEntry.getValue());
                }).collect(Collectors.toList());
    }
}
