package ru.itm.gendata.entity.shift;


import ru.itm.gendata.entity.trans.AbstractEntity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "shifts", schema = "shift")
public class Shift extends AbstractEntity {
	@Id
	@GeneratedValue
	private Integer id;
	private String description;

	@Temporal(TemporalType.TIME)
	@Column(name = "start_time")
	private Date startTime;

	@Temporal(TemporalType.TIME)
	@Column(name = "endTime")
	private Date endTime;

	@Column(name = "threshold")
	private Integer threshold;

	@Column(name = "ext_id")
	private Long ext_id;

	public Shift(){}

	@Override
	public String toString() {
		return "shift.shifts{" +
				"id=" + id +
				", description=" + description +
				", start_time=" + startTime +
				", endTime=" + endTime +
				", thresold=" + threshold +
				", ext_id=" + ext_id +
				'}';
	}

	public String toStringShow() {
		return this.toString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public Long getExt_id() {
		return ext_id;
	}

	public void setExt_id(Long ext_id) {
		this.ext_id = ext_id;
	}

	public Integer getId() {
		return id;
	}
}
