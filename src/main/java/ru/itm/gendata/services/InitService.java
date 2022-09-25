package ru.itm.gendata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.SerialNumber;
import ru.itm.gendata.entity.SerialNumberRepository;
import ru.itm.gendata.entity.equipment.EquipmentRepository;
import ru.itm.gendata.entity.equipment.EquipmentTypeRepository;

import java.util.List;

@Service
public class InitService {
    private static Logger logger = LoggerFactory.getLogger(InitService.class);

    private EquipmentRepository equipmentRepository;         //репозиторий для чтения данных об оборудовании
    private SerialNumberRepository serialNumberRepository;   //по серийному номеру определяем,что за техника у нас
    private EquipmentTypeRepository equipmentTypeRepository; //типы техники

    @Autowired
    public InitService(EquipmentRepository equipmentRepository, SerialNumberRepository serialNumberRepository, EquipmentTypeRepository equipmentTypeRepository) {
        this.equipmentRepository = equipmentRepository;
        this.serialNumberRepository = serialNumberRepository;
        this.equipmentTypeRepository = equipmentTypeRepository;
    }

    public void initEquipmentInfo() {
        List<SerialNumber> optSerial = serialNumberRepository.findAll();    //здесь может быть только 1 запись
        if (!optSerial.isEmpty()) {
            try {
                SystemConfig.setSn(optSerial.get(0).getSn());                          //нашли серийник в базе
                SystemConfig.setEquipment(equipmentRepository.findFirstByMtSn(SystemConfig.getSn())); //нашли оборудование
                SystemConfig.setEquipmentName(SystemConfig.getEquipment().getDescription());
                SystemConfig.setEquipmentId(SystemConfig.getEquipment().getId());                     //в нем уже id
                SystemConfig.setFuelFull(SystemConfig.getEquipment().getFuel());       //объем бака
                /**Тип оборудования возьмем из базы*/
                SystemConfig.setEquipmentType(equipmentTypeRepository.findByIdIs(SystemConfig.getEquipment().getEquip_type_id()).getDescr());
                SystemConfig.setFuelConsumptionPerHour(getConsumptionPerHourByType(SystemConfig.getEquipment().getEquip_type_id()));
            } catch (Exception e) {
                logger.warn("Equipment not found in H2 base.");
            }

        }
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




}
