package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransKeysCycleGenerator;
import ru.itm.gendata.entity.trans.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransKeysCycleService  implements TransService{
    private static Logger logger = LoggerFactory.getLogger(TransKeysCycleService.class);
    private static TransKeysCycleRepository transKeysCycleRepository;
    private TransKeysCycleGenerator transKeysCycleGenerator;

    private TransKeysCycle transKeysCycle = null;
    private LocalDateTime lastGeneration = LocalDateTime.now();
    private static LocalDateTime lastSave = LocalDateTime.now();


    @Autowired
    public TransKeysCycleService(TransKeysCycleRepository transKeysCycleRepository, TransKeysCycleGenerator transKeysCycleGenerator) {
        this.transKeysCycleRepository = transKeysCycleRepository;
        this.transKeysCycleGenerator = transKeysCycleGenerator;
    }

    public LocalDateTime getLastGeneration() {
        return lastGeneration;
    }
    public static LocalDateTime getLastSave() {
        return lastSave;
    }

    @Override
    public Boolean isStarting() {
        return transKeysCycleGenerator!=null && transKeysCycleGenerator.getGeneratorStart();
    }

    @Override
    public void start() {
        if(!transKeysCycleGenerator.getGeneratorStart()){
            transKeysCycleGenerator.setGeneratorStart(true);
        }
        else{
            logger.warn("KeysCycle data generation is already enabled.");
        }
    }

    @Override
    public void stop() {
        transKeysCycleGenerator.setGeneratorStart(false);
    }

    @Override
    public String getData() {
        if(transKeysCycle==null){
            return "0 - none";
        }
        return String.valueOf(transKeysCycle.getIdTransCycle())
                + " - " + transKeysCycleGenerator.getDataTypeSensor(transKeysCycle.getSensorDataTypeId());
    }

    public TransKeysCycle generate(){
        try{
            transKeysCycle = (TransKeysCycle) transKeysCycleGenerator.getEntity();
            lastGeneration = LocalDateTime.now();
            return transKeysCycle;
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return null;
        }
    }

    public static synchronized void saveToBase(List<TransKeysCycle> tKC)
    {
        lastSave = LocalDateTime.now();
        transKeysCycleRepository.saveAll(tKC);
    }

}
