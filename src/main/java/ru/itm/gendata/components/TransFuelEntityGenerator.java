package ru.itm.gendata.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    private static Boolean generatorStart = false;

    private static Equipment equipment = null;  //оборудование текущее
    private static Long equipId = 10000L;       //id оборудования, если база недоступна
    private static Calendar shiftDate;          //дата/время начала смены
    private static Long shiftId = 2L;           //id смены
    private static Calendar timeRead;           //дата/время генерации данных
    private static Double fuelLevel = 800.0;    //текущий уровень топлива def=800
    private static Long transCycles_id = 1L;    //рейс

    private static String sn = "none";          //серийный номер техники
    private static String equipType = "none";   //тип техники из description в equipment

    private static Double fuelFull = 800.0;     //размер бака по умолчанию
    private static Double fuelConsumptionPerHour = 100.0;   //расход по умолчанию
    private static EquipmentRepository equipmentRepository; //репозиторий для чтения данных об оборудовании
    private static SerialNumberRepository serialNumberRepository;   //по серийному номеру определяем,что за техника у нас
    private static EquipmentTypeRepository equipmentTypeRepository; //типы техники

    public static String getNameEq() {
        return (equipment.getEquip()!=null)?equipment.getEquip():"none";

    }

    @Autowired
    public void setEquipmentTypeRepository(EquipmentTypeRepository equipmentTypeRepository) {
        TransFuelEntityGenerator.equipmentTypeRepository = equipmentTypeRepository;
    }

    @Autowired
    public void setEquipmentRepository(EquipmentRepository equipmentRepository) {
        TransFuelEntityGenerator.equipmentRepository = equipmentRepository;
    }

    @Autowired
    public void setSerialNumberRepository(SerialNumberRepository serialNumberRepository) {
        TransFuelEntityGenerator.serialNumberRepository = serialNumberRepository;
    }

    public TransFuelEntityGenerator() {}

    /**
     * Генерируем новую запись с данными о топливе для текущего бк
     * @return
     */
    @Override
    public AbstractEntity getEntity() {
        /**При первом запуске находим id техники в базе h2 по серийнику.
         * Желательно. чтобы база уже получила первое обновление*/
        if(equipId == 10000L){  //10000L - это признак того, что база пока не считана и техника не определена
            List<SerialNumber> optSerial = serialNumberRepository.findAll();    //здесь может быть только 1 запись
            if(!optSerial.isEmpty()){
                try{
                    sn = optSerial.get(0).getSn();                          //нашли серийник в базе
                    equipment = equipmentRepository.findFirstByMtSn(sn);    //нашли оборудование
                    equipId = equipment.getId();                            //в нем уже id
                    Double fuelFullTmp = equipment.getFuel();               //объем бака
                    if(fuelFullTmp!=null){                                  //если null - то оставим default
                        fuelFull = fuelFullTmp;
                    }
                    /**Тип оборудования возьмем из базы*/
                    EquipmentType equipmentType = equipmentTypeRepository.findByIdIs(equipment.getEquip_type_id());
                    equipType = equipmentType.getDescr(); //тип оборудования описание
                    /**По типу оборудования (id) определим расход топлива*/
                    fuelConsumptionPerHour = getConsumptionPerHourByType(equipment.getEquip_type_id());
                }
                catch (Exception e){
                    logger.warn("Equipment not found in H2 base.");
                }

            }
        }
        /**Обновим данные по топливу в баке*/
        getFuelLevelCalculation();
        shiftDate = getStartShift();    //обновим данные по началу смены, если вдруг прошли отметку 9:00:00
        /**Генерируем новую запись для базы*/
        return new TransFuel(equipId, getStartShift(), shiftId, Calendar.getInstance(), fuelLevel, fuelLevel, transCycles_id);
    }

    /**
     * По id техники определяем (условно) расход топлива в час
     * @param t id типа техники из H2
     * @return aDouble расход топлива в час
     */
    private Double getConsumptionPerHourByType(Long t) {
        double aDouble = 1.0;
        if (t == 1L) {
            aDouble = 30.0;    //самосвал
        } else if (t == 2L) {
            aDouble = 100.0;   //Экскаватор
        } else if (t == 3L) {
            aDouble = 30.0;   //Топливозаправщик
        } else if (t == 4L) {
            aDouble = 100.0;   //Буровая
        } else if (t == 5L) {
            aDouble = 50.0;   //Бульдозер
        } else if (t == 6L) {
            aDouble = 30.0;   //Вспомогательная техника
        } else if (t == 7L) {
            aDouble = 10.0;   //Работники
        } else if (t == 8L) {
            aDouble = 30.0;   //Погрузчик
        } else if (t == 9L) {
            aDouble = 60.0;   //Грейдер
        } else {
            aDouble = 1.0;
        };
        return aDouble;
    }


    public static Double getFuelLevel() {
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
        double dF = fuelFull-(dH*fuelConsumptionPerHour + timeRead.get(Calendar.MINUTE)*(fuelConsumptionPerHour/60.0)
                + timeRead.get(Calendar.SECOND)*(fuelConsumptionPerHour/3600.0))%fuelFull ;
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

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        TransFuelEntityGenerator.logger = logger;
    }

    public static Long getEquipId() {
        return equipId;
    }

    public static void setEquipId(Long equipId) {
        TransFuelEntityGenerator.equipId = equipId;
    }

    public static Calendar getShiftDate() {
        return shiftDate;
    }

    public static void setShiftDate(Calendar shiftDate) {
        TransFuelEntityGenerator.shiftDate = shiftDate;
    }

    public static Long getShiftId() {
        return shiftId;
    }

    public static void setShiftId(Long shiftId) {
        TransFuelEntityGenerator.shiftId = shiftId;
    }

    public static Calendar getTimeRead() {
        return timeRead;
    }

    public static void setTimeRead(Calendar timeRead) {
        TransFuelEntityGenerator.timeRead = timeRead;
    }

    public static void setFuelLevel(Double fuelLevel) {
        TransFuelEntityGenerator.fuelLevel = fuelLevel;
    }

    public static Long getTransCycles_id() {
        return transCycles_id;
    }

    public static void setTransCycles_id(Long transCycles_id) {
        TransFuelEntityGenerator.transCycles_id = transCycles_id;
    }

    public static String getSn() {
        return sn;
    }

    public static void setSn(String sn) {
        TransFuelEntityGenerator.sn = sn;
    }

    public static Double getFuelFull() {
        return fuelFull;
    }

    public static void setFuelFull(Double fuelFull) {
        TransFuelEntityGenerator.fuelFull = fuelFull;
    }

    public static EquipmentRepository getEquipmentRepository() {
        return equipmentRepository;
    }

    public static SerialNumberRepository getSerialNumberRepository() {
        return serialNumberRepository;
    }

    public static Equipment getEquipment() {
        return equipment;
    }

    public static void setEquipment(Equipment equipment) {
        TransFuelEntityGenerator.equipment = equipment;
    }

    public static String getEquipType() {
        return equipType;
    }

    public static void setEquipType(String equipType) {
        TransFuelEntityGenerator.equipType = equipType;
    }

    public static Double getFuelConsumptionPerHour() {
        return fuelConsumptionPerHour;
    }

    public static void setFuelConsumptionPerHour(Double fuelConsumptionPerHour) {
        TransFuelEntityGenerator.fuelConsumptionPerHour = fuelConsumptionPerHour;
    }

    public static EquipmentTypeRepository getEquipmentTypeRepository() {
        return equipmentTypeRepository;
    }

    public static Boolean getGeneratorStart() {
        return generatorStart;
    }

    public static void setGeneratorStart(Boolean generatorStart) {
        TransFuelEntityGenerator.generatorStart = generatorStart;
    }
}
