package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransNetworkGenerator;
import ru.itm.gendata.entity.trans.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransNetworkService extends TransService{
    private static Logger logger = LoggerFactory.getLogger(TransNetworkService.class);
    private static TransNetworkRepository transNetworkRepository;
    private TransNetworkGenerator transNetworkGenerator;
    private TransNetwork transNetwork = null;

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
    public TransNetworkService(TransNetworkGenerator transNetworkGenerator, TransNetworkRepository transNetworkRepository) {
        this.transNetworkGenerator = transNetworkGenerator;
        this.transNetworkRepository = transNetworkRepository;
    }

    @Override
    public Boolean isStarting() {
        return transNetworkGenerator!=null && transNetworkGenerator.getGeneratorStart();
    }

    @Override
    public void start() {
        if(!transNetworkGenerator.getGeneratorStart()){
            transNetworkGenerator.setGeneratorStart(true);
        }
        else{
            logger.warn("Network data generation is already enabled.");
        }
    }

    @Override
    public void stop() {
        transNetworkGenerator.setGeneratorStart(false);
    }

    @Override
    public String getData() {
        if(transNetwork==null){
            return "0";
        }
        return String.valueOf(transNetwork.getLevel());
    }

    @Override
    public AbstractEntity generate() {
        try{
            transNetwork = (TransNetwork) transNetworkGenerator.getEntity();
            lastGeneration = LocalDateTime.now();
            return transNetwork;
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public void saveToBase(List<AbstractEntity> aE) {
        List<TransNetwork> transList = new ArrayList<>();
        aE.stream().forEach(a->transList.add((TransNetwork) a));
        lastSave = LocalDateTime.now();
        transNetworkRepository.saveAll(transList);
    }

    @Override
    public synchronized void saveOne(AbstractEntity abstractEntity) {
        transNetworkRepository.save((TransNetwork)abstractEntity);
    }
}
