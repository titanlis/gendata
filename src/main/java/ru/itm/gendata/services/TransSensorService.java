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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class TransSensorService extends TransService {
    private static Logger logger = LoggerFactory.getLogger(TransSensorService.class);
    private TransSensorGenerator transSensorGenerator;
    private static TransSensorRepository transSensorRepository;
    private TransSensor transSensor = null;

    private LocalDateTime lastGeneration = LocalDateTime.now();
    private LocalDateTime lastSave = LocalDateTime.now();
    @Override
    public LocalDateTime getLastGeneration() {
        return lastGeneration;
    }
    @Override
    public LocalDateTime getLastSave() {
        return lastSave;
    }
    public void setLastGeneration(LocalDateTime lastGeneration) {
        this.lastGeneration = lastGeneration;
    }
    public void setLastSave(LocalDateTime lastSave) {
        this.lastSave = lastSave;
    }


    @Autowired
    public void setTransSensorRepository(TransSensorRepository transSensorRepository) {
        this.transSensorRepository = transSensorRepository;
    }

    @Autowired
    public void setTransSensorGenerator(TransSensorGenerator transSensorGenerator) {
        this.transSensorGenerator = transSensorGenerator;
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


    @Override
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

    @Override
    public synchronized void saveToBase(List<AbstractEntity> aE)
    {
        List<TransSensor> transList = new ArrayList<>();
        aE.stream().forEach(a->transList.add((TransSensor) a));
        lastSave = LocalDateTime.now();
        transSensorRepository.saveAll(transList);
    }

    @Override
    public synchronized void saveOne(AbstractEntity abstractEntity) {
        transSensorRepository.save((TransSensor)abstractEntity);
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