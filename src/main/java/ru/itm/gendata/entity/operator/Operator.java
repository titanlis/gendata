package ru.itm.gendata.entity.operator;

import lombok.*;
import ru.itm.gendata.entity.trans.AbstractEntity;

import javax.persistence.*;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "OPERATORS", schema = "OPERATOR")
public class Operator extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Short id;

    @Column(name = "TAB", nullable = false, length = 24)
    private String tab;

    @Column(name = "NAME", nullable = false, length = 36)
    private String name;

    @Column(name = "SURNAME", nullable = false, length = 36)
    private String surname;

    @Column(name = "MIDDLE", length = 36)
    private String middle;

    @Column(name = "LICENCE", length = 36)
    private String licence;

    @Column(name = "EXPIRE")
    private Instant expire;

    @Column(name = "ROLE_ID")
    private Short role;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = false;

    @Column(name = "RFID_ID", length = 32)
    private String rfidId;

    @Column(name = "EXT_ID", length = 36)
    private String extId;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public Instant getExpire() {
        return expire;
    }

    public void setExpire(Instant expire) {
        this.expire = expire;
    }

    public Short getRole() {
        return role;
    }

    public void setRole(Short role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRfidId() {
        return rfidId;
    }

    public void setRfidId(String rfidId) {
        this.rfidId = rfidId;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }
}