package smartmetropolis.smartlab.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Room {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@ManyToOne
	private Local local;

	/*
	@OneToMany(cascade=CascadeType.REMOVE)
	@JoinColumn(name="room_id")
	private List<Sensor> sensors = new ArrayList<Sensor>();
	*/

	@OneToMany(cascade=CascadeType.REMOVE)
	@JoinColumn(name="room_id")
	private List<Scheduling> schedulings = new ArrayList<Scheduling>();

	public Room() {
		// TODO Auto-generated constructor stub
	}

	public List<Scheduling> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<Scheduling> schedulings) {
		this.schedulings = schedulings;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	/*public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}*/

}
