package smartmetropolis.smartlab.managedBeans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

import smartmetropolis.smartlab.controller.SolicitationController;
import smartmetropolis.smartlab.controller.UserController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.UserAlreadyExistsException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Notification;
import smartmetropolis.smartlab.model.Solicitation;
import smartmetropolis.smartlab.model.SolicitationStatus;
import smartmetropolis.smartlab.model.User;
import smartmetropolis.smartlab.model.UserRole;

@SessionScoped
@ManagedBean
public class UserMb implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User usuario;

	private static UserController USER_CONTROLLER;

	private static SolicitationController solicitationController;

	private String selectedRole;

	private boolean loged = false;

	private Map<String, String> userRoles = new HashMap<String, String>();

	private List<Notification> notifications = new ArrayList<Notification>();

	private static final String profilesPictureFolder = "/home/jorge/smartplace/profilepictures/";
	
	private UploadedFile imageFile;

	public UserMb() {

		USER_CONTROLLER = UserController.getInstance();
		solicitationController = SolicitationController.getInstance();

		usuario = new User();

		userRoles.put(UserRole.ADMININISTRATOR, UserRole.ADMININISTRATOR);
		userRoles.put(UserRole.PROFESSOR, UserRole.PROFESSOR);
		userRoles.put(UserRole.SUPPORT, UserRole.SUPPORT);
		
		if(usuario == null){
			usuario = new User();
			System.out.println(usuario);
			
		}
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
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e
							.getMessage()));
			e.printStackTrace();
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e
							.getMessage()));
			e.printStackTrace();
		} catch (validateDataException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e
							.getMessage()));
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

			notifications = getNotificationsbyUser(usuario);

			RequestContext.getCurrentInstance()
					.update("datalist_notifications");

			return "home";
		} catch (Exception e) {

			return "errorLogin";
		}
	}

	private List<Notification> getNotificationsbyUser(User user) {

		List<Notification> notifications = null;

		if (isSupport()) {

			try {
				notifications = getNotificationsToSupport();

			} catch (DAOException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e
								.getMessage()));
				e.printStackTrace();
			}
		}

		return notifications;

	}

	private List<Notification> getNotificationsToSupport() throws DAOException {
		List<Notification> notifications;
		List<Solicitation> in_progress = solicitationController
				.listSolicitationsByState(SolicitationStatus.Em_Progresso);
		List<Solicitation> waiting = solicitationController
				.listSolicitationsByState(SolicitationStatus.Aguardando);

		notifications = new ArrayList<Notification>();

		if (in_progress != null && in_progress.size() > 0) {

			Notification n1 = new Notification();

			n1.setMessage("Existem " + in_progress.size()
					+ " solicitações em progresso.");
			notifications.add(n1);
			System.out.println(n1);
		}

		if (waiting != null && waiting.size() > 0) {

			Notification n2 = new Notification();

			n2.setMessage("Existem " + waiting.size()
					+ " novas solicitações em aberto.");
			notifications.add(n2);
		
		}
		return notifications;
	}

	public void saveProfilePricture()  {

			if (imageFile != null) {

				try {
			
					String fileName = profilesPictureFolder+usuario.getLogin()+"_"+imageFile.getFileName();
					
					usuario.setProfilePicture(fileName);

					copyFile(fileName, imageFile.getInputstream());
					
					USER_CONTROLLER.updateUser(usuario);

					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, null,
									"Imagem do perfil atualizada com sucesso!"));

				} catch (IOException e) {
					System.out.println(e.getMessage());
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											null,
											"Desculpe, erro ocorrido ao atualizar a imagem. Tente novamente por favor!"));
				} catch (validateDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
								"Selecione uma imagempara upload!"));
			}

		}

		public void copyFile(String fileName, InputStream in) throws IOException {
			

				OutputStream out = new FileOutputStream(new File(fileName));

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = in.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}

				in.close();
				out.flush();
				out.close();
			
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

	public UploadedFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(UploadedFile imageFile) {
		this.imageFile = imageFile;
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


	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

}
