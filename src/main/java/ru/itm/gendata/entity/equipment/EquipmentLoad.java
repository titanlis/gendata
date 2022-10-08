package ru.itm.gendata.entity.equipment;

import ru.itm.gendata.entity.trans.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "equipment_load", schema = "equipment")
public class EquipmentLoad extends AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Short id;
	@Column(name = "equip_id")
	private Short equipId;
	private float radius_load;
	private float radius_wait;
	private Float radius_bucket;

	public EquipmentLoad() {
	}

	@Override
	public String toStringShow() {
		return "equipment.equipment_load [id=" + id + ", radius_load=" + radius_load + ", radius_wait=" + radius_wait
				+ ", radius_bucket=" + radius_bucket + ", equip_id=" + equipId + "]";
	}

	public Short getEquip_id() {
		return equipId;
	}

	public void setEquip_id(Short equip_id) {
		this.equipId = equip_id;
	}

	public float getRadius_load() {
		return radius_load;
	}

	public void setRadius_load(float radius_load) {
		this.radius_load = radius_load;
	}

	public float getRadius_wait() {
		return radius_wait;
	}

	public void setRadius_wait(float radius_wait) {
		this.radius_wait = radius_wait;
	}

	public Float getRadius_bucket() {
		return radius_bucket;
	}

	public void setRadius_bucket(Float radius_bucket) {
		this.radius_bucket = radius_bucket;
	}
}