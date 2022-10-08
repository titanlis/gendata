package ru.itm.gendata.entity.trans;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "trans_cycles", schema = "trans")
public class TransCycle extends AbstractEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "shift_date")
    private LocalDate shiftDate;            //дата смены(dd:mm:yyyy)

    @Column(name = "shift_id")              //айди смены
    private Short shiftId;

    @Column(name = "haul_id", nullable = false)
    private Short haulId;                   //haul_id - погружаемая техника по факту
                                            //Самосвалы по ID самосвала в equipment.equipment_haul

    @Column(name = "lu_id")
    private Short luId;                     //FK Экскаваторы ID техники в equipment.equipment_load

    @Column(name = "start_time")
    private Instant startTime;              //Время. Начало рейса. Без долей секунды и time zone

    @Column(name = "load_time")
    private Instant loadTime;               //Время начала погрузки

    @Column(name = "end_time")
    private Instant endTime;                //Время окончания рейса

    @Column(name = "loc_start_id")
    private Integer locStartId;             //FK Локация. Начало маршрута.

    @Column(name = "loc_end_id")
    private Integer locEndId;               //FK Локация. Конец маршрута.

    @Column(name = "distance_haul")
    private Float distanceHaul;             //Расстояние груженым которое проехал АС по данным со справочника (с таблицы Roads)

    @Column(name = "distance_haul_gps")
    private Float distanceHaulGps;          //Расстояние груженым которое проехал АС по данным GPS

    @Column(name = "distance_haul_sensor")
    private Float distanceHaulSensor;       //Растояние пробега груженым по данным собственного датчика для определения пробега, если он есть.

    @Column(name = "distance")
    private Float distance;                 //Расстояние всего которое проехал АС по данным со справочника (с таблицы Roads)

    @Column(name = "distance_gps")
    private Float distanceGps;              //Расстояние всего которое проехал АС по данным GPS

    @Column(name = "distance_sensor")
    private Float distanceSensor;           //Расстояние пробега по датчику. Если есть датчик определения общего пробега гружен + порожний.

    @Column(name = "payload")
    private Float payload;                  //Вес груза по весам

    @Column(name = "material_id")
    private Short materialId;               //FK - Тип перевозимого материала

    @Column(name = "mt")
    private Float mt;                       //Вес груза в тоннах

    @Column(name = "bcm")
    private Float bcm;                      //Объем груза в кубах

    @Column(name = "report")
    private Float report;                   //Вес груза для учета

    @Column(name = "plan_lu_id")
    private Short planLuId;                 //FK Экскаватор погрузки по плану

    @Column(name = "plan_load_loc")
    private Integer planLoadLoc;            //FK Зона погрузки по плану - location

    @Column(name = "plan_unl_loc")
    private Integer planUnlLoc;             //FK Зона разгрузки по плану - location

    @Column(name = "cost_center_id")
    private Integer costCenterId;           //FK - Подразделение или центр затрат в equipment.cost_center

    @Column(name = "operator_id")
    private Short operatorId;             //FK - Оператор техники самосвала

    @Deprecated
    public TransCycle() { }

    public TransCycle(LocalDate shiftDate, Short shiftId, Short haulId, Short luId, Instant startTime, Instant loadTime, Instant endTime, Integer locStartId, Integer locEndId, Float distanceHaul, Float distanceHaulGps, Float distanceHaulSensor, Float distance, Float distanceGps, Float distanceSensor, Float payload, Short materialId, Float mt, Float bcm, Float report, Short planLuId, Integer planLoadLoc, Integer planUnlLoc, Integer costCenterId, Short operatorId) {
        this.shiftDate = shiftDate;
        this.shiftId = shiftId;
        this.haulId = haulId;
        this.luId = luId;
        this.startTime = startTime;
        this.loadTime = loadTime;
        this.endTime = endTime;
        this.locStartId = locStartId;
        this.locEndId = locEndId;
        this.distanceHaul = distanceHaul;
        this.distanceHaulGps = distanceHaulGps;
        this.distanceHaulSensor = distanceHaulSensor;
        this.distance = distance;
        this.distanceGps = distanceGps;
        this.distanceSensor = distanceSensor;
        this.payload = payload;
        this.materialId = materialId;
        this.mt = mt;
        this.bcm = bcm;
        this.report = report;
        this.planLuId = planLuId;
        this.planLoadLoc = planLoadLoc;
        this.planUnlLoc = planUnlLoc;
        this.costCenterId = costCenterId;
        this.operatorId = operatorId;
    }

    @Override
    public String toString() {
        return "trans.trans_cycles" + '{' +
                "\"id\":" + id +
                ", \"shift_date\":" + shiftDate +
                ", \"shift_id\":" + shiftId +
                ", \"haul_id\":" + haulId +
                ", \"lu_id\":" + luId +
                ", \"start_time\":" + startTime +
                ", \"load_time\":" + loadTime +
                ", \"end_time\":" + endTime +
                ", \"loc_start_id\":" + locStartId +
                ", \"loc_end_id\":" + locEndId +
                ", \"distance_haul\":" + distanceHaul +
                ", \"distance_haul_gps\":" + distanceHaulGps +
                ", \"distance_haul_sensor\":" + distanceHaulSensor +
                ", \"distance\":" + distance +
                ", \"distance_gps\":" + distanceGps +
                ", \"distance_sensor\":" + distanceSensor +
                ", \"payload\":" + payload +
                ", \"material_id\":" + materialId +
                ", \"mt\":" + mt +
                ", \"bcm\":" + bcm +
                ", \"report\":" + report +
                ", \"plan_lu_id\":" + planLuId +
                ", \"plan_load_loc\":" + planLoadLoc +
                ", \"plan_unl_loc\":" + planUnlLoc +
                ", \"cost_center_id\":" + costCenterId +
                ", \"operator_id\":" + operatorId +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public Short getShiftId() {
        return shiftId;
    }

    public void setShiftId(Short shiftId) {
        this.shiftId = shiftId;
    }

    public Short getHaulId() {
        return haulId;
    }

    public void setHaulId(Short haulId) {
        this.haulId = haulId;
    }

    public Short getLuId() {
        return luId;
    }

    public void setLuId(Short luId) {
        this.luId = luId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Instant loadTime) {
        this.loadTime = loadTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Integer getLocStartId() {
        return locStartId;
    }

    public void setLocStartId(Integer locStartId) {
        this.locStartId = locStartId;
    }

    public Integer getLocEndId() {
        return locEndId;
    }

    public void setLocEndId(Integer locEndId) {
        this.locEndId = locEndId;
    }

    public Float getDistanceHaul() {
        return distanceHaul;
    }

    public void setDistanceHaul(Float distanceHaul) {
        this.distanceHaul = distanceHaul;
    }

    public Float getDistanceHaulGps() {
        return distanceHaulGps;
    }

    public void setDistanceHaulGps(Float distanceHaulGps) {
        this.distanceHaulGps = distanceHaulGps;
    }

    public Float getDistanceHaulSensor() {
        return distanceHaulSensor;
    }

    public void setDistanceHaulSensor(Float distanceHaulSensor) {
        this.distanceHaulSensor = distanceHaulSensor;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Float getDistanceGps() {
        return distanceGps;
    }

    public void setDistanceGps(Float distanceGps) {
        this.distanceGps = distanceGps;
    }

    public Float getDistanceSensor() {
        return distanceSensor;
    }

    public void setDistanceSensor(Float distanceSensor) {
        this.distanceSensor = distanceSensor;
    }

    public Float getPayload() {
        return payload;
    }

    public void setPayload(Float payload) {
        this.payload = payload;
    }

    public Short getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Short materialId) {
        this.materialId = materialId;
    }

    public Float getMt() {
        return mt;
    }

    public void setMt(Float mt) {
        this.mt = mt;
    }

    public Float getBcm() {
        return bcm;
    }

    public void setBcm(Float bcm) {
        this.bcm = bcm;
    }

    public Float getReport() {
        return report;
    }

    public void setReport(Float report) {
        this.report = report;
    }

    public Short getPlanLuId() {
        return planLuId;
    }

    public void setPlanLuId(Short planLuId) {
        this.planLuId = planLuId;
    }

    public Integer getPlanLoadLoc() {
        return planLoadLoc;
    }

    public void setPlanLoadLoc(Integer planLoadLoc) {
        this.planLoadLoc = planLoadLoc;
    }

    public Integer getPlanUnlLoc() {
        return planUnlLoc;
    }

    public void setPlanUnlLoc(Integer planUnlLoc) {
        this.planUnlLoc = planUnlLoc;
    }

    public Integer getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(Integer costCenterId) {
        this.costCenterId = costCenterId;
    }

    public Short getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Short operatorId) {
        this.operatorId = operatorId;
    }
}