package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransKeysCycleGenerator;
import ru.itm.gendata.components.TransKeysDrillingGenerator;
import ru.itm.gendata.entity.trans.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransKeysDrillingService extends TransService{

    private static Logger logger = LoggerFactory.getLogger(TransKeysDrillingService.class);
    private static TransKeysDrillingRepository transKeysDrillingRepository;
    private TransKeysDrillingGenerator transKeysDrillingGenerator;
    private TransKeysDrilling transKeysDrilling = null;

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
    public TransKeysDrillingService(TransKeysDrillingGenerator transKeysDrillingGenerator, TransKeysDrillingRepository transKeysDrillingRepository) {
        this.transKeysDrillingGenerator = transKeysDrillingGenerator;
        this.transKeysDrillingRepository = transKeysDrillingRepository;
    }

    @Override
    public Boolean isStarting() {
        return transKeysDrillingGenerator!=null && transKeysDrillingGenerator.getGeneratorStart();
    }

    @Override
    public void start() {
        if(!transKeysDrillingGenerator.getGeneratorStart()){
            transKeysDrillingGenerator.setGeneratorStart(true);
        }
        else{
            logger.warn("KeysDrilling data generation is already enabled.");
        }
    }

    @Override
    public void stop() {
        transKeysDrillingGenerator.setGeneratorStart(false);
    }

    @Override
    public String getData() {
        if(transKeysDrilling==null){
            return "0";
        }
        return String.valueOf(transKeysDrilling.getIdHole());
    }

    @Override
    public TransKeysDrilling generate(){
        try{
            transKeysDrilling = (TransKeysDrilling) transKeysDrillingGenerator.getEntity();
            lastGeneration = LocalDateTime.now();
            return transKeysDrilling;
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return null;
        }
    }


    @Override
    public synchronized void saveToBase(List<AbstractEntity> aE)
    {
        List<TransKeysDrilling> transList = new ArrayList<>();
        aE.stream().forEach(a->transList.add((TransKeysDrilling) a));
        lastSave = LocalDateTime.now();
        transKeysDrillingRepository.saveAll(transList);
    }

    @Override
    public synchronized void saveOne(AbstractEntity abstractEntity) {
        transKeysDrillingRepository.save((TransKeysDrilling)abstractEntity);
    }

}
