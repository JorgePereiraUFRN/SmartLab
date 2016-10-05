package smartmetropolis.smartlab.managedBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

@ManagedBean(name = "sensorB")
@SessionScoped
public class SensorMBean {

	private Sensor sensor;
	private Room room;
	private Local local;
	private List<Sensor> sensors = new ArrayList<Sensor>();
	private Map<SensorType, SensorType> sensorsType;
	private Map<String, String> localsMap;
	private Map<String, String> roomsMap;

	private LocalController localController;

	private SensorController sensorController;

	@PostConstruct
	public void init() {
		sensor = new Sensor();
		room = new Room();
		local = new Local();

		sensorController = SensorController.getInstance();
		localController = LocalController.getInstance();
		// roomController = RoomController.getInstance();

		initSensorsTypeMap();
		initLocalsMap();

	}

	private void initSensorsTypeMap() {
		sensorsType = new HashMap<SensorType, SensorType>();

		sensorsType.put(SensorType.HUMIDITY, SensorType.HUMIDITY);
		sensorsType.put(SensorType.PRESENCE, SensorType.PRESENCE);
		sensorsType.put(SensorType.TEMPERATURE,
				SensorType.TEMPERATURE);
		sensorsType.put(SensorType.OTHER, SensorType.OTHER);
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
			Local l = localController.findLocal(local.getLocalName());
			
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

	public void saveSensor() {
		try {
			room.setLocal(local);
			sensor.setRoom(room);
			
	
			sensorController.saveSensor(sensor);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Save Sensor: ", "Sensor Saved!"));

			listSensors();

			room = new Room();
			local = new Local();
			sensor = new Sensor();

		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Save Sensor: ", "Erro to save in database!"));
		} catch (validateDataException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Save Sensor: ", e.getMessage()));
		}
	}

	public void listSensors() {
		try {
			sensors = sensorController.findSensors();
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"List Sensor: ", "Erro to list sensors!"));
		}
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

	
	public Map<SensorType, SensorType> getSensorsType() {
		return sensorsType;
	}

	public void setSensorsType(Map<SensorType, SensorType> sensorsType) {
		this.sensorsType = sensorsType;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
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

}