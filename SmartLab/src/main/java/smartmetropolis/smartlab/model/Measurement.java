package smartmetropolis.smartlab.model;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Measurement {

	@Id
	@GeneratedValue
	private Long measurementId;
	private String value;
	private Date time;

	@ManyToOne 
	@Cascade(CascadeType.DELETE)
	private Sensor sensor;

	public Measurement() {
		// TODO Auto-generated constructor stub
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getMeasurementId() {
		return measurementId;
	}

	public void setMeasurmentId(Long id) {
		this.measurementId = id;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public void setMeasurementId(Long measurementId) {
		this.measurementId = measurementId;
	}

	
}
