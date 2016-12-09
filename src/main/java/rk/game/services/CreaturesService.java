package rk.game.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rk.game.model.Creature;
import rk.game.model.Race;

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

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }

    private List<Race> races = new ArrayList<>();

    @PostConstruct
    void loadCreaturesResources(){
        File dir = new File("./src/main/resources/creatures");
        FileFilter filter = new FileNameExtensionFilter("", "crt");
        File[] jsons = dir.listFiles(pathname -> filter.accept(pathname));
        for(File file: jsons){
            try {
                Object
                Race race = objectMapper.readValue(file, Race.class);
                races.add(race);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
