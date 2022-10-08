package ru.itm.gendata.entity.location;

import lombok.*;
import ru.itm.gendata.entity.trans.AbstractEntity;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "LOCATION", schema = "LOCATION")
public class Location  extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false, length = 24)
    private String name;

    @Column(name = "DESCRIPTION", length = 64)
    private String description;

    @Column(name = "location_type_id")
    private Integer locationTypeId;

    @Column(name = "THRESHOLD_TIME")
    private Short thresholdTime;

    @Column(name = "loc_material_id")
    private Short locMaterialId;

    @Column(name = "loc_material2_id")
    private Short locMaterial2Id;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = false;

    @Column(name = "TYPE_POLYGON", nullable = false)
    private Short typePolygon;

    @Column(name = "HEIGHT")
    private Float height;

    @Column(name = "EXT_ID", length = 36)
    private String extId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLocationTypeId() {
        return locationTypeId;
    }

    public void setLocationTypeId(Integer locationTypeId) {
        this.locationTypeId = locationTypeId;
    }

    public Short getThresholdTime() {
        return thresholdTime;
    }

    public void setThresholdTime(Short thresholdTime) {
        this.thresholdTime = thresholdTime;
    }

    public Short getLocMaterialId() {
        return locMaterialId;
    }

    public void setLocMaterialId(Short locMaterialId) {
        this.locMaterialId = locMaterialId;
    }

    public Short getLocMaterial2Id() {
        return locMaterial2Id;
    }

    public void setLocMaterial2Id(Short locMaterial2Id) {
        this.locMaterial2Id = locMaterial2Id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Short getTypePolygon() {
        return typePolygon;
    }

    public void setTypePolygon(Short typePolygon) {
        this.typePolygon = typePolygon;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }
}