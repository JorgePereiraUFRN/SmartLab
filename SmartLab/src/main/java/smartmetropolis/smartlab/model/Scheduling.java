package smartmetropolis.smartlab.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity
public class Scheduling {

	@Id
	private Long id;

	@ManyToOne(cascade = CascadeType.REMOVE)
	private String roomName;

	@ManyToOne(cascade = CascadeType.REMOVE)
	private User user;
	private Date date;
	private String description;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Scheduling() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

}
