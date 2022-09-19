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
import ru.itm.gendata.components.TransFuelEntityGenerator;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransFuel;
import ru.itm.gendata.entity.trans.TransFuelRepository;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Основной контроллер для организации генерации данных и записи их в h2
 */
@Controller
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    /**Флаг отключения сервиса*/
    private static Boolean startService = true;

    /**Инжектируем генератор trans_fuel и trans_fuel репозиторий для записи в базу h2*/
    private TransFuelEntityGenerator transFuelEntityGenerator;
    private TransFuelRepository transFuelRepository;

    @Autowired
    public MainController(TransFuelEntityGenerator transFuelEntityGenerator, TransFuelRepository transFuelRepository) {
        this.transFuelEntityGenerator = transFuelEntityGenerator;
        this.transFuelEntityGenerator.getEntity();          //генерируем первое значенние
        this.transFuelRepository = transFuelRepository;
    }

    /**
     * Заполним модель данных о текущем оборудовании для фронта
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("id", TransFuelEntityGenerator.getEquipId());
        model.put("sn", TransFuelEntityGenerator.getSn());
        model.put("nameEq", TransFuelEntityGenerator.getNameEq());
        model.put("typEq", TransFuelEntityGenerator.getEquipType());
        model.put("fuelFull", TransFuelEntityGenerator.getFuelFull());
        model.put("fuelConsumptionPerHour", TransFuelEntityGenerator.getFuelConsumptionPerHour());
        model.put("shiftDate", TransFuelEntityGenerator.getShiftDate().getTime());
        model.put("timeRead", TransFuelEntityGenerator.getTimeRead().getTime());
        model.put("fuelLevel", TransFuelEntityGenerator.getFuelLevel());
        return "index.html";
    }

    /**
     * Кнопка включить генерацию trans_fuel
     * @param model
     * @return
     */
    @RequestMapping(value="/fuel_on", method= RequestMethod.GET)
    public String fuelOn(Map<String, Object> model) {
        model.put("fuelLevel", TransFuelEntityGenerator.getFuelLevel());
        TransFuelEntityGenerator.setGeneratorStart(true);
        return  "redirect:/";
    }

    /**
     * Кнопка выключить генерацию trans_fuel
     * @param model
     * @return
     */
    @RequestMapping(value="/fuel_off", method= RequestMethod.GET)
    public String fuelOff(Map<String, Object> model) {
        model.put("fuelLevel", TransFuelEntityGenerator.getFuelLevel());
        TransFuelEntityGenerator.setGeneratorStart(false);
        return  "redirect:/";
    }

    /**
     * Выход из сервиса
     * @return
     */
    @RequestMapping(value="/exit", method= RequestMethod.GET)
    public String exitServ() {
        logger.info("exit");
        MainController.startService = false;
        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException ex) {
            logger.info("sleep aborting");
        }
        System.exit(0);
        return  "redirect:/";
    }


    /**
     * Автозапуск сервиса после создания контекста
     */
    @EventListener(ApplicationReadyEvent.class)
    private void startGenerations(){
        /**Цикл пока не пришла команда на завершение*/
        while(startService){
            /**Если нужно генерировать trans_fuel, то делаем это */
            if(TransFuelEntityGenerator.getGeneratorStart()){
                /**Получаем новые данные*/
                AbstractEntity abstractEntity = transFuelEntityGenerator.getEntity();
                logger.info(abstractEntity.toString());
                /**Пишем их в базу*/
                transFuelRepository.save((TransFuel) abstractEntity);
                /**Ждем 300сек или 5 мин*/
                try {
                    TimeUnit.SECONDS.sleep(300L);
                } catch (InterruptedException ex) {
                    logger.info("sleep aborting");
                }
            }
            else{
                /**Раз в 2 сек проверяет не пришла ли команда запустить генерацию данных*/
                try {
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException ex) {
                    logger.info("sleep aborting");
                }
            }
        }
        System.exit(0);
    }
}
