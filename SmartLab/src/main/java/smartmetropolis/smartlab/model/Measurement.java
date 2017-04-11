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
public class Measurement implements Comparable<Measurement> {

	@Id
	@GeneratedValue
	private Long id;
	private String value;
	private Date time;

	private String sensorId;

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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Measurement [id=" + id + ", value=" + value + ", time=" + time
				+ ", sensorId=" + sensorId + "]";
	}

	public void setId(Long measurementId) {
		this.id = measurementId;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public int compareTo(Measurement o) {
		return this.getTime().compareTo(o.getTime());
	}

}
