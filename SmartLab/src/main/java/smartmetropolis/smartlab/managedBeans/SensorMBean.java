package smartmetropolis.smartlab.managedBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

@ManagedBean(name = "sensorB")
public class SensorMBean {

	private Sensor sensor;
	private List<Sensor> sensors = new ArrayList<Sensor>();
	private Map<SensorType, String> sensorsType;
	private String localName;
	private Map<String, String> localsMap;
	private Map<String, String> roomsMap;
	
	private LocalController localController;
	//private RoomController roomController;

	private SensorController sensorController;

	@PostConstruct
	public void init() {
		sensor = new Sensor();

		sensorController = SensorController.getInstance();
		localController = LocalController.getInstance();
		//roomController = RoomController.getInstance();
		
		initSensorsTypeMap();
		initLocalsMap();

	}

	private void initSensorsTypeMap() {
		sensorsType = new HashMap<SensorType, String>();

		sensorsType.put(SensorType.HUMIDITY, SensorType.OTHER.toString());
		sensorsType.put(SensorType.OTHER, SensorType.OTHER.toString());
		sensorsType.put(SensorType.PRESENCE, SensorType.PRESENCE.toString());
		sensorsType.put(SensorType.TEMPERATURE,
				SensorType.TEMPERATURE.toString());
	}
	
	
	private void initLocalsMap(){
		
		try {
			List<Local> locals = localController.findAllLocals();
			localsMap = new HashMap<String, String>();
			
			for(Local l : locals){
				localsMap.put(l.getLocaName(), l.getLocaName());
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void initRoomsMap(){
		
		roomsMap  = new HashMap<String, String>();
		
		try {
			Local l = localController.findLocal(localName);
			
			for(Room r : l.getRooms()){
				roomsMap.put(r.getRoomName(), r.getRoomName());
			}
			
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void saveSensor() {
		try {
			sensorController.saveSensor(sensor);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Save Sensor: ", "Sensor Saved!"));

			listSensors();

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

	public Map<SensorType, String> getSensorsType() {
		return sensorsType;
	}

	public void setSensorsType(Map<SensorType, String> sensorsType) {
		this.sensorsType = sensorsType;
	}

}