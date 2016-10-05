package smartmetropolis.smartlab.managedBeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import smartmetropolis.smartlab.controller.AirConditionerStateController;
import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.AirConditionerState;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Room;

@ManagedBean(name = "AirCstateB")
@SessionScoped
public class AirConditionerStateMBean {

	private String localName;
	private String roomName;

	private Date initialDate;
	private Date finalDate;

	private Map<String, String> localsMap;
	private Map<String, String> roomsMap;

	private LocalController localController;
	private AirConditionerStateController airCStateController;

	private List<AirConditionerState> airCStates = new ArrayList<AirConditionerState>();

	public AirConditionerStateMBean() {
		localController = LocalController.getInstance();
		airCStateController = AirConditionerStateController.getInstance();
		initLocalsMap();
	}

	private void initLocalsMap() {

		try {
			List<Local> locals = localController.findAllLocals();
			localsMap = new HashMap<String, String>();

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

	public void initRoomsMap() {

		roomsMap = new HashMap<String, String>();

		try {
			Local l = localController.findLocal(localName);

			if (l != null) {
				for (Room r : l.getRooms()) {
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

	public void listAirCStates() {
		try {

			if (roomName != null && localName != null && initialDate != null
					&& finalDate != null) {
				airCStates = airCStateController.listByRoomAndDate(roomName,
						localName, initialDate, finalDate);
			}
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao recuperar dados: ", e.getMessage()));
		} catch (validateDataException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Dado inválido: ", e.getMessage()));
		}
	}

	
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Map<String, String> getLocalsMap() {
		return localsMap;
	}

	public void setLocalsMap(Map<String, String> localsMap) {
		this.localsMap = localsMap;
	}

	public Map<String, String> getRoomsMap() {
		return roomsMap;
	}

	public void setRoomsMap(Map<String, String> roomsMap) {
		this.roomsMap = roomsMap;
	}

	public List<AirConditionerState> getAirCStates() {
		return airCStates;
	}

	public void setAirCStates(List<AirConditionerState> airCStates) {
		this.airCStates = airCStates;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

}
