package ru.itm.gendata.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransCoord;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Random;

@Component
public class TransCoordEntitiesGenerator implements AbstractEntitiesGenerator {

    /**Идет ли в данный момент генерация данных?*/
    private Boolean generatorStart = false;
    private float  latitude = 54.99587f;
    private float deltaLatitude = 0.00001f;
    private float  longitude = 82.88338f;
    private float deltaLongitude = 0.00001f;
    private float  azimuth = 0.0f;
    private float deltaAzimuth = 0.5f;
    private Random random = new Random();

    public TransCoordEntitiesGenerator() {}


    @Override
    public AbstractEntity getEntity() {
        if(SystemConfig.getEquipmentId() != 10000L) {  //10000L - это признак того, что база пока не считана и техника не определена
            latitude+=(deltaLatitude* random.nextInt(9));
            if(random.nextInt(9)>7){
                deltaLatitude*=-1;
            }

            longitude+=(deltaLongitude* random.nextInt(9));
            if(random.nextInt(9)>7){
                deltaLongitude*=-1;
            }

            if(random.nextInt(9)>6){
                azimuth+=deltaAzimuth;
            }
            if(random.nextInt(9)>7){
                deltaAzimuth*=-1;
            }
            if(azimuth >= Math.abs(180)){
                azimuth = 180.0f;
                deltaAzimuth*=-1;
            }

            return new TransCoord(
                    SystemConfig.getEquipmentId(),
                    Calendar.getInstance(),
                    Calendar.getInstance(),
                    BigDecimal.valueOf(1.0),
                    latitude,
                    longitude,
                    azimuth,
                    BigDecimal.valueOf(129.0 + (135.0 - 129.0) * random.nextDouble()),
                    BigDecimal.valueOf(0.15 * random.nextDouble()),
                    BigDecimal.valueOf(0.95 + (1.25 - 0.95) * random.nextDouble()),
                    BigDecimal.valueOf(0.95 + (1.25 - 0.95) * random.nextDouble()),
                    20+random.nextInt()%11,
                    1,
                    1L,
                    1L
                    );
        }
        return null;
    }

    public Double getCoord() {
        return Double.valueOf(latitude);
    }

    public void setGeneratorStart(Boolean generatorStart) {
        this.generatorStart = generatorStart;
    }

    public boolean getGeneratorStart() {
        return generatorStart;
    }

}
