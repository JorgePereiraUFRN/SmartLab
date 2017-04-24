package smartmetropolis.smartlab.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

	@Override
	public String toString() {
		return "User [login=" + login + ", password=" + password
				+ ", preferenceTemperature=" + preferenceTemperature
				+ ", name=" + name + ", email=" + email + ", role=" + role
				+ "]";
	}
	
	

}
