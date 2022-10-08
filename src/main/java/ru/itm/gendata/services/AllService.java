package ru.itm.gendata.services;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@Service
public class AllService{
    private static Logger logger = LoggerFactory.getLogger(AllService.class);
    private TransFuelService transFuelService;
    private TransCoordService transCoordService;
    private TransSensorService transSensorService;
    private TransKeysCycleService transKeysCycleService;
    private TransKeysDrillingService transKeysDrillingService;
    private TransNetworkService transNetworkService;
    private TransCycleService transCycleService;

    private Thread thread = null;

    @Autowired
    public AllService(TransFuelService transFuelService, TransCoordService transCoordService,
                      TransSensorService transSensorService, TransKeysCycleService transKeysCycleService,
                      TransKeysDrillingService transKeysDrillingService, TransNetworkService transNetworkService,
                      TransCycleService transCycleService) {
        this.transFuelService = transFuelService;
        this.transCoordService = transCoordService;
        this.transSensorService = transSensorService;
        this.transKeysCycleService = transKeysCycleService;
        this.transKeysDrillingService = transKeysDrillingService;
        this.transNetworkService = transNetworkService;
        this.transCycleService = transCycleService;
    }

    private Runnable task = () -> {
        logger.info("Thread Trans Generator Data is started.");
        LocalDateTime now;
        List<AbstractEntity> transFuelList = new LinkedList<>();
        List<AbstractEntity> transCoordList = new LinkedList<>();
        List<AbstractEntity> transSensorList = new LinkedList<>();
        List<AbstractEntity> transKeysCycleList = new LinkedList<>();
        List<AbstractEntity> transKeysDrillingList = new LinkedList<>();
        List<AbstractEntity> transNetworkList = new LinkedList<>();
        List<AbstractEntity> transCycleList = new LinkedList<>();

        while (!SystemConfig.isNeedStop()) {
            now = LocalDateTime.now();

            transGenerate("trans_fuel", 30L, 300L, now, transFuelList);
            transGenerate("trans_coord", 10L, 30L, now, transCoordList);
            transGenerate("trans_sensor", 20L, 60L, now, transSensorList);
            transGenerate("trans_keys_cycle", 45L, 120L, now, transKeysCycleList);
            transGenerate("trans_keys_drilling", 45L, 120L, now, transKeysDrillingList);
            transGenerate("trans_network", 30L, 120L, now, transNetworkList);
            transGenerate("trans_cycles", 900L, 900L, now, transCycleList);

            pause(1L);    //сек
        }
        //transCycleService.stop();
        logger.info("Thread Trans Generator Data is finished.");
    };

    private void transGenerate(String tableName, long fixTimeSeconds, long saveTimeSeconds, LocalDateTime now, List<AbstractEntity> abstractEntityList) {
        TransService transService = getService(tableName);

        if(transService!=null && transService.isStarting()){
            if(ChronoUnit.SECONDS.between(transService.getLastGeneration(),now)>fixTimeSeconds){
                AbstractEntity abstractEntity = transService.generate();
                abstractEntityList.add(abstractEntity);
                logger.info("New data: " + abstractEntity.toStringShow());
            }

            if(ChronoUnit.SECONDS.between(transService.getLastSave(), now)>saveTimeSeconds && abstractEntityList.size()>0){
                transService.saveToBase(abstractEntityList);
                logger.info("Saved " + abstractEntityList.size() + " records " + tableName);
                abstractEntityList.clear();
            }
        }

    }

    /**
     * Генерируем и пишем в базу одну запись по имени таблицы
     * @param tableName имя таблицы
     */
    public void transGenerateOneAndSave(String tableName){
        TransService transService = getService(tableName);
        if(transService!=null){
            if(transService.isStarting()){
                transService.stop();
            }
            AbstractEntity abstractEntity = transService.generate();
            logger.info("New data: " + abstractEntity.toStringShow());
            if(abstractEntity!=null){
                transService.saveOne(abstractEntity);
            }
        }
        else{
            logger.warn(tableName + " service not crated.");
        }
    }


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
    public TransKeysCycleService getTransKeysCycleService() {
        return transKeysCycleService;
    }
    public TransKeysDrillingService getTransKeysDrillingService() {
        return transKeysDrillingService;
    }
    public TransNetworkService getTransNetworkService() {
        return transNetworkService;
    }

    public void on(String tableName) {
        TransService transService = getService(tableName);
        start();
        if(!transService.isStarting()){
            transService.start();
            logger.info(tableName + " generation is start.");
        }
    }

    public void off(String tableName) {
        logger.info(tableName + " generation is stop.");
        getService(tableName).stop();
    }

    public TransService getService(String tableName){
        return switch(tableName){
            case "trans_fuel" -> transFuelService;
            case "trans_coord" -> transCoordService;
            case "trans_keys_cycle" -> transKeysCycleService;
            case "trans_keys_drilling" -> transKeysDrillingService;
            case "trans_sensor" -> transSensorService;
            case "trans_network" -> transNetworkService;
            case "trans_cycles" -> transCycleService;

            default -> null;
        };
    }

    public AbstractEntity generateOne(String tableName){
        TransService transService = getService(tableName);
        if(transService==null){
            return null;
        }
        return transService.generate();
    }

    public TransCycleService getTransCycleService() {
        return transCycleService;
    }
}
