package smartmetropolis.smartlab.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@XmlRootElement
@Entity
public class Sensor {

	@Id
	@GeneratedValue
	private Long sensorId;
	private SensorType sensorType;
	private String local;
	private String description;

	@OneToMany
	@Cascade(CascadeType.DELETE)
	private List<Measurement> measurements = new ArrayList<Measurement>();

	public Sensor() {
		// TODO Auto-generated constructor stub
	}

	public Long getSensorId() {
		return sensorId;
	}

	public void setSensorId(Long id) {
		this.sensorId = id;
	}

	public SensorType getSensorType() {
		return sensorType;
	}

	public void setSensorType(SensorType sensorType) {
		this.sensorType = sensorType;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
