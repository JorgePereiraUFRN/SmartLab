package smartmetropolis.smartlab.model;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Solicitation {
	
	@Id
	@GeneratedValue
	private Long id;
	private Date lastUpdate;
	private Date openDate;
	@ManyToOne
	private User user;
	private SolicitationStatus status;
	private String description;
	private Date deadline;
	private String room;
	private ResourceType resource;
	private String observation;

	public Solicitation() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SolicitationStatus getStatus() {
		return status;
	}

	public void setStatus(SolicitationStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public ResourceType getResource() {
		return resource;
	}

	public void setResource(ResourceType resource) {
		this.resource = resource;
	}
	
	

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	@Override
	public String toString() {
		return "Solicitation [id=" + id + ", lastUpdate=" + lastUpdate
				+ ", openDate=" + openDate + ", user=" + user + ", status="
				+ status + ", description=" + description + ", deadline="
				+ deadline + ", room=" + room + ", resource=" + resource
				+ ", observation=" + observation + "]";
	}

	
	

}
