package smartmetropolis.smartlab.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;

@Entity
public class AirConditionerState {

	@Id
	@GeneratedValue
	private Long id;
	private AirConditionerAction action;
	private Date timestamp;
	@ManyToOne(cascade = CascadeType.REMOVE)
	private AirConditioner airConditioner;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AirConditioner getAirConditioner() {
		return airConditioner;
	}

	public void setAirConditioner(AirConditioner airConditioner) {
		this.airConditioner = airConditioner;
	}

	public AirConditionerAction getAction() {
		return action;
	}

	public void setAction(AirConditionerAction action) {
		this.action = action;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
