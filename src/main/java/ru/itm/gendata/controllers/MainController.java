package ru.itm.gendata.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.itm.gendata.components.TransCoordEntitiesGenerator;
import ru.itm.gendata.components.TransFuelEntityGenerator;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.*;
import ru.itm.gendata.services.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Основной контроллер для организации генерации данных и записи их в h2
 */
@Controller
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    private InitService initService;
    private AllService allService;

    @Autowired
    public void setAllService(AllService allService) {
        this.allService = allService;
    }

    @Autowired
    public void setInitService(InitService initService) {
        this.initService = initService;
    }

    /**
     * Заполним модель данных о текущем оборудовании для фронта
     * @param model
     * @return
     */
    @RequestMapping(value="/", method= RequestMethod.GET)
    public String welcome(Map<String, Object> model) {
        model.put("id", SystemConfig.getEquipmentId());
        model.put("sn", SystemConfig.getSn());
        model.put("nameEq", SystemConfig.getEquipmentName());
        model.put("typEq", SystemConfig.getEquipmentType());
        model.put("fuelFull", SystemConfig.getFuelFull());
        model.put("fuelConsumptionPerHour", SystemConfig.getFuelConsumptionPerHour());
        model.put("shiftDate", allService.getTransFuelService().getShiftDate().getTime());
        model.put("timeRead", allService.getTransFuelService().getTimeRead().getTime());

        model.put("fuelLevel", allService.getTransFuelService().getData());
        model.put("coordLevel", allService.getTransCoordService().getData());
        model.put("sensorLevel", allService.getTransSensorService().getData());
        model.put("keysCycle", allService.getTransKeysCycleService().getData());

        return "index.html";
    }

    /**
     * Кнопка включить генерацию trans_fuel
     * @param model
     * @return
     */
    @RequestMapping(value="/fuel_on", method= RequestMethod.GET)
    public String fuelOn(Map<String, Object> model) {
        allService.fuelOnOF(true);
        allService.generateOneTransFuel();
        return  "redirect:/";
    }

    /**
     * Кнопка выключить генерацию trans_fuel
     * @param model
     * @return
     */
    @RequestMapping(value="/fuel_off", method= RequestMethod.GET)
    public String fuelOff(Map<String, Object> model) {
        allService.fuelOnOF(false);
        return  "redirect:/";
    }

    /**
     * Кнопка включить генерацию trans_coord
     * @param model
     * @return
     */
    @RequestMapping(value="/coord_on", method= RequestMethod.GET)
    public String coordOn(Map<String, Object> model) {
        allService.coordOnOF(true);
        allService.generateOneTransCoord();
        return  "redirect:/";
    }

    /**
     * Кнопка выключить генерацию trans_coord
     * @param model
     * @return
     */
    @RequestMapping(value="/coord_off", method= RequestMethod.GET)
    public String coordOff(Map<String, Object> model) {
        allService.coordOnOF(false);
        return  "redirect:/";
    }


    /**
     * Кнопка включить генерацию trans_sensor
     * @param model
     * @return
     */
    @RequestMapping(value="/sensor_on", method= RequestMethod.GET)
    public String sensorOn(Map<String, Object> model) {
        allService.sensorOnOF(true);
        allService.generateOneTransSensor();
        return  "redirect:/";
    }

    /**
     * Кнопка выключить генерацию trans_sensor
     * @param model
     * @return
     */
    @RequestMapping(value="/sensor_off", method= RequestMethod.GET)
    public String sensorOff(Map<String, Object> model) {
        allService.sensorOnOF(false);
        return  "redirect:/";
    }

    @RequestMapping(value="/keys_cycles_on", method= RequestMethod.GET)
    public String keysCyclesOn(Map<String, Object> model) {
        allService.keysCycleOnOF(true);
        allService.generateOneTransKeysCycle();
        return  "redirect:/";
    }

    @RequestMapping(value="/keys_cycles_off", method= RequestMethod.GET)
    public String keysCyclesOff(Map<String, Object> model) {
        allService.keysCycleOnOF(false);
        return  "redirect:/";
    }


    /**
     * Автозапуск сервиса после создания контекста
     */
    @EventListener(ApplicationReadyEvent.class)
    private void startGenerations(){
        initService.initEquipmentInfo();
    }
}
