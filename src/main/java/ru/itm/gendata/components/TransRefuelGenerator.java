package ru.itm.gendata.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itm.gendata.config.SystemConfig;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransRefuel;
import ru.itm.gendata.services.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Random;
@Component
public class TransRefuelGenerator implements AbstractEntitiesGenerator {
    private static Logger logger = LoggerFactory.getLogger(TransRefuelGenerator.class);

    private ShiftService shiftService;
    private LocationService locationService;
    private EquipmentService equipmentService;
    private TransFuelService transFuelService;
    private TransCycleService transCycleService;
    /**Идет ли в данный момент генерация данных?*/
    private Boolean generatorStart = false;
    private Random random = new Random();

    @Autowired
    public TransRefuelGenerator(ShiftService shiftService, LocationService locationService,
                                EquipmentService equipmentService, TransFuelService transFuelService,
                                TransCycleService transCycleService) {
        this.shiftService = shiftService;
        this.locationService = locationService;
        this.equipmentService = equipmentService;
        this.transFuelService = transFuelService;
        this.transCycleService = transCycleService;
    }

    @Override
    public AbstractEntity getEntity() {
        /**При первом запуске находим id техники в базе h2 по серийнику.
         * Желательно. чтобы база уже получила первое обновление*/
        if(SystemConfig.getSn() == null) {
            logger.warn("Equipment not found in H2 base.");
            return null;
        }

        int shiftId = shiftService.getShiftId(getStartShift());
        int equip1Id = Math.toIntExact(equipmentService.getRandomIdNotFuelTruck(1L));
        double fuelRaw = transFuelService.getFuelRaw(SystemConfig.getEquipmentId(), 700.0); //700  - default объем бака

        return new TransRefuel(
                SystemConfig.getShortEquipmentId(),
                getStartShiftDate(),
                (short)shiftId,
                Instant.now(),
                null,
                locationService.getRandomId(1), //1  - default
                (short)equip1Id,                      //1L - default
                (float)fuelRaw,
                transCycleService.getRandomId()
                );
    }

    @Override
    public Calendar getStartShift() {
        return AbstractEntitiesGenerator.super.getStartShift();
    }

    @Override
    public LocalDate getStartShiftDate() {
        return AbstractEntitiesGenerator.super.getStartShiftDate();
    }

    public void setGeneratorStart(Boolean generatorStart) {
        this.generatorStart = generatorStart;
    }

    public boolean getGeneratorStart() {
        return generatorStart;
    }
}
