package smartmetropolis.smartlab.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name="smartplaceUser")
public class User {	
	@Id
	@Column(name="user_login")
	private String login;
	private String password;
	private Float preferenceTemperature;
	private String name;
	private String email;
	@OneToOne(mappedBy="user", cascade = CascadeType.ALL)
	private UserRole role;
	private String profilePicture;
	
	private String userPicture = "/home/jorge/smartplace/profilepictures/default.jpeg";
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Solicitation> solicitations = new ArrayList<Solicitation>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Notification> notifications = new ArrayList<Notification>();
	

	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPreferenceTemperature() {
		return preferenceTemperature;
	}

	public void setPreferenceTemperature(Float preferenceTemperature) {
		this.preferenceTemperature = preferenceTemperature;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
	

	public List<Solicitation> getSolicitations() {
		return solicitations;
	}

	public void setSolicitations(List<Solicitation> solicitations) {
		this.solicitations = solicitations;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public String getUserPicture() {
		return userPicture;
	}

	public void setUserPicture(String userPicture) {
		this.userPicture = userPicture;
	}
	

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	@Override
	public String toString() {
		return "User [login=" + login + ", password=" + password
				+ ", preferenceTemperature=" + preferenceTemperature
				+ ", name=" + name + ", email=" + email + ", role=" + role
				+ ", userPicture=" + userPicture + ", solicitations="
				+ solicitations + ", notifications=" + notifications + "]";
	}

	
	

}
