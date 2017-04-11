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
	private String airConditionerId;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAirConditionerId() {
		return airConditionerId;
	}

	public void setAirConditionerId(String airConditionerId) {
		this.airConditionerId = airConditionerId;
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

	@Override
	public String toString() {
		return "AirConditionerState [id=" + id + ", action=" + action
				+ ", timestamp=" + timestamp + ", airConditionerId="
				+ airConditionerId + "]";
	}
		

}
