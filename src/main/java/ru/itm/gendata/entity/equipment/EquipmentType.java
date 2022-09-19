package ru.itm.gendata.entity.equipment;

import javax.persistence.*;

@Entity
@Table(name="equip_type", schema = "equipment")
public class EquipmentType{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Enumerated(EnumType.STRING)
	private EQUIPMENT_TYPE name;
	private String descr;
	private boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EquipmentType() {
		
	}

	public String toStringShow() {
		return "equipment.equip_type [id=" + id + ", name=" + name + ", descr=" + descr
				+ ", active=" + active + "]";
	}

	public EQUIPMENT_TYPE getName() {
		return name;
	}

	public void setName(EQUIPMENT_TYPE name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
