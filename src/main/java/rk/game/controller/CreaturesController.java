package rk.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rk.game.model.Creature;
import rk.game.model.Race;
import rk.game.services.CreaturesService;

import java.util.List;
import java.util.Map;

@RestController
public class CreaturesController {
    @Autowired
    private CreaturesService service;

    @RequestMapping(value = "/creature")
    List<Race> getCretures(){
        return service.getRaces();
    }
}
