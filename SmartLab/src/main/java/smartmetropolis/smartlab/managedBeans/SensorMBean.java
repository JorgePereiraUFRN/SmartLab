package smartmetropolis.smartlab.managedBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.RoomController;
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
	private String predio;
	private List<Sensor> sensors = new ArrayList<Sensor>();
	private Map<SensorType, SensorType> sensorsType;
	private Map<String, String> localsMap;
	private Map<String, String> roomsMap;

	private LocalController localController;

	private SensorController sensorController;
	
	private RoomController roomController;

	@PostConstruct
	public void init() {
		sensor = new Sensor();

		sensorController = SensorController.getInstance();
		localController = LocalController.getInstance();
		roomController = RoomController.getInstance();

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
			Local l = localController.findLocal(predio);
			
			if (l != null) {
				
				List<Room> rooms = roomController.findRoomsByBuilding(l.getLocalName());
				
				if(rooms == null){
					return;
				}
				
				for (Room r : rooms) {
					roomsMap.put(r.getRoomName(), r.getRoomName());
				}
			}

		} catch (DAOException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao recuperar dados: ", e.getMessage()));
		}

	}

	public String saveSensor() {
		try {
		
			String id = UUID.randomUUID().toString();
			
			sensor.setId(id.split("-")[0]);
	
			sensorController.saveSensor(sensor);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Save Sensor: ", "Sensor Saved!"));

			listSensors();

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
		
		return null;
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

	

	public String getPredio() {
		return predio;
	}

	public void setPredio(String predio) {
		this.predio = predio;
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