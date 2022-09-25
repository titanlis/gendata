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
import ru.itm.gendata.services.InitService;
import ru.itm.gendata.services.TransCoordService;
import ru.itm.gendata.services.TransFuelService;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Основной контроллер для организации генерации данных и записи их в h2
 */
@Controller
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    private InitService initService;
    private TransFuelService transFuelService;
    private TransCoordService transCoordService;

    @Autowired
    public void setTransCoordService(TransCoordService transCoordService) {
        this.transCoordService = transCoordService;
    }

    @Autowired
    public void setTransFuelService(TransFuelService transFuelService) {
        this.transFuelService = transFuelService;
    }

    @Autowired
    public void setInitService(InitService initService) {
        this.initService = initService;
    }


    /**Флаг отключения сервиса*/
//    private static Boolean startService = true;

    /**Инжектируем генератор trans_fuel и trans_fuel репозиторий для записи в базу h2*/
//    private final TransFuelEntityGenerator transFuelEntityGenerator;
//    private final TransCoordEntitiesGenerator transCoordEntitiesGenerator;
//    private final TransFuelRepository transFuelRepository;
//    private final TransCoordRepository transCoordRepository;

//    @Autowired
//    public MainController(TransFuelEntityGenerator transFuelEntityGenerator, TransCoordEntitiesGenerator transCoordEntitiesGenerator, TransFuelRepository transFuelRepository, TransCoordRepository transCoordRepository) {
//        this.transFuelEntityGenerator = transFuelEntityGenerator;
//        this.transCoordEntitiesGenerator = transCoordEntitiesGenerator;
//        this.transFuelRepository = transFuelRepository;
//        this.transCoordRepository = transCoordRepository;
//    }

//    @Autowired
//    public MainController(
//            TransFuelEntityGenerator transFuelEntityGenerator, TransCoordEntitiesGenerator transCoordEntitiesGenerator,
//            TransFuelRepository transFuelRepository, TransCoordRepository transCoordRepository) {
//        this.transFuelEntityGenerator = transFuelEntityGenerator;
//        this.transCoordEntitiesGenerator = transCoordEntitiesGenerator;
//        this.transFuelRepository = transFuelRepository;
//        this.transCoordRepository = transCoordRepository;
//    }


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
        model.put("shiftDate", transFuelService.getShiftDate().getTime());
        model.put("timeRead", transFuelService.getTimeRead().getTime());
        model.put("fuelLevel", transFuelService.getFuelLevel());
        model.put("coordLevel", transCoordService.getCoordLevel());

        return "index.html";
    }

    /**
     * Кнопка включить генерацию trans_fuel
     * @param model
     * @return
     */
    @RequestMapping(value="/fuel_on", method= RequestMethod.GET)
    public String fuelOn(Map<String, Object> model) {
        if(!transFuelService.isStarting()){
            transFuelService.start();
        }
        model.put("fuelLevel", transFuelService.getFuelLevel());
        return  "redirect:/";
    }

    /**
     * Кнопка выключить генерацию trans_fuel
     * @param model
     * @return
     */
    @RequestMapping(value="/fuel_off", method= RequestMethod.GET)
    public String fuelOff(Map<String, Object> model) {
        if(transFuelService.isStarting()){
            transFuelService.stop();
        }
        model.put("fuelLevel", transFuelService.getFuelLevel());
        return  "redirect:/";
    }

    /**
     * Кнопка включить генерацию trans_coord
     * @param model
     * @return
     */
    @RequestMapping(value="/coord_on", method= RequestMethod.GET)
    public String coordOn(Map<String, Object> model) {
        if(!transCoordService.isStarting()){
            transCoordService.start();
        }
        model.put("coordLevel", transCoordService.getCoordLevel());
        return  "redirect:/";
    }

    /**
     * Кнопка выключить генерацию trans_coord
     * @param model
     * @return
     */
    @RequestMapping(value="/coord_off", method= RequestMethod.GET)
    public String coordOff(Map<String, Object> model) {
        if(transCoordService.isStarting()){
            transCoordService.stop();
        }
        model.put("coordLevel", transCoordService.getCoordLevel());
        return  "redirect:/";
    }



    /**
     * Выход из сервиса
     * @return
     */
//    @RequestMapping(value="/exit", method= RequestMethod.GET)
//    public String exitServ() {
//        logger.info("exit");
//        MainController.startService = false;
//        try {
//            TimeUnit.SECONDS.sleep(5L);
//        } catch (InterruptedException ex) {
//            logger.info("sleep aborting");
//        }
//        (new ShutdownManager()).stopPage();
//        //System.exit(0);
//        return  "redirect:/";
//    }

//    private void transFuelGenerator(){
//        System.out.println("++++");
//        while (!TransFuelEntityGenerator.getGeneratorStart()) {
//            try {
//                TimeUnit.SECONDS.sleep(2L);
//            } catch (InterruptedException ex) {
//                logger.info("sleep aborting");
//            }
//        }
//        /**Получаем новые данные*/
//        AbstractEntity abstractEntity = transFuelEntityGenerator.getEntity();
//        logger.info(abstractEntity.toString());
//        /**Пишем их в базу*/
//        transFuelRepository.save((TransFuel) abstractEntity);
//        /**Ждем 300сек или 5 мин*/
//    }

    /**
     * Автозапуск сервиса после создания контекста
     */
    @EventListener(ApplicationReadyEvent.class)
    private void startGenerations(){
        initService.initEquipmentInfo();
//        /**Цикл пока не пришла команда на завершение*/
//        while(startService){
//            /**Если нужно генерировать trans_fuel, то делаем это */
////            if(TransFuelEntityGenerator.getGeneratorStart()||TransCoordEntitiesGenerator.getGeneratorStart()){
//            if(TransFuelEntityGenerator.getGeneratorStart()){
//                /**Получаем новые данные*/
//                AbstractEntity abstractEntity = transFuelEntityGenerator.getEntity();
//                logger.info(abstractEntity.toString());
//                /**Пишем их в базу*/
//                transFuelRepository.save((TransFuel) abstractEntity);
//                /**Ждем 300сек или 5 мин*/
//            }
//
////                if(TransCoordEntitiesGenerator.getGeneratorStart()){
////                    System.out.println("TransCoordEntitiesGenerator starting");
////                    /**Получаем новые данные*/
////                    AbstractEntity abstractEntity = transCoordEntitiesGenerator.getEntity();
////                    logger.info(abstractEntity.toString());
////                    /**Пишем их в базу*/
////                    transCoordRepository.save((TransCoord) abstractEntity);
////                    /**Ждем 300сек или 5 мин*/
////                }
//
////                if(TransFuelEntityGenerator.getGeneratorStart()&&TransCoordEntitiesGenerator.getGeneratorStart()){
//                    try {
//                        TimeUnit.SECONDS.sleep(30L);
//                    } catch (InterruptedException ex) {
//                        logger.info("sleep aborting");
//                    }
////                }
//            }
//            else{
//                /**Раз в 2 сек проверяет не пришла ли команда запустить генерацию данных*/
//                try {
//                    TimeUnit.SECONDS.sleep(2L);
//                } catch (InterruptedException ex) {
//                    logger.info("sleep aborting");
//                }
//            }
//        }
//        System.exit(0);
    }
}
