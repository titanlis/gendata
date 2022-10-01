package ru.itm.gendata.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransKeysCycle;
import ru.itm.gendata.entity.trans.TransSensor;

import java.util.Calendar;
import java.util.Random;

@Component
public class TransKeysCycleGenerator implements AbstractEntitiesGenerator{
    private static Logger logger = LoggerFactory.getLogger(TransKeysCycleGenerator.class);

    /**Идет ли в данный момент генерация данных?*/
    private Boolean generatorStart = false;
    Long[] idTransCycle ={1L, 11L, 17L, 19L, 20L, 21L, 22L, 25L, 28L, 30L};
    Long[] idTransSensor ={20082L, 35166L, 35200L, 35201L, 35489L, 35490L, 35491L, 35492L, 35493L, 35494L};
    private Random random = new Random();

    @Override
    public AbstractEntity getEntity() {
        if(SystemConfig.getEquipmentId() != 10000L) {  //10000L - это признак того, что база пока не считана и техника не определена
            return new TransKeysCycle(
                    idTransCycle[random.nextInt(9)],
                    idTransSensor[random.nextInt(9)],
                    random.nextLong(9)+1
            );
        }
        return null;
    }

    public void setGeneratorStart(Boolean generatorStart) {
        this.generatorStart = generatorStart;
    }

    public boolean getGeneratorStart() {
        return generatorStart;
    }
}
