package ru.itm.gendata.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransNetwork;

import java.util.Calendar;
import java.util.Random;

@Component
public class TransNetworkGenerator  implements AbstractEntitiesGenerator{
    private static Logger logger = LoggerFactory.getLogger(TransNetworkGenerator.class);
    /**Идет ли в данный момент генерация данных?*/
    private Boolean generatorStart = false;
    Long[] idTransCoord ={3583L, 3584L, 3585L, 3586L, 3587L, 3588L, 3589L, 3590L, 3591L, 3592L};

    private Random random = new Random();

    @Override
    public AbstractEntity getEntity() {
        if(SystemConfig.getEquipmentId() != 10000L) {  //10000L - это признак того, что база пока не считана и техника не определена
            return new TransNetwork(
                    SystemConfig.getEquipmentId(),
                    Calendar.getInstance(),
                    idTransCoord[random.nextInt(9)],
                    SystemConfig.getEquipment().getMt_mac(),
                    random.nextInt(255),
                    random.nextInt(255),
                    random.nextInt(255)
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
