package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.TransCoord;
import ru.itm.gendata.entity.trans.TransFuel;
import ru.itm.gendata.entity.trans.TransSensor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AllService{
    private static Logger logger = LoggerFactory.getLogger(AllService.class);
    private TransFuelService transFuelService;
    private TransCoordService transCoordService;
    private TransSensorService transSensorService;
    private Thread thread = null;

    @Autowired
    public AllService(TransFuelService transFuelService, TransCoordService transCoordService, TransSensorService transSensorService) {
        this.transFuelService = transFuelService;
        this.transCoordService = transCoordService;
        this.transSensorService = transSensorService;
    }

    private Runnable task = () -> {
        logger.info("Thread Trans Generator Data is started.");
        LocalDateTime now;
        List<TransFuel> transFuelList = new LinkedList<>();
        List<TransCoord> transCoordList = new LinkedList<>();
        List<TransSensor> transSensorList = new LinkedList<>();

        while (!SystemConfig.isNeedStop()) {
            now = LocalDateTime.now();

            if(transFuelService!=null && transFuelService.isStarting()){
                if(ChronoUnit.SECONDS.between(transFuelService.getLastGeneration(),now)>60L){  //раз в минуту фиксируем значение
                    TransFuel transFuel = transFuelService.generate();
                    transFuelList.add(transFuel);
                    logger.info("New data: " + transFuel.toStringShow());
                }

                if(ChronoUnit.SECONDS.between(transFuelService.getLastSave(), now)>300L && transFuelList.size()>0){       //раз в 5 минут пишем в базу
                    transFuelService.saveToBase(transFuelList);
                    logger.info("Saved " + transFuelList.size() + " records TransFuel");
                    transFuelList.clear();
                }
            }

            if(transCoordService!=null && transCoordService.isStarting()){
                if(ChronoUnit.SECONDS.between(transCoordService.getLastGeneration(),now)>10L){  //раз в 10 сек фиксируем значение
                    TransCoord transCoord = transCoordService.generate();
                    transCoordList.add(transCoord);
                    logger.info("New data: " + transCoord.toStringShow());
                }

                if(ChronoUnit.SECONDS.between(transCoordService.getLastSave(), now)>30L && transFuelList.size()>0){       //раз в 30 сек пишем в базу
                    transCoordService.saveToBase(transCoordList);
                    logger.info("Saved " + transCoordList.size() + " records TransCoord");
                    transCoordList.clear();
                }
            }

            if(transSensorService!=null && transSensorService.isStarting()) {
                if(ChronoUnit.SECONDS.between(transSensorService.getLastGeneration(),now)>20L){  //раз в 20 сек фиксируем значение
                    TransSensor transSensor = transSensorService.generate();
                    transSensorList.add(transSensor);
                    logger.info("New data: " + transSensor.toStringShow());
                }

                if(ChronoUnit.SECONDS.between(transSensorService.getLastSave(), now)>60L && transSensorList.size()>0){       //раз в 60 сек пишем в базу
                    transSensorService.saveToBase(transSensorList);
                    logger.info("Saved " + transSensorList.size() + " records TransSensor");
                    transSensorList.clear();
                }
            }
            pause(1L);    //сек
        }
        logger.info("Thread Trans Generator Data is finished.");
    };

    public void start(){
        if(thread==null || !thread.isAlive()){
            thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void stop(){
        transFuelService.stop();
        transCoordService.stop();
        transSensorService.stop();
        SystemConfig.setNeedStop(true);
    }

    public void pause(long l) {
        for(int i=0; i<l && !SystemConfig.isNeedStop(); i++){
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException ex) {
                logger.info("sleep aborting");
                break;
            }
        }
    }


    public TransFuelService getTransFuelService() {
        return transFuelService;
    }

    public TransCoordService getTransCoordService() {
        return transCoordService;
    }

    public TransSensorService getTransSensorService() {
        return transSensorService;
    }

    public void fuelOnOF(boolean start){

        if(start){
            start();    //старт потока
            if(!transFuelService.isStarting()){
                transFuelService.start();
                logger.info("TransFuel generation is start.");
            }
        }
        else{
            logger.info("TransFuel generation is stop.");
            transFuelService.stop();
        }
    }

    public void coordOnOF(boolean start) {
        if(start){
            start();    //старт потока
            if(!transCoordService.isStarting()){
                transCoordService.start();
                logger.info("TransCoord generation is start.");
            }
        }
        else{
            logger.info("TransCoord generation is stop.");
            transCoordService.stop();
        }
    }

    public void sensorOnOF(boolean start) {
        if(start){
            start();    //старт потока
            if(!transSensorService.isStarting()){
                transSensorService.start();
                logger.info("TransSensor generation is start.");
            }
        }
        else{
            logger.info("TransSensor generation is stop.");
            transSensorService.stop();
        }
    }

    public TransFuel generateOneTransFuel(){
        return transFuelService.generate();
    }

    public TransCoord generateOneTransCoord() {
        return transCoordService.generate();
    }

    public TransSensor generateOneTransSensor() {
        return transSensorService.generate();
    }
}
