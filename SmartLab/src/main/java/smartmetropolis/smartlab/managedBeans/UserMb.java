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

	private User usuario;
	
	private static  UserController USER_CONTROLLER;
	
	private String selectedRole;

	private Map<String, String> userRoles = new HashMap<String, String>();

	public UserMb() {
		
		USER_CONTROLLER = UserController.getInstance();
		
		usuario = new User();
		
		userRoles.put(UserRole.ADMININISTRATOR, UserRole.ADMININISTRATOR);
		userRoles.put(UserRole.PROFESSOR, UserRole.PROFESSOR);
		userRoles.put(UserRole.SUPPORT, UserRole.SUPPORT);
	}

	public void saveUser() {

		
		
		UserRole userRole = new UserRole();
		userRole.setRole(selectedRole);
		userRole.setUser(usuario);
		
		usuario.setRole(userRole);
		
		try {
			USER_CONTROLLER.saveUser(usuario);
			
		} catch (UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (validateDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		usuario = new User();
		
	}
	
	
	public String logIn() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		try {
			request.login(usuario.getLogin(), usuario.getPassword());
			System.out.println("logado!");
			return "home";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					"formLogin",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
							"Login ou senha inválidos"));
			return "errorLogin";
		}
		

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

	
}
