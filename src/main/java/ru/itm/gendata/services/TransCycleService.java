package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransCycleGenerator;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransCycle;
import ru.itm.gendata.entity.trans.TransCycleRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class TransCycleService extends TransService{
    private static Logger logger = LoggerFactory.getLogger(TransCycleService.class);
    private TransCycle transCycle = null;
    private TransCycleGenerator transCycleGenerator;
    private static TransCycleRepository transCycleRepository;
    private LocalDateTime lastGeneration = LocalDateTime.now();
    private LocalDateTime lastSave = LocalDateTime.now();

    @Autowired
    public TransCycleService(TransCycleGenerator transCycleGenerator, TransCycleRepository transCycleRepository) {
        this.transCycleGenerator = transCycleGenerator;
        this.transCycleRepository = transCycleRepository;
    }

    @Override
    public LocalDateTime getLastGeneration() {
        return lastGeneration;
    }
    @Override
    public LocalDateTime getLastSave() {
        return lastSave;
    }

    @Override
    public Boolean isStarting() {
        return transCycleGenerator!=null && transCycleGenerator.getGeneratorStart();
    }

    @Override
    public void start() {
        if(!transCycleGenerator.getGeneratorStart()){
            transCycleGenerator.setGeneratorStart(true);
        }
        else{
            logger.warn("Cycles data generation is already enabled.");
        }
    }

    @Override
    public void stop() {
        logger.info("TransCycleService stopping");
        //transCycle = (TransCycle) transCycleGenerator.getEntity();
        List<TransCycle> oldTransCycle = transCycleRepository.findAllByEndTimeIsNull();
        List<TransCycle> newTransCycle = new LinkedList<>();
        oldTransCycle.stream().forEach(tC -> {
            Instant endTime = Instant.now();//transCycle.getStartTime().minusSeconds(1L);
            if(tC.getLoadTime().isAfter(endTime)){  //если новый цикл пустили еще до погрузки
                endTime=endTime.plusSeconds(300L); //старый завершаем через 5 минут после погрузки (ошибка, если все вместе смотреть)
            } else if (endTime.isAfter(tC.getLoadTime().plusSeconds(900L))) {
                endTime=tC.getLoadTime().plusSeconds(300L); //если после загрузки прошло больше 15 мин, цикл завершаем через 5 мин после загрузки
            }
            tC.setEndTime(endTime);  //завершаем предыдущие циклы секундой раньше нового, если уложились в 15 мин + время до погрузки
            newTransCycle.add(tC);
        });
        transCycleRepository.saveAll(newTransCycle);
        lastSave = LocalDateTime.now();

        transCycleGenerator.setGeneratorStart(false);
    }

    @Override
    public String getData() {
        if(transCycle ==null){
            return "0";
        }
        return String.valueOf(getHaulId());
    }

    public Short getHaulId() {
        if(transCycle!=null){
            return transCycle.getHaulId();
        }
        return null;
    }


    @Override
    public AbstractEntity generate() {
        try{
            transCycle = (TransCycle) transCycleGenerator.getEntity();
            lastGeneration = LocalDateTime.now();
            return transCycle;
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public void saveToBase(List<AbstractEntity> aE) {
        //List<TransCycle> transCyclesList = new ArrayList<>();
        aE.stream().forEach(a-> saveOne(a));
        lastSave = LocalDateTime.now();
        //transCycleRepository.saveAll(transCyclesList);
    }

    @Override
    public synchronized void saveOne(AbstractEntity abstractEntity) {
        transCycle = (TransCycle)abstractEntity;
        List<TransCycle> oldTransCycle = transCycleRepository.findAllByEndTimeIsNull();
        List<TransCycle> newTransCycle = new LinkedList<>();
        oldTransCycle.stream().forEach(tC -> {
            Instant endTime = transCycle.getStartTime().minusSeconds(1L);
            if(tC.getLoadTime().isAfter(endTime)){  //если новый цикл пустили еще до погрузки
                endTime=endTime.plusSeconds(300L); //старый завершаем через 5 минут после погрузки (ошибка, если все вместе смотреть)
            } else if (endTime.isAfter(tC.getLoadTime().plusSeconds(900L))) {
                endTime=tC.getLoadTime().plusSeconds(300L); //если после загрузки прошло больше 15 мин, цикл завершаем через 5 мин после загрузки
            }
            tC.setEndTime(endTime);  //завершаем предыдущие циклы секундой раньше нового, если уложились в 15 мин + время до погрузки
            newTransCycle.add(tC);
        });
        transCycleRepository.saveAll(newTransCycle);
        transCycleRepository.save(transCycle);
        lastGeneration = LocalDateTime.now();
        lastSave = LocalDateTime.now();
    }

    public void setLastGeneration(LocalDateTime lastGeneration) {
        this.lastGeneration = lastGeneration;
    }
    public void setLastSave(LocalDateTime lastSave) {
        this.lastSave = lastSave;
    }

}
