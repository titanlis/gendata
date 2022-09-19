package ru.itm.gendata.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.itm.gendata.components.TransFuelEntityGenerator;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransFuel;
import ru.itm.gendata.entity.trans.TransFuelRepository;

import java.util.LinkedList;
import java.util.List;

/**
 * Тестовый контроллер
 * В работе не участвует.
 */
@RestController
@RequestMapping("/{name_table}")
public class TransFuelTestController {

    private static Logger logger = LoggerFactory.getLogger(TransFuelTestController.class);

    private TransFuelEntityGenerator transFuelEntityGenerator;
    public TransFuelRepository transFuelRepository;

    @Autowired
    public TransFuelTestController(TransFuelRepository transFuelRepository, TransFuelEntityGenerator transFuelEntityGenerator) {
        this.transFuelRepository = transFuelRepository;
        this.transFuelEntityGenerator=transFuelEntityGenerator;
    }

    @GetMapping("/test")
    public String test1(@PathVariable String name_table) {
        switch (name_table){
            case "trans_fuel" -> {
                return transFuelEntityGenerator.getEntity().toString();
            }
            default -> {return "\'"+name_table+"\' not found";}
        }
    }

    @GetMapping(value = "", produces = "application/json")
    public AbstractEntity getTransFuel(@PathVariable String name_table) {
        switch (name_table){
            case "trans_fuel" -> {
                return transFuelEntityGenerator.getEntity();
            }
            default -> {return null;}
        }
    }

}
