package rk.game.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rk.game.model.Creature;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class CreaturesService {
    @Autowired
    ObjectMapper objectMapper;

    private Map<String, List<Creature>> races = new HashMap<>();

    @PostConstruct
    void loadCreaturesResources(){
        File dir = new File("./src/main/resources/creatures");
        File[] jsons = dir.listFiles();
        for(File file: jsons){
            try {
                Creature[] creatures = objectMapper.readValue(file, Creature[].class);
                races.put(file.getName(), Arrays.asList(creatures));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
