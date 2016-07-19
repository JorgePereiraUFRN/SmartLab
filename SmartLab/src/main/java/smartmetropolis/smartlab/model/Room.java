package smartmetropolis.smartlab.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;

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

	@OneToMany(cascade = CascadeType.REMOVE)
	private List<Scheduling> schedulings = new ArrayList<Scheduling>();

	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumns({ @JoinColumn(name = "room_roomName"),
			@JoinColumn(name = "room_localName") })
	private List<Sensor> sensors = new ArrayList<Sensor>();

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

	public List<Scheduling> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<Scheduling> schedulings) {
		this.schedulings = schedulings;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

}