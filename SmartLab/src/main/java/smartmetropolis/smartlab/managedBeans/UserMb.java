package smartmetropolis.smartlab.managedBeans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import smartmetropolis.smartlab.controller.UserController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.UserAlreadyExistsException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.User;
import smartmetropolis.smartlab.model.UserRole;

@SessionScoped
@ManagedBean
public class UserMb implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User usuario;
	
	private static  UserController USER_CONTROLLER;
	
	private String selectedRole;
	
	private boolean loged = false;

	private Map<String, String> userRoles = new HashMap<String, String>();

	public UserMb() {
		
		USER_CONTROLLER = UserController.getInstance();
		
		usuario = new User();
		
		userRoles.put(UserRole.ADMININISTRATOR, UserRole.ADMININISTRATOR);
		userRoles.put(UserRole.PROFESSOR, UserRole.PROFESSOR);
		userRoles.put(UserRole.SUPPORT, UserRole.SUPPORT);
	}

	public String saveUser() {

		UserRole userRole = new UserRole();
		userRole.setRole(selectedRole);
		userRole.setUser(usuario);
		
		usuario.setRole(userRole);
		
		try {
			USER_CONTROLLER.saveUser(usuario);
			
			logIn();
			
		} catch (UserAlreadyExistsException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
						e.getMessage()));
			e.printStackTrace();
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
						e.getMessage()));
			e.printStackTrace();
		} catch (validateDataException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
						e.getMessage()));
			e.printStackTrace();
		}
		
		usuario = new User();
		
		return "home";
		
	}
	
	
	public String logIn() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		try {
			request.login(usuario.getLogin(), usuario.getPassword());
			
			usuario = USER_CONTROLLER.findUser(usuario.getLogin());
			
			loged = true;
			
			return "home";
		} catch (Exception e) {
			
			return "errorLogin";
		}
	}
	
	
	
	public String logOut() {
		getRequest().getSession().invalidate();
		return "home";
	}

	
	private HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	
	public boolean isProfessor() {
		return getRequest().isUserInRole("Professor");

	}

	public boolean isAdminitrator() {
		return getRequest().isUserInRole("Administrador");

	}
	
	public boolean isSupport() {
		return getRequest().isUserInRole("Suporte");

	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public Map<String, String> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Map<String, String> userRoles) {
		this.userRoles = userRoles;
	}

	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}

	public boolean isLoged() {
		return loged;
	}

	public void setLoged(boolean loged) {
		this.loged = loged;
	}

	
}
