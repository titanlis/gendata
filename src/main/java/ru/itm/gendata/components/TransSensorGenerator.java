package ru.itm.gendata.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.sensor.Sensor;
import ru.itm.gendata.entity.sensor.SensorRepository;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransSensor;

import java.util.Calendar;


@Component
public class TransSensorGenerator implements AbstractEntitiesGenerator{
    private static Logger logger = LoggerFactory.getLogger(TransSensorGenerator.class);

    /**Идет ли в данный момент генерация данных?*/
    private Boolean generatorStart = false;

    private boolean notInit = true;
    private Sensor sensor;
    private Double valueData;

    private SensorRepository sensorRepository;

    @Autowired
    public void setSensorRepository(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void TransSensorGeneratorEventListener() {
//        sensor = sensorRepository.findFirstByEquipIdAndSensorDataTypeId(SystemConfig.getEquipmentId(), 5L);
//        valueData = sensor.getMin_sens();
//    }

    @Override
    public AbstractEntity getEntity() {
        if(notInit){
            System.out.println("getEquipmentId() = " + SystemConfig.getEquipmentId());
            sensor = sensorRepository.findFirstByEquipIdAndSensorDataTypeId(SystemConfig.getEquipmentId(), 5L);
            valueData = sensor.getMin_sens();
            notInit=false;
        }

        if(SystemConfig.getEquipmentId() != 10000L) {  //10000L - это признак того, что база пока не считана и техника не определена
            valueData+=1;
            valueData%=90;
            return new TransSensor(
                    sensor.getId(),
                    SystemConfig.getEquipmentId(),
                    SystemConfig.getStartShiftLocalDate(),
                    1L,
                    Calendar.getInstance(),
                    valueData,
                    1L);
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
