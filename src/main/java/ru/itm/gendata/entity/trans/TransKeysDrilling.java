package ru.itm.gendata.entity.trans;

import javax.persistence.*;

@Entity
@Table(name = "trans_keys_drilling", schema = "trans")
public class TransKeysDrilling  extends AbstractEntity{
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_hole", nullable = false)
    private Long idHole;

    @Column(name = "id_trans_drilling", nullable = false)
    private Long idTransDrilling;

    @Column(name = "id_trans_sensor", nullable = false)
    private Long idTransSensor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdHole() {
        return idHole;
    }

    public void setIdHole(Long idHole) {
        this.idHole = idHole;
    }

    public Long getIdTransDrilling() {
        return idTransDrilling;
    }

    public void setIdTransDrilling(Long idTransDrilling) {
        this.idTransDrilling = idTransDrilling;
    }

    public Long getIdTransSensor() {
        return idTransSensor;
    }

    public void setIdTransSensor(Long idTransSensor) {
        this.idTransSensor = idTransSensor;
    }

    @Deprecated
    public TransKeysDrilling() {
    }

    public TransKeysDrilling(Long idHole, Long idTransDrilling, Long idTransSensor) {
        this.idHole = idHole;
        this.idTransDrilling = idTransDrilling;
        this.idTransSensor = idTransSensor;
    }

    public TransKeysDrilling(TransKeysDrilling t) {
        this.idHole = t.idHole;
        this.idTransDrilling = t.idTransDrilling;
        this.idTransSensor = t.idTransSensor;
    }

    @Override
    public String toString() {
        return "trans.trans_keys_drilling" + '{' +
                "\"id\":" + id +
                ", \"id_hole\":" + idHole +
                ", \"id_trans_drilling\":\"" + idTransDrilling + "\"" +
                ", \"id_trans_sensor\":\"" + idTransSensor + "\"" +
                '}';
    }


}