package smartmetropolis.smartlab.model;

import java.util.ArrayList;
import java.util.List;


import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Sensor {

	private String id;
	private SensorType sensorType;
	private String description;
	private String roomName;
	
	private Measurement measurement;

	/*private List<Measurement> measurements = new ArrayList<Measurement>();*/

	public Sensor() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SensorType getSensorType() {
		return sensorType;
	}

	public void setSensorType(SensorType sensorType) {
		this.sensorType = sensorType;
	}

	

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	@Override
	public String toString() {
		return "Sensor [id=" + id + ", sensorType=" + sensorType
				+ ", description=" + description + ", roomName=" + roomName
				+ ", measurement=" + measurement + "]";
	}
	
	
}
	
