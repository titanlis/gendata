package ru.itm.gendata.entity.trans;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Координаты техники
 */
@Entity
@Table(name = "trans_coord", schema = "trans")
public class TransCoord extends AbstractEntity{

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "equip_id", nullable = false)
    private Long equipId;                                   //id техники

    @Column(name = "gnss_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar gnssTime;                              //время координаты ГНСС

    @Column(name = "equip_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar equipTime;                             //время получения данных с ГПС (время идентификации)

    @Column(name = "z", precision = 11, scale = 2)
    private BigDecimal z;                                   //координата привязки к блоку (не используется -1)

    @Column(name = "latitude")
    private Float latitude;                                 //координата в системе ГНСС

    @Column(name = "longitude")
    private Float longitude;                                //координата в системе ГНСС


    @Column(name = "azimuth")
    private Float azimuth;                                  //азимут


    @Column(name = "height", precision = 11, scale = 2)
    private BigDecimal height;                              //координата в системе ГНСС

    @Column(name = "speed", precision = 6, scale = 2)
    private BigDecimal speed;                               //скорость движения

    @Column(name = "gnss_pdop", precision = 5, scale = 2)
    private BigDecimal gnssPdop;                            //показатель качества ГНСС PDOP

    @Column(name = "gnss_hdop", precision = 5, scale = 2)
    private BigDecimal gnssHdop;                            //показатель качества ГНСС HDOP

    @Column(name = "gnss_sat", precision = 3)
    private Integer gnssSat;                             //количество используемых для расчета координат спутников

    @Column(name = "gnss_rtk", precision = 2)
    private Integer gnssRtk;                             //тип решения в случае высокуоточки

    @Column(name = "cycle_id")
    private Long cycleId;                                   //FK ссылка на локацию, в которой находится техника

    @Column(name = "loc_id")
    private Long locId;                                     //FK ссылка на исполняемый рейс

    @Deprecated
    public TransCoord() {
    }

    public TransCoord(Long equipId, Calendar gnssTime, Calendar equipTime, BigDecimal z, Float latitude, Float longitude, Float azimuth, BigDecimal height, BigDecimal speed, BigDecimal gnssPdop, BigDecimal gnssHdop, Integer gnssSat, Integer gnssRtk, Long cycleId, Long locId) {
        this.equipId = equipId;
        this.gnssTime = gnssTime;
        this.equipTime = equipTime;
        this.z = z;
        this.latitude = latitude;
        this.longitude = longitude;
        this.azimuth = azimuth;
        this.height = height;
        this.speed = speed;
        this.gnssPdop = gnssPdop;
        this.gnssHdop = gnssHdop;
        this.gnssSat = gnssSat;
        this.gnssRtk = gnssRtk;
        this.cycleId = cycleId;
        this.locId = locId;
    }

    public TransCoord(TransCoord t) {
        this.equipId = t.equipId;
        this.gnssTime = t.gnssTime;
        this.equipTime = t.equipTime;
        this.z = t.z;
        this.latitude = t.latitude;
        this.longitude = t.longitude;
        this.azimuth = t.azimuth;
        this.height = t.height;
        this.speed = t.speed;
        this.gnssPdop = t.gnssPdop;
        this.gnssHdop = t.gnssHdop;
        this.gnssSat = t.gnssSat;
        this.gnssRtk = t.gnssRtk;
        this.cycleId = t.cycleId;
        this.locId = t.locId;
    }

    @Override
    public String toString() {
        return "trans.trans_coord" + '{' +
                "\"id\":" + id +
                ", \"equip_id\":" + equipId +
                ", \"gnss_time\":\"" + calendarToString(gnssTime) + "\"" +
                ", \"equip_time\":\"" + calendarToString(equipTime) + "\"" +
                ", \"z\":" + z +
                ", \"latitude\":" + latitude +
                ", \"longitude\":" + longitude +
                ", \"azimuth\":" + azimuth +
                ", \"height\":" + height +
                ", \"speed\":" + speed +
                ", \"gnss_pdop\":" + gnssPdop +
                ", \"gnss_hdop\":" + gnssHdop +
                ", \"gnss_sat\":" + gnssSat +
                ", \"gnss_rtk\":" + gnssRtk +
                ", \"cycle_id\":" + cycleId +
                ", \"loc_id\":" + locId +
                '}';
    }

    private String calendarToString(Calendar calendar){
        return (calendar!=null)?calendar.getTime().toString():"";
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

    public Calendar getGnssTime() {
        return gnssTime;
    }

    public void setGnssTime(Calendar gnssTime) {
        this.gnssTime = gnssTime;
    }

    public Calendar getEquipTime() {
        return equipTime;
    }

    public void setEquipTime(Calendar equipTime) {
        this.equipTime = equipTime;
    }

    public BigDecimal getZ() {
        return z;
    }

    public void setZ(BigDecimal z) {
        this.z = z;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(Float azimuth) {
        this.azimuth = azimuth;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public BigDecimal getGnssPdop() {
        return gnssPdop;
    }

    public void setGnssPdop(BigDecimal gnssPdop) {
        this.gnssPdop = gnssPdop;
    }

    public BigDecimal getGnssHdop() {
        return gnssHdop;
    }

    public void setGnssHdop(BigDecimal gnssHdop) {
        this.gnssHdop = gnssHdop;
    }

    public Integer getGnssSat() {
        return gnssSat;
    }

    public void setGnssSat(Integer gnssSat) {
        this.gnssSat = gnssSat;
    }

    public Integer getGnssRtk() {
        return gnssRtk;
    }

    public void setGnssRtk(Integer gnssRtk) {
        this.gnssRtk = gnssRtk;
    }

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public Long getLocId() {
        return locId;
    }

    public void setLocId(Long locId) {
        this.locId = locId;
    }

    public String toStringShow() {
        return this.toString();
    }
}