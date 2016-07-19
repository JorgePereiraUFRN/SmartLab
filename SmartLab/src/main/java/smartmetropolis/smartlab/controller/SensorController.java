package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.HibernateDAOFactory;
import smartmetropolis.smartlab.dao.MeasurementDaoInterface;
import smartmetropolis.smartlab.dao.SensorDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Sensor;

public class SensorController {

	private final DAOFactory factory = new HibernateDAOFactory();
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

		} else if (sensor.getRoom() == null
				|| sensor.getRoom().getRoomName() == null
				|| sensor.getRoom().getLocal() == null
				|| sensor.getRoom().getLocal().getLocalName() == null) {
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

		if (sensor.getId() == null || sensor.getId() < 0) {
			throw new validateDataException("invalid Id: " + sensor.getId());
		}
		return sensorDao.update(sensor);

	}

	public Sensor findSensor(Long sensorId) throws DAOException {
		return sensorDao.findById(Sensor.class, sensorId);
	}

	public List<Sensor> findSensors() throws DAOException {
		return sensorDao.findAll(Sensor.class);
	}

	public void deleteSensor(long sensorId) throws DAOException,
			validateDataException {
		Sensor sensor = sensorDao.findById(Sensor.class, sensorId);
		if (sensor == null) {
			throw new validateDataException("invalid Id");
		}
		sensorDao.delete(sensor);
	}

}
