package smartmetropolis.smartlab.managedBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;

@ManagedBean(name = "measurementB")
@SessionScoped
public class MeasurementMB {

	private String localName;
	private String roomName;
	private String sensorId;
	private Map<String, String> localsMap;
	private Map<String, String> roomsMap;
	private Map<String, String> sensorsMap;
	private LocalController localController;
	private List<Measurement> measurements;

	private SensorController sensorController;
	private RoomController roomController;

	public MeasurementMB() {
		sensorController = SensorController.getInstance();
		localController = LocalController.getInstance();
		roomController = RoomController.getInstance();
		measurements = new ArrayList<Measurement>();

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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initSensorsMap() {

		sensorsMap = new HashMap<String, String>();

		try {
			Room r = roomController.findRoom(roomName, localName);
			if (r != null) {
				for (Sensor s : r.getSensors()) {
					sensorsMap.put(
							"sensor id: " + s.getId() + " tipo: "
									+ s.getSensorType(), s.getId().toString());

				}
			}

		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (validateDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listMeasurements() {

		try {

			Sensor s = sensorController.findSensor(Long.parseLong(sensorId));

			measurements = s.getMeasurements();

		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
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

	public Map<String, String> getSensorsMap() {
		return sensorsMap;
	}

	public void setSensorsMap(Map<String, String> sensorsMap) {
		this.sensorsMap = sensorsMap;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

}
