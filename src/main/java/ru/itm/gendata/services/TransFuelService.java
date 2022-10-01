package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransFuelEntityGenerator;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransCoord;
import ru.itm.gendata.entity.trans.TransFuel;
import ru.itm.gendata.entity.trans.TransFuelRepository;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TransFuelService implements TransService{
    private static Logger logger = LoggerFactory.getLogger(TransFuelService.class);
    private TransFuel transFuel = null;
    private LocalDateTime lastGeneration = LocalDateTime.now();
    private static LocalDateTime lastSave = LocalDateTime.now();
    private TransFuelEntityGenerator transFuelEntityGenerator;
    private static TransFuelRepository transFuelRepository;

    @Autowired
    public void setTransFuelEntityGenerator(TransFuelEntityGenerator transFuelEntityGenerator) {
        this.transFuelEntityGenerator = transFuelEntityGenerator;
    }

    @Autowired
    public void setTransFuelRepository(TransFuelRepository transFuelRepository) {
        this.transFuelRepository = transFuelRepository;
    }


    /**
     * Пауза кратно секунде
     * @param l число секунд
     */
    @Override
    public void pause(long l) {
        for(int i=0; i<l && !SystemConfig.isNeedStop() && transFuelEntityGenerator.getGeneratorStart(); i++){
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
        return transFuelEntityGenerator!=null && transFuelEntityGenerator.getGeneratorStart();
    }

    @Override
    public void start(){
        if(!transFuelEntityGenerator.getGeneratorStart()){
            transFuelEntityGenerator.setGeneratorStart(true);
        }
        else{
            logger.warn("Fuel data generation is already enabled.");
        }
    }

    @Override
    public void stop(){
        transFuelEntityGenerator.setGeneratorStart(false);
    }

    /**
     * сделаем запись в базу потокобезопасной
     * @param transFuel
     */
//    @Override
//    public synchronized void saveToBase(AbstractEntity transFuel) {
//        transFuelRepository.save((TransFuel)transFuel);
//    }

    @Override
    public String getData() {
        if(transFuel==null){
            return "0.0";
        }
        return String.valueOf(getFuelLevel());
    }

    public LocalDateTime getLastGeneration() {
        return lastGeneration;
    }

    public static LocalDateTime getLastSave() {
        return lastSave;
    }

    public TransFuel generate(){
        try{
            transFuel = (TransFuel) transFuelEntityGenerator.getEntity();
            lastGeneration = LocalDateTime.now();
            return transFuel;
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return null;
        }
    }


    public static synchronized void saveToBase(List<TransFuel> tF)
    {
        lastSave = LocalDateTime.now();
        transFuelRepository.saveAll(tF);
    }


    public Calendar getShiftDate() {
        if(transFuelEntityGenerator!=null){
            return transFuelEntityGenerator.getShiftDate();
        }
        else{
            return null;
        }
    }

    public Calendar getTimeRead() {
        if(transFuelEntityGenerator!=null){
            return transFuelEntityGenerator.getTimeRead();
        }
        else{
            return null;
        }

    }

    public Double getFuelLevel() {
        if(transFuel!=null){
            return transFuel.getFuelLevel();
        }
        return null;
    }
}
