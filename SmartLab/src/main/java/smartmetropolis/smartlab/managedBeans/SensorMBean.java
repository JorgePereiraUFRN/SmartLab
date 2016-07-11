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

import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

@ManagedBean(name = "sensorB")
public class SensorMBean {

	private Sensor sensor;
	private List<Sensor> sensors = new ArrayList<Sensor>();
	private Map<SensorType, String> sensorsType;
	private Map<Sensor, String> sensorsMap;
	private List<Measurement> sensorMeasurements = new ArrayList<Measurement>();
	// private long sensorId;

	private SensorController sensorController;

	@ManagedProperty("#{themeService}")
	private SensorService service;

	@PostConstruct
	public void init() {
		sensor = new Sensor();
		sensorController = SensorController.getInstance();

		sensorsType = new HashMap<SensorType, String>();

		sensorsType.put(SensorType.HUMIDITY, SensorType.HUMIDITY.toString());
		sensorsType.put(SensorType.OTHER, SensorType.OTHER.toString());
		sensorsType.put(SensorType.PRESENCE, SensorType.PRESENCE.toString());
		sensorsType.put(SensorType.TEMPERATURE,
				SensorType.TEMPERATURE.toString());
		
		service = new SensorService();
		sensors = service.getSensors();

		/*sensorsMap = new HashMap<Sensor, String>();
		try {
			sensors = sensorController.findSensors();
			for (Sensor s : sensors) {
				sensorsMap.put(s, s.getSensorType() + " " + s.getLocal());
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

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

	public void listMeassurements(ActionEvent e) {
		//System.out.println(sensor.getId());
		sensorMeasurements = sensor.getMeasurements();
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

	public List<Measurement> getSensorMeasurements() {
		return sensorMeasurements;
	}

	public void setSensorMeasurements(List<Measurement> sensorMeasurements) {
		this.sensorMeasurements = sensorMeasurements;
	}

	/*
	 * public Long getSensorId() { return sensorId; }
	 * 
	 * public void setSensorId(Long sensorId) { this.sensorId = sensorId; }
	 */
	public Map<Sensor, String> getSensorsMap() {
		return sensorsMap;
	}

	public void setSensorsMap(Map<Sensor, String> sensorsMap) {
		this.sensorsMap = sensorsMap;
	}

	public SensorService getService() {
		return service;
	}

	public void setService(SensorService service) {
		this.service = service;
	}

}
