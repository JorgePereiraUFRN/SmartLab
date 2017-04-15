package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.ConcreteDaoFactory;
import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.MeasurementDaoInterface;
import smartmetropolis.smartlab.dao.SensorDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Sensor;

public class SensorController {

	private final DAOFactory factory = new ConcreteDaoFactory();
	private final SensorDaoInterface sensorDao;
	private static final SensorController SENSOR_CONTROLLER = new SensorController();

	private SensorController() {
		sensorDao = factory.getSensorDao();
	}

	public synchronized static SensorController getInstance() {
		return SENSOR_CONTROLLER;
	}

	private void validateData(Sensor sensor) throws validateDataException {

		if (sensor == null) {
			throw new validateDataException("Sensor is null");

		}  else if(sensor.getId() == null || sensor.getId().equals("")){
			throw new validateDataException("invalid sensor id");
		}		else if (sensor.getRoomName() == null
				|| sensor.getRoomName().equals("")) {
			throw new validateDataException("invalid room");
		} else if (sensor.getSensorType() == null) {
			throw new validateDataException("Sensor Type is null");
		}
	}

	public Sensor saveSensor(Sensor sensor) throws DAOException,
			validateDataException {

		validateData(sensor);
		return sensorDao.save(sensor);

	}

	public Sensor updateSensor(Sensor sensor) throws validateDataException,
			DAOException {
		validateData(sensor);

		validateData(sensor);
		
		return sensorDao.update(sensor);

	}

	public Sensor findSensor(String sensorId) throws DAOException {
		return sensorDao.findById( sensorId);
	}

	public List<Sensor> findSensors() throws DAOException {
		return sensorDao.findAll();
	}

	public void deleteSensor(String sensorId) throws DAOException,
			validateDataException {
		
		sensorDao.delete(sensorId);
	}
	
	public List<Sensor> findSensorsByRoom(String roomId) throws DAOException{
		
		return sensorDao.findSensorByRoom(roomId);
	}

}
