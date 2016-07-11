package smartmetropolis.smartlab.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class User {	
	@Id
	private String login;
	private String password;
	private Float preferenceTemperature;

	@OneToMany(cascade=CascadeType.REMOVE)
	@JoinColumn(name="user_id")
	private List<Scheduling> schedulings = new ArrayList<Scheduling>();

	public User() {
		// TODO Auto-generated constructor stub
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

	public List<Scheduling> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<Scheduling> schedulings) {
		this.schedulings = schedulings;
	}

}
