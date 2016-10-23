package rk.game.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rk.game.model.Creature;

import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CreaturesService {
    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, List<Creature>> getRaces() {
        return races;
    }

    public void setRaces(Map<String, List<Creature>> races) {
        this.races = races;
    }

    private Map<String, List<Creature>> races = new HashMap<>();

    @PostConstruct
    void loadCreaturesResources(){
        File dir = new File("./src/main/resources/creatures");
        FileFilter filter = new FileNameExtensionFilter("", "crt");
        File[] jsons = dir.listFiles(pathname -> filter.accept(pathname));
        for(File file: jsons){
            try {
                Creature[] creatures = objectMapper.readValue(file, Creature[].class);
                List<Creature> filtered = Arrays.asList(creatures)
                        .stream().filter(creature -> creature.getName() != null && creature.getName().length() > 0)
                        .collect(Collectors.toList());
                races.put(file.getName(), filtered);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
