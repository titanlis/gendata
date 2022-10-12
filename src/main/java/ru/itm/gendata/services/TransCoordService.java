package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransCoordEntitiesGenerator;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TransCoordService extends TransService{
    private static Logger logger = LoggerFactory.getLogger(TransCoordService.class);
    private TransCoord transCoord = null;
    private TransCoordEntitiesGenerator transCoordEntitiesGenerator;
    private static TransCoordRepository transCoordRepository;

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
    public void setTransCoordEntitiesGenerator(TransCoordEntitiesGenerator transCoordEntitiesGenerator) {
        this.transCoordEntitiesGenerator = transCoordEntitiesGenerator;
    }

    @Autowired
    public void setTransCoordRepository(TransCoordRepository transCoordRepository) {
        this.transCoordRepository = transCoordRepository;
    }


    @Override
    public Boolean isStarting(){
        return transCoordEntitiesGenerator!=null && transCoordEntitiesGenerator.getGeneratorStart();
    }

    @Override
    public void start() {
        if(!transCoordEntitiesGenerator.getGeneratorStart()){
            transCoordEntitiesGenerator.setGeneratorStart(true);
        }
        else{
            logger.warn("Coord data generation is already enabled.");
        }
    }
    @Override
    public void stop() {
        transCoordEntitiesGenerator.setGeneratorStart(false);
    }


    @Override
    public String getData() {
        if(transCoord==null){
            return "0.0";
        }
        return String.valueOf(getCoordLevel());
    }

    @Override
    public TransCoord generate(){
        try{
            transCoord = (TransCoord) transCoordEntitiesGenerator.getEntity();
            lastGeneration = LocalDateTime.now();
            return transCoord;
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public synchronized void saveToBase(List<AbstractEntity> aE)
    {
        List<TransCoord> transCoordList = new ArrayList<>();
        aE.stream().forEach(a->transCoordList.add((TransCoord) a));
        lastSave = LocalDateTime.now();
        transCoordRepository.saveAll(transCoordList);
    }

    @Override
    public synchronized void saveOne(AbstractEntity abstractEntity) {
        transCoordRepository.save((TransCoord)abstractEntity);
    }

    @Override
    public String getName() {
        return "TransCoordService";
    }


    public Float getCoordLevel() {
        if(transCoord!=null){
            return transCoord.getLatitude();
        }
        return null;
    }

}
