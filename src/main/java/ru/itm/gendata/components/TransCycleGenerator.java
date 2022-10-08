package ru.itm.gendata.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itm.gendata.entity.location.LocationRepository;
import ru.itm.gendata.entity.trans.AbstractEntity;
import ru.itm.gendata.entity.trans.TransCycle;
import ru.itm.gendata.services.*;

import javax.persistence.Column;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Component
public class TransCycleGenerator implements AbstractEntitiesGenerator {
    private static Logger logger = LoggerFactory.getLogger(TransCycleGenerator.class);

    /**Идет ли в данный момент генерация данных?*/
    private Boolean generatorStart = false;
    private Random random = new Random();
    private static ShiftService shiftService;
    private static EquipmentHaulService equipmentHaulService;
    private static EquipmentLoadService equipmentLoadService;
    private static LocationService locationService;
    private static OperatorService operatorService;

    @Autowired
    public TransCycleGenerator(ShiftService shiftService, EquipmentHaulService equipmentHaulService,
                               EquipmentLoadService equipmentLoadService, LocationService locationService,
                               OperatorService operatorService) {
        this.shiftService = shiftService;
        this.equipmentHaulService = equipmentHaulService;
        this.equipmentLoadService = equipmentLoadService;
        this.locationService = locationService;
        this.operatorService = operatorService;
    }

    @Override
    public AbstractEntity getEntity() {
        logger.info("TransCycleGenerator.getEntity starting.");
        LocalDate dateShift = getStartShiftDate();  //дата начала смены
        //-----------------------------------------------------------------
        int tmp = shiftService.getShiftId(Calendar.getInstance());
        Short shiftId = (short)(tmp);               // айди смены
        //-----------------------------------------------------------------
        //haul_id - погружаемая техника по факту
        //Самосвалы по ID самосвала в equipment.equipment_haul
        List<Short> equipmentHaulIdList = equipmentHaulService.getAllId();
        Short haulId = (equipmentHaulIdList.size()>0)?equipmentHaulIdList.get(random.nextInt(equipmentHaulIdList.size()-1)):null;
        //-----------------------------------------------------------------
        //FK Экскаваторы ID техники в equipment.equipment_load
        List<Short> equipmentLuIdList = equipmentLoadService.getAllId();
        Short luId = (equipmentLuIdList.size()>0)?equipmentLuIdList.get(random.nextInt(equipmentLuIdList.size()-1)):null;
        //-----------------------------------------------------------------
        //Время. Начало рейса. Без долей секунды и time zone
        Instant startTime = Instant.now().minusSeconds(600L + random.nextLong(600L));
        //-----------------------------------------------------------------
        //Время. Начала погрузки. Без долей секунды и time zone
        Instant loadTime = startTime.plusSeconds(180 + random.nextLong(120L));  //погрузка начнется через минуту после начала рейса
        //-----------------------------------------------------------------
        //Время. Окончания рейса. Без долей секунды и time zone
        Instant endTime = null;
        //-----------------------------------------------------------------
        //FK Локация. Начало маршрута.
        List<Integer> locationIdList = locationService.getAllId();
        Integer locStartId = (locationIdList.size()>0)?locationIdList.get(random.nextInt(locationIdList.size()-1)):null;
        Integer locEndId = (locationIdList.size()>0)?locationIdList.get(random.nextInt(locationIdList.size()-1)):null;
        //-----------------------------------------------------------------
        Float distanceHaul = random.nextFloat(0.5f)+0.75f; //Расстояние груженым которое проехал АС по данным со справочника (с таблицы Roads)
        Float distanceHaulGps = distanceHaul -0.02f + random.nextFloat(0.04f);    //Расстояние груженым которое проехал АС по данным GPS
        Float distanceHaulSensor = distanceHaul -0.02f + random.nextFloat(0.04f); //Растояние пробега груженым по данным собственного датчика для определения пробега, если он есть.

        Float distance = distanceHaul*2 -0.04f + random.nextFloat(0.08f);                   //Расстояние всего которое проехал АС по данным со справочника (с таблицы Roads)
        Float distanceGps = distance - 0.01f + random.nextFloat(0.02f);          //Расстояние всего которое проехал АС по данным GPS
        Float distanceSensor = distance - 0.01f + random.nextFloat(0.02f); //Расстояние пробега по датчику. Если есть датчик определения общего пробега гружен + порожний.
        //-----------------------------------------------------------------
        Float payload = 120f + random.nextFloat(7f);  //вес груза по весам

        int materialId = random.nextInt(1) + 1;               //FK - Тип перевозимого материала

        Float mt = payload + random.nextFloat(7f);      //Вес груза в тоннах
        Float bcm = payload / (random.nextFloat(3)+2f); //Объем груза в кубах
        Float report = payload - 0.2f + random.nextFloat(0.4f);  //Вес груза для учета

        Short planLuId = haulId;                //FK Экскаватор погрузки по плану

        Integer planLoadLoc = (locationIdList.size()>0)?locationIdList.get(random.nextInt(locationIdList.size()-1)):null;  //FK Зона погрузки по плану - location
        Integer planUnlLoc = locEndId;             //FK Зона разгрузки по плану - location

        Integer costCenterId = null;           //FK - Подразделение или центр затрат в equipment.cost_center

        List<Short> operatorsList = operatorService.getAllId();
        Short operatorId = (operatorsList.size()>0)?operatorsList.get(random.nextInt(operatorsList.size()-1)):null;   //FK - Оператор техники самосвала

        return new TransCycle(
                dateShift,
                shiftId,
                haulId,
                luId,
                startTime,
                loadTime,
                endTime,
                locStartId,
                locEndId,
                distanceHaul,
                distanceHaulGps,
                distanceHaulSensor,
                distance,
                distanceGps,
                distanceSensor,
                payload,
                (short)materialId,
                mt,
                bcm,
                report,
                planLuId,
                planLoadLoc,
                planUnlLoc,
                costCenterId,
                operatorId
                );
    }

    public void setGeneratorStart(Boolean generatorStart) {
        this.generatorStart = generatorStart;
    }

    public boolean getGeneratorStart() {
        return generatorStart;
    }

}
