package ru.itm.gendata.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="serialnumbers")
@AllArgsConstructor
public class SerialNumber {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sn;

    public String getSn() {
        return sn;
    }

    public SerialNumber() {
    }

    public SerialNumber(String sn) {
        this.sn = sn;
    }
}
