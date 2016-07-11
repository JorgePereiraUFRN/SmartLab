package smartmetropolis.smartlab.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AirConditioner {
	
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Room room;
	private Float currentTemperature;
	

	public AirConditioner() {
		// TODO Auto-generated constructor stub
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


	public Float getCurrentTemperature() {
		return currentTemperature;
	}


	public void setCurrentTemperature(Float currentTemperature) {
		this.currentTemperature = currentTemperature;
	}
	
	

}
