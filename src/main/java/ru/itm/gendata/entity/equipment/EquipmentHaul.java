package ru.itm.gendata.entity.equipment;

import ru.itm.gendata.entity.trans.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "equipment_haul", schema = "equipment")
public class EquipmentHaul extends AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "equip_id")
	private Short equipId;
	@Column(name = "volume")
	private float volume;
	@Column(name = "payload")
	private float payload;
	@Column(name = "empty_weight")
	private Float emptyWeight;
	@Column(name = "tire_type")
	private String tireType;

	public EquipmentHaul() {
	}

	@Override
	public String toString() {
		return "equipment.equipment_haul [id=" + id + ", empty_weight=" + emptyWeight + ", tire_type=" + tireType
				+ ", equip_id=" + equipId + "]";
	}

	public Short getEquip_id() {
		return equipId;
	}

	public void setEquip_id(Short equip_id) {
		this.equipId = equip_id;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getPayload() {
		return payload;
	}

	public void setPayload(float payload) {
		this.payload = payload;
	}

	public Float getEmptyWeight() {
		return emptyWeight;
	}

	public void setEmptyWeight(Float emptyWeight) {
		this.emptyWeight = emptyWeight;
	}

	public String getTireType() {
		return tireType;
	}

	public void setTireType(String tireType) {
		this.tireType = tireType;
	}
}
