package smartmetropolis.smartlab.model;


import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Measurement {

	@Id
	@GeneratedValue
	private Long id;
	private String value;
	private Date time;

	@ManyToOne(cascade = CascadeType.REMOVE)
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

	public Long getId() {
		return id;
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

	public void setId(Long measurementId) {
		this.id = measurementId;
	}

	@Override
	public String toString() {
		return "Measurement [id=" + id + ", value=" + value + ", time=" + time
				+ ", sensor=" + sensor + "]";
	}

	
	
}
