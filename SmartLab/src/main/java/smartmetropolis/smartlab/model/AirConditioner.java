package smartmetropolis.smartlab.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class AirConditioner {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Room room;	
	private String ipaddressAirControl;
	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "airConditioner_id")
	private List<AirConditionerState> historical = new ArrayList<AirConditionerState>();
	
	public AirConditioner() {
		// TODO Auto-generated constructor stub
	}
	
	public Boolean itsOn;
	
	public Boolean getItsOn() {
		return itsOn;
	}

	public void setItsOn(Boolean itsOn) {
		this.itsOn = itsOn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public List<AirConditionerState> getHistorical() {
		return historical;
	}

	public void setHistorical(List<AirConditionerState> historical) {
		this.historical = historical;
	}

	public String getIpaddressAirControl() {
		return ipaddressAirControl;
	}

	public void setIpaddressAirControl(String ipaddressAirControl) {
		this.ipaddressAirControl = ipaddressAirControl;
	}

	@Override
	public String toString() {
		return "AirConditioner [id=" + id + ", room=" + room
				+ ", ipaddressAirControl=" + ipaddressAirControl + "]";
	}

	

}
