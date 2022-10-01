package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransKeysCycleRepository;
@Service
public class TransKeysCycleService  implements TransService{
    private static Logger logger = LoggerFactory.getLogger(TransKeysCycleService.class);
    private TransKeysCycleRepository transKeysCycleRepository;

    @Autowired
    public void setTransKeysCycleRepository(TransKeysCycleRepository transKeysCycleRepository) {
        this.transKeysCycleRepository = transKeysCycleRepository;
    }

    @Override
    public void pause(long l) {

    }

    @Override
    public Boolean isStarting() {
        return null;
    }

    @Override
    public void start() {

    }

//    @Override
//    public void saveToBase(AbstractEntity a) {
//
//    }

    @Override
    public void stop() {

    }

    @Override
    public String getData() {
        return null;
    }

}
