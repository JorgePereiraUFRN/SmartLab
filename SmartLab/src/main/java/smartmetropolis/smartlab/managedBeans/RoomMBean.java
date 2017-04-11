package smartmetropolis.smartlab.managedBeans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Room;

@ManagedBean(name = "roomB")
public class RoomMBean {

	private Room room;
	private Local local;
	private RoomController roomController;
	private LocalController localController;
	private List<Room> rooms;
	private Map<String, String> localsMap;

	public RoomMBean() {

		room = new Room();
		roomController = RoomController.getInstance();
		localController = LocalController.getInstance();
		local = new Local();

		initMap();
	}

	private void initMap() {
		localsMap = new HashMap<String, String>();

		try {
			List<Local> locals = localController.findAllLocals();

			for (Local l : locals) {
				localsMap.put(l.getLocalName(), l.getLocalName());
			}

		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao recuperar dados: ", e.getMessage()));
		}

	}

	public void saveRoom() {

		try {

			// Local local = localController.findLocal(localId);
			room.setPredio(local.getLocalName());
			roomController.saveRoom(room);
			room = new Room();
			local = new Local();

			listRoons();

		} catch (validateDataException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Dados inválidos! ", e.getMessage()));
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao salvar: ", e.getMessage()));
		}
	}

	public void listRoons() {

		try {
			rooms = roomController.findAllRooms();
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao listar salas.", e.getMessage()));
		}
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public Map<String, String> getLocalsMap() {
		return localsMap;
	}

	public void setLocalsMap(Map<String, String> localsMap) {
		this.localsMap = localsMap;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

}
