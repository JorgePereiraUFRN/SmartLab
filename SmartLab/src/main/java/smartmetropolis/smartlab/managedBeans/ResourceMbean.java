package smartmetropolis.smartlab.managedBeans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.ResourceController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Resource;
import smartmetropolis.smartlab.model.ResourceType;
import smartmetropolis.smartlab.model.Room;

@ManagedBean
@SessionScoped
public class ResourceMbean {

	private Resource resource;
	private String building;
	private String room;
	private String resourceType;
	private Map<String, String> buildigsMap;
	private Map<String, String> roomsMap;
	private Map<String, String> resourceTypeMap;
	private List<Resource> resources;

	private LocalController localController;
	private ResourceController resourceController;
	private RoomController roomController;

	public ResourceMbean() {

		resource = new Resource();
		localController = LocalController.getInstance();
		resourceController = ResourceController.getInstance();
		roomController = RoomController.getInstance();

		buildigsMap = new HashMap<String, String>();
		roomsMap = new HashMap<String, String>();
		resourceTypeMap = new HashMap<String, String>();

		initBuildingsMap();
		initResourcesTypeMap();
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

	public String save() {

		resource.setRoom(room);
		resource.setResourceType(getSelectedResourceType());

		try {
			resourceController.saveResource(resource);
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao salvar dados: ", e.getMessage()));

			e.printStackTrace();

			return null;

		} catch (validateDataException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Dados invalidos: ", e.getMessage()));

			e.printStackTrace();
			return null;
		}

		resource = new Resource();

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso: ",
						"Recursos salvo com suceso!"));

		return null;

	}

	public String findResource() {

		try {

			if (getSelectedResourceType() != null) {

				resources = resourceController.findResourcesByRoomAndResourceType(room,
						getSelectedResourceType());
			} else {
				resources = resourceController.findResourcesByRoom(room);
			}
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao consultar: ", e.getMessage()));

		}

		return null;

	}

	private ResourceType getSelectedResourceType() {

		if (resourceType.equalsIgnoreCase(ResourceType.AirConditioner
				.toString())) {
			return ResourceType.AirConditioner;
		} else if (resourceType.equalsIgnoreCase(ResourceType.Compututer
				.toString())) {

			return ResourceType.Compututer;
		} else if (resourceType.equalsIgnoreCase(ResourceType.Other.toString())) {

			return ResourceType.Other;
		} else if (resourceType
				.equalsIgnoreCase(ResourceType.Sensor.toString())) {

			return ResourceType.Sensor;
		} else if (resourceType.equalsIgnoreCase(ResourceType.Software
				.toString())) {

			return ResourceType.Software;
		} else {
			return null;
		}
	}

	public void selectedRoom() {
		System.out.println(room);
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
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

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Map<String, String> getResourceTypeMap() {
		return resourceTypeMap;
	}

	public void setResourceTypeMap(Map<String, String> resourceTypeMap) {
		this.resourceTypeMap = resourceTypeMap;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	
	

}
