package ru.itm.gendata.services;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransSensorGenerator;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class TransSensorService implements TransService {
    private static Logger logger = LoggerFactory.getLogger(TransSensorService.class);
    private LocalDateTime lastGeneration = LocalDateTime.now();
    private static LocalDateTime lastSave = LocalDateTime.now();
    private TransSensorGenerator transSensorGenerator;
    private static TransSensorRepository transSensorRepository;
    private TransSensor transSensor = null;

    @Autowired
    public void setTransSensorRepository(TransSensorRepository transSensorRepository) {
        this.transSensorRepository = transSensorRepository;
    }

    @Autowired
    public void setTransSensorGenerator(TransSensorGenerator transSensorGenerator) {
        this.transSensorGenerator = transSensorGenerator;
    }

    public LocalDateTime getLastGeneration() {
        return lastGeneration;
    }

    public static LocalDateTime getLastSave() {
        return lastSave;
    }

    @Override
    public void pause(long l) {
        for(int i = 0; i<l && !SystemConfig.isNeedStop() && transSensorGenerator.getGeneratorStart(); i++){
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException ex) {
                logger.info("sleep aborting");
                break;
            }
        }
    }

    @Override
    public Boolean isStarting() {
        return transSensorGenerator!=null && transSensorGenerator.getGeneratorStart();

    }

    @Override
    public void start() {
        if(!transSensorGenerator.getGeneratorStart()){
            transSensorGenerator.setGeneratorStart(true);
        }
        else{
            logger.warn("Sensors data generation is already enabled.");
        }
    }

    @Override
    public void stop() {
        transSensorGenerator.setGeneratorStart(false);
    }

//    @Override
//    public void saveToBase(AbstractEntity a) {
//
//    }

    public TransSensor generate(){
        try{
            transSensor = (TransSensor) transSensorGenerator.getEntity();
            lastGeneration = LocalDateTime.now();
            return transSensor;
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return null;
        }
    }

    public static synchronized void saveToBase(List<TransSensor> tS)
    {
        lastSave = LocalDateTime.now();
        transSensorRepository.saveAll(tS);
    }

    @Override
    public String getData() {
        if(transSensor==null){
            return "0.0";
        }
        return String.valueOf(transSensor.getValueData());
    }

    public TransSensorGenerator getTransSensorGenerator() {
        return transSensorGenerator;
    }
}