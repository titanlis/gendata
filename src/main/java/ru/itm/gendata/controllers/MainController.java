package ru.itm.gendata.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.services.*;

import java.util.Map;

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
        model.put("keysDrilling", allService.getTransKeysDrillingService().getData());
        model.put("transNetwork", allService.getTransNetworkService().getData());
        model.put("transCycle", (allService.getTransCycleService().isStarting())?"p-3 border bg-info bg-gradient":"p-3 border bg-light");
        model.put("transRefuel", (allService.getTransRefuelService().isStarting())?"p-3 border bg-info bg-gradient":"p-3 border bg-light");

        return "index.html";
    }

    /**
     * Кнопка включить генерацию trans_fuel
     * @param model
     * @return
     */
    @RequestMapping(value="/fuel_on", method= RequestMethod.GET)
    public String fuelOn(Map<String, Object> model) {
        allService.on("trans_fuel");
        allService.generateOne("trans_fuel");
        return  "redirect:/";
    }

    /**
     * Кнопка выключить генерацию trans_fuel
     * @param model
     * @return
     */
    @RequestMapping(value="/fuel_off", method= RequestMethod.GET)
    public String fuelOff(Map<String, Object> model) {
        allService.off("trans_fuel");
        return  "redirect:/";
    }

    /**
     * Кнопка включить генерацию trans_coord
     * @param model
     * @return
     */
    @RequestMapping(value="/coord_on", method= RequestMethod.GET)
    public String coordOn(Map<String, Object> model) {
        allService.on("trans_coord");
        allService.generateOne("trans_coord");
        return  "redirect:/";
    }

    /**
     * Кнопка выключить генерацию trans_coord
     * @param model
     * @return
     */
    @RequestMapping(value="/coord_off", method= RequestMethod.GET)
    public String coordOff(Map<String, Object> model) {
        allService.off("trans_coord");
        return  "redirect:/";
    }


    /**
     * Кнопка включить генерацию trans_sensor
     * @param model
     * @return
     */
    @RequestMapping(value="/sensor_on", method= RequestMethod.GET)
    public String sensorOn(Map<String, Object> model) {
        allService.on("trans_sensor");
        allService.generateOne("trans_sensor");
        return  "redirect:/";
    }

    /**
     * Кнопка выключить генерацию trans_sensor
     * @param model
     * @return
     */
    @RequestMapping(value="/sensor_off", method= RequestMethod.GET)
    public String sensorOff(Map<String, Object> model) {
        allService.off("trans_sensor");
        return  "redirect:/";
    }

    @RequestMapping(value="/keys_cycles_on", method= RequestMethod.GET)
    public String keysCyclesOn(Map<String, Object> model) {
        allService.on("trans_keys_cycle");
        allService.generateOne("trans_keys_cycle");
        return  "redirect:/";
    }

    @RequestMapping(value="/keys_cycles_off", method= RequestMethod.GET)
    public String keysCyclesOff(Map<String, Object> model) {
        allService.off("trans_keys_cycle");
        return  "redirect:/";
    }

    @RequestMapping(value="/keys_drilling_on", method= RequestMethod.GET)
    public String keysDrillingOn(Map<String, Object> model) {
        allService.on("trans_keys_drilling");
        allService.generateOne("trans_keys_drilling");
        return  "redirect:/";
    }

    @RequestMapping(value="/keys_drilling_off", method= RequestMethod.GET)
    public String keysDrillingOff(Map<String, Object> model) {
        allService.off("trans_keys_drilling");
        return  "redirect:/";
    }

    @RequestMapping(value="/trans_network_on", method= RequestMethod.GET)
    public String transNetworkOn(Map<String, Object> model) {
        allService.on("trans_network");
        allService.generateOne("trans_network");
        return  "redirect:/";
    }

    @RequestMapping(value="/trans_network_off", method= RequestMethod.GET)
    public String transNetworkOff(Map<String, Object> model) {
        allService.off("trans_network");
        return  "redirect:/";
    }


    @RequestMapping(value="/trans_cycle_on", method= RequestMethod.GET)
    public String transCycleOn(Map<String, Object> model) {
        //allService.on("trans_cycles");
        allService.transGenerateOneAndSave("trans_cycles");
        allService.on("trans_cycles");

//        allService.generateOne("trans_cycles");
        return  "redirect:/";
    }

    /**
     * Генерация и сохранение одной записи tarns_cycle
     * @param model
     * @return
     */
    @RequestMapping(value="/trans_cycle_one", method= RequestMethod.GET)
    public String transCycleOne(Map<String, Object> model) {
        allService.transGenerateOneAndSave("trans_cycles");
        return  "redirect:/";
    }

    @RequestMapping(value="/trans_cycle_off", method= RequestMethod.GET)
    public String transCycleOff(Map<String, Object> model) {
        allService.off("trans_cycles");
        return  "redirect:/";
    }


    @RequestMapping(value="/trans_refuel_on", method= RequestMethod.GET)
    public String transRefuelOn(Map<String, Object> model) {
        allService.transGenerateOneAndSave("trans_refuel");
        allService.on("trans_refuel");
        return  "redirect:/";
    }

    /**
     * Генерация и сохранение одной записи tarns_cycle
     * @param model
     * @return
     */
    @RequestMapping(value="/trans_refuel_one", method= RequestMethod.GET)
    public String transRefuelOne(Map<String, Object> model) {
        allService.transGenerateOneAndSave("trans_refuel");
        return  "redirect:/";
    }

    @RequestMapping(value="/trans_refuel_off", method= RequestMethod.GET)
    public String transRefuelOff(Map<String, Object> model) {
        allService.off("trans_refuel");
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
