package ru.itm.gendata.entity.trans;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Транс данные по уровню топлива
 */
@Entity
@Table(name = "trans_fuel", schema = "trans")
public class TransFuel extends AbstractEntity {
    @Id
//    @SequenceGenerator(name = "seq",
//            sequenceName = "sequence",
//            initialValue = 1, allocationSize = 20)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @GeneratedValue
    private Long id;

    @Column(name = "equip_id")
    private Long equipId;

    @Column(name = "shift_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar shiftDate;

    @Column(name = "shift_id")
    private Long shiftId;

    @Column(name = "time_read")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar timeRead;

    @Column(name = "fuel_level")
    private Double fuelLevel;


    @Column(name = "fuel_raw")
    private Double fuelRaw;

    @Column(name = "trans_cycles_id")
    private Long transCycles_id;

    @Override
    public String toString() {
        return "trans.trans_fuel{" +
                "id=" + id +
                ", equipId=" + equipId +
                ", shiftDate=" + shiftDate.getTime() +
                ", shiftId=" + shiftId +
                ", timeRead=" + timeRead.getTime() +
                ", fuelLevel=" + fuelLevel +
                ", fuelRaw=" + fuelRaw +
                ", transCycles_id=" + transCycles_id +
                '}';
    }

    public TransFuel(Long equipId, Calendar shiftDate, Long shiftId, Calendar timeRead, Double fuelLevel, Double fuelRaw, Long transCycles_id) {
        this.equipId = equipId;
        this.shiftDate = shiftDate;
        this.shiftId = shiftId;
        this.timeRead = timeRead;
        this.fuelLevel = fuelLevel;
        this.fuelRaw = fuelRaw;
        this.transCycles_id = transCycles_id;
    }

    public TransFuel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipId() {
        return equipId;
    }

    public void setEquipId(Long equipId) {
        this.equipId = equipId;
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

    public Double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(Double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public Double getFuelRaw() {
        return fuelRaw;
    }

    public void setFuelRaw(Double fuelRaw) {
        this.fuelRaw = fuelRaw;
    }

    public Long getTransCycles_id() {
        return transCycles_id;
    }

    public void setTransCycles_id(Long transCycles_id) {
        this.transCycles_id = transCycles_id;
    }
}