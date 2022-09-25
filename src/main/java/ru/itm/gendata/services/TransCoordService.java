package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransCoordEntitiesGenerator;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.*;

import java.util.concurrent.TimeUnit;

@Service
public class TransCoordService implements TransService{
    private static Logger logger = LoggerFactory.getLogger(TransCoordService.class);

    private TransCoord transCoord = null;
    private Thread thread = null;

    private TransCoordEntitiesGenerator transCoordEntitiesGenerator;
    private TransCoordRepository transCoordRepository;

    private Runnable task = () -> {
        logger.info("Thread TransCoord is started.");
        while (!SystemConfig.isNeedStop() && transCoordEntitiesGenerator.getGeneratorStart()) {
            transCoord = (TransCoord) transCoordEntitiesGenerator.getEntity();
            if(transCoord!=null){
                logger.info(transCoord.toString());
                /**Пишем их в базу*/
                saveToBase(transCoord);
                pause(20L);
            }
            else{
                pause(2L);
            }
        }
        logger.info("Thread TransCoord is finished.");
    };


    @Autowired
    public void setTransCoordEntitiesGenerator(TransCoordEntitiesGenerator transCoordEntitiesGenerator) {
        this.transCoordEntitiesGenerator = transCoordEntitiesGenerator;
    }

    @Autowired
    public void setTransCoordRepository(TransCoordRepository transCoordRepository) {
        this.transCoordRepository = transCoordRepository;
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

            if(thread==null || !thread.isAlive()){
                thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            }
        }
        else{
            logger.warn("Coord data generation is already enabled.");
        }
    }

    @Override
    public void saveToBase(AbstractEntity a) {
        transCoordRepository.save((TransCoord)transCoord);
    }

    @Override
    public void stop() {
        transCoordEntitiesGenerator.setGeneratorStart(false);
    }

    public Float getCoordLevel() {
        if(transCoord!=null){
            return transCoord.getLatitude();
        }
        return null;
    }
}
