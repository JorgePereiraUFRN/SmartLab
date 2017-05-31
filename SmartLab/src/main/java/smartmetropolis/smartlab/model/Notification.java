package smartmetropolis.smartlab.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Notification {
	@Id
	@GeneratedValue
	private long id;
	
	private Date createdAt;
	private String message;
	
	@ManyToOne
	private User user;
	
	private boolean visualizated;

	public boolean isVisualizated() {
		return visualizated;
	}

	public void setVisualizated(boolean visualizated) {
		this.visualizated = visualizated;
	}

	public Notification() {
		// TODO Auto-generated constructor stub
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", createdAt=" + createdAt
				+ ", message=" + message + ", visualizated=" + visualizated
				+ "]";
	}
	
	
	

}
