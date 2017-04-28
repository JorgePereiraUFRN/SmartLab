package smartmetropolis.smartlab.managedBeans;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.controller.SolicitationController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.ResourceType;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Solicitation;
import smartmetropolis.smartlab.model.SolicitationStatus;

@ManagedBean
@SessionScoped
public class SolicitationMb {

	private Solicitation solicitation;
	private SolicitationController solicitationController;
	@ManagedProperty("#{userMb}")
	private UserMb userMb;
	private String selectedReource;

	private LocalController localController;
	private RoomController roomController;

	private String building;

	private Map<String, String> buildigsMap;
	private Map<String, String> roomsMap;
	private Map<String, String> resourceTypeMap;

	private List<Solicitation> solicitations;

	public SolicitationMb() {
		solicitation = new Solicitation();
		solicitationController = SolicitationController.getInstance();
		localController = LocalController.getInstance();
		roomController = RoomController.getInstance();

		buildigsMap = new HashMap<String, String>();
		roomsMap = new HashMap<String, String>();
		resourceTypeMap = new HashMap<String, String>();

		initBuildingsMap();
		initResourcesTypeMap();

	}

	private void initResourcesTypeMap() {

		resourceTypeMap.put(ResourceType.AirConditioner.toString(),
				ResourceType.AirConditioner.toString());
		resourceTypeMap.put(ResourceType.Compututer.toString(),
				ResourceType.Compututer.toString());
		resourceTypeMap.put(ResourceType.Sensor.toString(),
				ResourceType.Sensor.toString());
		resourceTypeMap.put(ResourceType.Software.toString(),
				ResourceType.Software.toString());
		resourceTypeMap.put(ResourceType.Other.toString(),
				ResourceType.Other.toString());

	}

	private void initBuildingsMap() {

		try {
			List<Local> locals = localController.findAllLocals();
			buildigsMap = new HashMap<String, String>();

			for (Local l : locals) {
				buildigsMap.put(l.getLocalName(), l.getLocalName());
			}
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao recuperar dados: ", e.getMessage()));
		}

	}

	public void initRoomsMap() {

		roomsMap = new HashMap<String, String>();

		try {
			Local l = localController.findLocal(building);

			if (l != null) {

				List<Room> rooms = roomController.findRoomsByBuilding(l
						.getLocalName());

				for (Room r : rooms) {
					roomsMap.put(r.getRoomName(), r.getRoomName());
				}
			}

		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao recuperar dados: ", e.getMessage()));
		}
	}

	public String saveSolicitation() {

		try {

			solicitation.setLastUpdate(new Date());
			solicitation.setOpenDate(new Date());
			solicitation.setUser(userMb.getUsuario());
			solicitation.setStatus(SolicitationStatus.Aguardando);
			solicitation.setResource(getSelectedResourceType());

			solicitationController.saveSolicitation(solicitation);

		} catch (validateDataException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Dados inválidos: ", e.getMessage()));
			e.printStackTrace();
			return null;
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao salvar dados: ", e.getMessage()));
			e.printStackTrace();
			return null;
		}

		solicitation = new Solicitation();

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso: ",
						"Solicitação registrada com suceso!"));

		return null;
	}

	private ResourceType getSelectedResourceType() {

		if (selectedReource.equalsIgnoreCase(ResourceType.AirConditioner
				.toString())) {
			return ResourceType.AirConditioner;
		} else if (selectedReource.equalsIgnoreCase(ResourceType.Compututer
				.toString())) {

			return ResourceType.Compututer;
		} else if (selectedReource.equalsIgnoreCase(ResourceType.Other
				.toString())) {

			return ResourceType.Other;
		} else if (selectedReource.equalsIgnoreCase(ResourceType.Sensor
				.toString())) {

			return ResourceType.Sensor;
		} else if (selectedReource.equalsIgnoreCase(ResourceType.Software
				.toString())) {

			return ResourceType.Software;
		} else {
			return null;
		}
	}

	public String listSolicitationsByUser() {

		try {
			solicitations = solicitationController
					.listSoliciationsByUser(userMb.getUsuario().getLogin());
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao pesquisar dados: ", e.getMessage()));
			e.printStackTrace();
		}

		return null;
	}

	public String listSolicitations() {

		try {
			solicitations = solicitationController.findAll();
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao pesquisar dados: ", e.getMessage()));
			e.printStackTrace();
		}

		return null;
	}

	public Solicitation getSolicitation() {
		return solicitation;
	}

	public void setSolicitation(Solicitation solicitation) {
		this.solicitation = solicitation;
	}

	public UserMb getUserMb() {
		return userMb;
	}

	public void setUserMb(UserMb userMb) {
		this.userMb = userMb;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public Map<String, String> getBuildigsMap() {
		return buildigsMap;
	}

	public void setBuildigsMap(Map<String, String> buildigsMap) {
		this.buildigsMap = buildigsMap;
	}

	public Map<String, String> getRoomsMap() {
		return roomsMap;
	}

	public void setRoomsMap(Map<String, String> roomsMap) {
		this.roomsMap = roomsMap;
	}

	public String getSelectedReource() {
		return selectedReource;
	}

	public void setSelectedReource(String selectedReource) {
		this.selectedReource = selectedReource;
	}

	public Map<String, String> getResourceTypeMap() {
		return resourceTypeMap;
	}

	public void setResourceTypeMap(Map<String, String> resourceTypeMap) {
		this.resourceTypeMap = resourceTypeMap;
	}

	public List<Solicitation> getSolicitations() {
		return solicitations;
	}

	public void setSolicitations(List<Solicitation> solicitations) {
		this.solicitations = solicitations;
	}

}
