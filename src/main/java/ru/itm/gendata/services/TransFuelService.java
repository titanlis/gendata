package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.components.TransFuelEntityGenerator;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransFuel;
import ru.itm.gendata.entity.trans.TransFuelRepository;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Service
public class TransFuelService implements TransService{
    private static Logger logger = LoggerFactory.getLogger(TransFuelService.class);

    private TransFuel transFuel = null;
    private Thread thread = null;

    private TransFuelEntityGenerator transFuelEntityGenerator;
    private TransFuelRepository transFuelRepository;

    private Runnable task = () -> {
        logger.info("Thread TransFuel is started.");
        while (!SystemConfig.isNeedStop() && transFuelEntityGenerator.getGeneratorStart()) {
            transFuel = (TransFuel) transFuelEntityGenerator.getEntity();
            if(transFuel!=null){
                logger.info(transFuel.toString());
                /**Пишем их в базу*/
                saveToBase(transFuel);
                pause(300L);            //пишем раз в 5 минут
            }
            else{
                pause(2L);
            }
        }
        logger.info("Thread TransFuel is finished.");
    };


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


    @Autowired
    public void setTransFuelEntityGenerator(TransFuelEntityGenerator transFuelEntityGenerator) {
        this.transFuelEntityGenerator = transFuelEntityGenerator;
    }

    @Autowired
    public void setTransFuelRepository(TransFuelRepository transFuelRepository) {
        this.transFuelRepository = transFuelRepository;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public TransFuelRepository getTransFuelRepository() {
        return transFuelRepository;
    }

    public TransFuelEntityGenerator getTransFuelEntityGenerator() {
        return transFuelEntityGenerator;
    }

    @Override
    public Boolean isStarting(){
        return transFuelEntityGenerator!=null && transFuelEntityGenerator.getGeneratorStart();
    }

    @Override
    public void start(){
        if(!transFuelEntityGenerator.getGeneratorStart()){
            transFuelEntityGenerator.setGeneratorStart(true);

            if(thread==null || !thread.isAlive()){
                thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            }
        }
        else{
            logger.warn("Fuel data generation is already enabled.");
        }
    }


    /**
     * сделаем запись в базу потокобезопасной
     * @param transFuel
     */
    @Override
    public synchronized void saveToBase(AbstractEntity transFuel) {
        transFuelRepository.save((TransFuel)transFuel);
    }

    @Override
    public void stop(){
        transFuelEntityGenerator.setGeneratorStart(false);
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
