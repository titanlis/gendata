package ru.itm.gendata.entity.trans;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "TRANS_REFUEL", schema = "TRANS")
public class TransRefuel extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "EQUIP_ID", nullable = false)
    private Short equipId;                              //FK ID техники

    @Column(name = "SHIFT_DATE", nullable = false)
    private LocalDate shiftDate;                        //Дата начала смены

    @Column(name = "SHIFT_ID", nullable = false)
    private Short shiftId;                              //FK Номер смены

    @Column(name = "START_DT", nullable = false)
    private Instant startDt;                            //Время записи

    @Column(name = "END_DT", nullable = false)
    private Instant endDt;                              //Время окончания заправки

    @Column(name = "LOC_ID")
    private Integer locId;                              //FK Место заправки location

    @Column(name = "FUEL_EQUIP_ID", nullable = false)
    private Short fuelEquipId;                          //FK ID заправочной техники

    @Column(name = "REFUEL", nullable = false)
    private Float refuel;                               //Количество заправленного топлива

    @Column(name = "TRANS_CYCLES_ID")
    private Integer transCyclesId;                      //FK ссылка на рейс

    @Deprecated
    public TransRefuel() {
    }

    public TransRefuel(Short equipId, LocalDate shiftDate, Short shiftId, Instant startDt, Instant endDt, Integer locId, Short fuelEquipId, Float refuel, Integer transCyclesId) {
        this.equipId = equipId;
        this.shiftDate = shiftDate;
        this.shiftId = shiftId;
        this.startDt = startDt;
        this.endDt = endDt;
        this.locId = locId;
        this.fuelEquipId = fuelEquipId;
        this.refuel = refuel;
        this.transCyclesId = transCyclesId;
    }

    @Override
    public String toString() {
        return "trans.trans_refuel" + '{' +
                "\"id\":" + id +
                ", \"equip_id\":" + equipId +
                ", \"shift_date\":" + shiftDate +
                ", \"shift_id\":" + shiftId +
                ", \"start_dt\":" + startDt +
                ", \"end_dt\":" + endDt +
                ", \"loc_id\":" + locId +
                ", \"fuel_equip_id\":" + fuelEquipId +
                ", \"refuel\":" + refuel +
                ", \"trans_cycles_id\":" + transCyclesId +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getEquipId() {
        return equipId;
    }

    public void setEquipId(Short equipId) {
        this.equipId = equipId;
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

    public Instant getStartDt() {
        return startDt;
    }

    public void setStartDt(Instant startDt) {
        this.startDt = startDt;
    }

    public Instant getEndDt() {
        return endDt;
    }

    public void setEndDt(Instant endDt) {
        this.endDt = endDt;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public Short getFuelEquipId() {
        return fuelEquipId;
    }

    public void setFuelEquipId(Short fuelEquipId) {
        this.fuelEquipId = fuelEquipId;
    }

    public Float getRefuel() {
        return refuel;
    }

    public void setRefuel(Float refuel) {
        this.refuel = refuel;
    }

    public Integer getTransCyclesId() {
        return transCyclesId;
    }

    public void setTransCyclesId(Integer transCyclesId) {
        this.transCyclesId = transCyclesId;
    }
}