package ru.itm.gendata.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.SerialNumber;
import ru.itm.gendata.entity.SerialNumberRepository;
import ru.itm.gendata.entity.equipment.Equipment;
import ru.itm.gendata.entity.equipment.EquipmentRepository;
import ru.itm.gendata.entity.equipment.EquipmentType;
import ru.itm.gendata.entity.equipment.EquipmentTypeRepository;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransFuel;

import java.util.Calendar;
import java.util.List;

/**
 * Генератор данных для trans_fuel
 */
@Component
public class TransFuelEntityGenerator implements AbstractEntitiesGenerator{
    private static Logger logger = LoggerFactory.getLogger(TransFuelEntityGenerator.class);

    /**Идет ли в данный момент генерация данных?*/
    private Boolean generatorStart = false;
    private Calendar shiftDate = Calendar.getInstance();          //дата/время начала смены
    private Long shiftId = 2L;           //id смены
    private Calendar timeRead = Calendar.getInstance();           //дата/время генерации данных
    private Double fuelLevel = 800.0;    //текущий уровень топлива def=800
    private Long transCycles_id = 1L;    //рейс

    public TransFuelEntityGenerator() {
        if(SystemConfig.getFuelFull()!=null){
            fuelLevel = SystemConfig.getFuelFull();
        }
    }

    /**
     * Генерируем новую запись с данными о топливе для текущего бк
     * @return
     */
    @Override
    public AbstractEntity getEntity() {
        /**При первом запуске находим id техники в базе h2 по серийнику.
         * Желательно. чтобы база уже получила первое обновление*/
        if(SystemConfig.getSn() == null) {
            logger.warn("Equipment not found in H2 base.");
            return null;
        }
        /**Обновим данные по топливу в баке*/
        getFuelLevelCalculation();
        shiftDate = getStartShift();    //обновим данные по началу смены, если вдруг прошли отметку 9:00:00
        /**Генерируем новую запись для базы*/
        return new TransFuel(SystemConfig.getEquipmentId(), getStartShift(), shiftId, Calendar.getInstance(), fuelLevel, fuelLevel, transCycles_id);
    }


    public Double getFuelLevel() {
        return fuelLevel;
    }

    /**
     * Уровень топлива.
     * Считаем, что в начале смены его было 800л (fuelFull). Расход 10л в час.
     *
     * @return
     */
    private Double getFuelLevelCalculation() {
        timeRead = Calendar.getInstance();  //текущее время
        int dH = timeRead.get(Calendar.HOUR_OF_DAY)-getStartShift().get(Calendar.HOUR_OF_DAY); //часов прошло с начала смены
        /**Считаем на основе расхода, сколько топлива потрачено с начала смены. При достижении нуля, идет заправка до полного бака.*/
        double dF = SystemConfig.getFuelFull()-(dH*SystemConfig.getFuelConsumptionPerHour()
                + timeRead.get(Calendar.MINUTE)*(SystemConfig.getFuelConsumptionPerHour()/60.0)
                + timeRead.get(Calendar.SECOND)*(SystemConfig.getFuelConsumptionPerHour()/3600.0))%SystemConfig.getFuelFull();
        fuelLevel = (double)((int)dF); //округляем до целого литра
        return fuelLevel;
    }

    /**
     * Возвращает время начала смены. 9:00
     * Если сейчас, например 8 часов, то значит смена началась в 9:00, но вчера.
     * Если сейчас 14:00, то значит смена началась сегодня в 9:00
     * @return начало смены
     */
    private Calendar getStartShift() {
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY)<9){
            calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-1);
        }
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar;
    }

    public Calendar getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(Calendar shiftDate) {
        this.shiftDate = shiftDate;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
    }

    public Calendar getTimeRead() {
        return timeRead;
    }

    public void setTimeRead(Calendar timeRead) {
        this.timeRead = timeRead;
    }

    public void setFuelLevel(Double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public Long getTransCycles_id() {
        return transCycles_id;
    }

    public void setTransCycles_id(Long transCycles_id) {
        this.transCycles_id = transCycles_id;
    }

    public Boolean getGeneratorStart() {
        return generatorStart;
    }

    public void setGeneratorStart(Boolean generatorStart) {
        this.generatorStart = generatorStart;
    }

}
