package rk.game.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rk.game.model.Creature;
import rk.game.services.CreaturesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CreaturesController {
    @Autowired
    private CreaturesService service;

    @RequestMapping(value = "/creature")
    List<List<Creature>> getCretures(){
        return new ArrayList<>(service.getRaces().values());
    }
}
