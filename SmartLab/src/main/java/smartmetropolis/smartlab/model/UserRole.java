package smartmetropolis.smartlab.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name="userRole")
public class UserRole {

	@Id
	@GeneratedValue
	private Long id;

	private String role;
	@OneToOne
	@JoinColumn(name="user_login")
	private User user;
	
	public static String ADMININISTRATOR = "Administrador", SUPPORT = "Suporte", PROFESSOR = "Professor";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", role=" + role + "]";
	}
	
	

}
