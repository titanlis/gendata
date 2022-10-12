package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransCycleGenerator;
import ru.itm.gendata.components.TransRefuelGenerator;
import ru.itm.gendata.entity.trans.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class TransRefuelService extends TransService{
    private static Logger logger = LoggerFactory.getLogger(TransRefuelService.class);

    private TransRefuel transRefuel = null;
    private TransRefuelGenerator transRefuelGenerator;
    private static TransRefuelRepository transRefuelRepository;
    private LocalDateTime lastGeneration = LocalDateTime.now();
    private LocalDateTime lastSave = LocalDateTime.now();

    @Autowired
    public TransRefuelService(TransRefuelGenerator transRefuelGenerator, TransRefuelRepository transRefuelRepository) {
        this.transRefuelGenerator = transRefuelGenerator;
        this.transRefuelRepository = transRefuelRepository;
    }

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



    @Override
    public Boolean isStarting() {
        return transRefuelGenerator!=null && transRefuelGenerator.getGeneratorStart();
    }

    @Override
    public void start() {
        if(!transRefuelGenerator.getGeneratorStart()){
            transRefuelGenerator.setGeneratorStart(true);
        }
        else{
            logger.warn("Refuel data generation is already enabled.");
        }
    }

    @Override
    public void stop() {
        logger.info("TransRefuelService stopping");
        List<TransRefuel> oldTransRefuel = transRefuelRepository.findAllByEndDtIsNull();
        List<TransRefuel> newTransRefuel = new LinkedList<>();
        oldTransRefuel.stream().forEach(transRefuel -> {
            transRefuel.setEndDt(Instant.now().minusSeconds(1L));
            /*если заправка получилась >10 мин, то ставим 10 мин*/
            if(transRefuel.getEndDt().isAfter(transRefuel.getStartDt().plusSeconds(600L))){
                transRefuel.setEndDt(transRefuel.getStartDt().plusSeconds(600L));
            }
            newTransRefuel.add(transRefuel);
        });
        transRefuelRepository.saveAll(newTransRefuel);
        lastSave = LocalDateTime.now();
        transRefuelGenerator.setGeneratorStart(false);
    }

    @Override
    public String getData() {
        if(transRefuel ==null){
            return "0";
        }
        return String.valueOf(transRefuel.getEquipId());    }

    @Override
    public AbstractEntity generate() {
        try{
            transRefuel = (TransRefuel) transRefuelGenerator.getEntity();
            lastGeneration = LocalDateTime.now();
            return transRefuel;
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public void saveToBase(List<AbstractEntity> aE) {
        aE.stream().forEach(a-> saveOne(a));
        lastSave = LocalDateTime.now();
    }

    @Override
    public void saveOne(AbstractEntity abstractEntity) {
        transRefuel = (TransRefuel)abstractEntity;
        List<TransRefuel> oldTransRefuel = transRefuelRepository.findAllByEndDtIsNull();
        List<TransRefuel> newTransRefuel = new LinkedList<>();
        oldTransRefuel.stream().forEach(tR -> {
            tR.setEndDt(transRefuel.getStartDt().minusSeconds(1L));
            newTransRefuel.add(tR);
        });
        newTransRefuel.add(transRefuel);
        transRefuelRepository.saveAll(newTransRefuel);
        //transRefuelRepository.save(transRefuel);
        lastGeneration = LocalDateTime.now();
        lastSave = LocalDateTime.now();
    }

    @Override
    public String getName() {
        return "TransRefuelService";
    }



}
