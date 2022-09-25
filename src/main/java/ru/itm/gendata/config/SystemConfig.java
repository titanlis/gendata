package ru.itm.gendata.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.itm.gendata.entity.equipment.Equipment;

import java.util.concurrent.TimeUnit;

@Component
public class SystemConfig {
    private static Logger logger = LoggerFactory.getLogger(SystemConfig.class);

    private static boolean needStop = false;    //не пора ли остановить сервис?
    private static Long equipmentId = null;         //id текущего оборудования
    private static String equipmentName = null;     //имя текущего оборудования
    private static String equipmentType = null;     //тип текущего оборудования
    private static String sn = null;                //серийник
    private static Equipment equipment = null;      //оборудование
    private static Double fuelFull = null;          //размер бака
    private static Double fuelConsumptionPerHour = null;   //расход

    public static Double getFuelConsumptionPerHour() {
        return fuelConsumptionPerHour;
    }

    public static void setFuelConsumptionPerHour(Double fuelConsumptionPerHour) {
        SystemConfig.fuelConsumptionPerHour = fuelConsumptionPerHour;
    }

    public static Long getEquipmentId() {
        return equipmentId;
    }


    public static void setEquipmentId(Long equipmentId) {
        SystemConfig.equipmentId = equipmentId;
    }

    public static String getEquipmentName() {
        return equipmentName;
    }

    public static void setEquipmentName(String equipmentName) {
        SystemConfig.equipmentName = equipmentName;
    }

    public static String getEquipmentType() {
        return equipmentType;
    }

    public static void setEquipmentType(String equipmentType) {
        SystemConfig.equipmentType = equipmentType;
    }

    public static String getSn() {
        return sn;
    }

    public static void setSn(String sn) {
        SystemConfig.sn = sn;
    }

    public static Equipment getEquipment() {
        return equipment;
    }

    public static void setEquipment(Equipment equipment) {
        SystemConfig.equipment = equipment;
    }

    public static Double getFuelFull() {
        return fuelFull;
    }

    public static void setFuelFull(Double fuelFull) {
        SystemConfig.fuelFull = fuelFull;
    }


    public static boolean isNeedStop() {
        return needStop;
    }
    public static void setNeedStop(boolean needStop) {
        SystemConfig.needStop = needStop;
    }

    /**
     * Пауза кратно секунде
     * @param l число секунд
     */
    public static void pauseDOne(long l) {
        try {
            TimeUnit.SECONDS.sleep(l);
        } catch (InterruptedException ex) {
            logger.info("sleep aborting");
        }
    }


    public static void pause(long l) {
        try {
            TimeUnit.SECONDS.sleep(l);
        } catch (InterruptedException ex) {
            logger.info("sleep aborting");
        }
    }

}
