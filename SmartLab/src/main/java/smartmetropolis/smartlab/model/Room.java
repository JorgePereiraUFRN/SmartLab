package smartmetropolis.smartlab.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@IdClass(value = RoomKey.class)
public class Room {

	@Id
	private String roomName;
	@Id
	private String localName;

	@ManyToOne
	@JoinColumn(name = "localName", insertable = false, updatable = false)
	private Local local;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "room")
	private List<Scheduling> schedulings = new ArrayList<Scheduling>();

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "room")	
	private List<Sensor> sensors = new ArrayList<Sensor>();

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "room")
	private List<AirConditioner> airConditioners = new ArrayList<AirConditioner>();

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
		if (local.getLocalName() != null)
			localName = local.getLocalName();
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	/*
	 * public String getLocalName() { return localName; }
	 * 
	 * public void setLocalName(String localName) { this.localName = localName;
	 * }
	 */

	@XmlTransient
	public List<Scheduling> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<Scheduling> schedulings) {
		this.schedulings = schedulings;
	}

	@XmlTransient
	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

}