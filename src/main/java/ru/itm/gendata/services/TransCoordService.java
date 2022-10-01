package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransCoordEntitiesGenerator;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TransCoordService implements TransService{
    private static Logger logger = LoggerFactory.getLogger(TransCoordService.class);
    private LocalDateTime lastGeneration = LocalDateTime.now();
    private static LocalDateTime lastSave = LocalDateTime.now();
    private TransCoord transCoord = null;
    private TransCoordEntitiesGenerator transCoordEntitiesGenerator;
    private static TransCoordRepository transCoordRepository;

    @Autowired
    public void setTransCoordEntitiesGenerator(TransCoordEntitiesGenerator transCoordEntitiesGenerator) {
        this.transCoordEntitiesGenerator = transCoordEntitiesGenerator;
    }

    @Autowired
    public void setTransCoordRepository(TransCoordRepository transCoordRepository) {
        this.transCoordRepository = transCoordRepository;
    }

    public LocalDateTime getLastGeneration() {
        return lastGeneration;
    }

    public void setLastGeneration(LocalDateTime lastGeneration) {
        this.lastGeneration = lastGeneration;
    }

    public static LocalDateTime getLastSave() {
        return lastSave;
    }

    public static void setLastSave(LocalDateTime lastSave) {
        TransCoordService.lastSave = lastSave;
    }

    @Override
    public void pause(long l) {
        for(int i = 0; i<l && !SystemConfig.isNeedStop() && transCoordEntitiesGenerator.getGeneratorStart(); i++){
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException ex) {
                logger.info("sleep aborting");
                break;
            }
        }
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

//    public void saveToBase(AbstractEntity a) {
//        transCoordRepository.save((TransCoord)a);
//    }

    @Override
    public String getData() {
        if(transCoord==null){
            return "0.0";
        }
        return String.valueOf(getCoordLevel());
    }

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

    public static synchronized void saveToBase(List<TransCoord> tC)
    {
        lastSave = LocalDateTime.now();
        transCoordRepository.saveAll(tC);
    }


    public Float getCoordLevel() {
        if(transCoord!=null){
            return transCoord.getLatitude();
        }
        return null;
    }

}
