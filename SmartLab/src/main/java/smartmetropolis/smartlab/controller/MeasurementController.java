package smartmetropolis.smartlab.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import smartmetropolis.smartlab.MeasurementBlackBoard.MeasurementTreater;
import smartmetropolis.smartlab.MeasurementBlackBoard.PresenceMeasurementTreater;
import smartmetropolis.smartlab.MeasurementBlackBoard.TemperatureMeasurementTreater;
import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.HibernateDAOFactory;
import smartmetropolis.smartlab.dao.MeasurementDaoInterface;
import smartmetropolis.smartlab.dao.SensorDao;
import smartmetropolis.smartlab.dao.SensorDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public class MeasurementController {

	private final DAOFactory factory = new HibernateDAOFactory();
	private final MeasurementDaoInterface measurementDao;
	private final SensorDaoInterface sensorDao;
	private static final MeasurementController MEASURAMENT_CONTROLLER = new MeasurementController();

	private MeasurementTreater measurementTreater;

	private MeasurementController() {
		measurementDao = factory.getMeasurementDao();
		sensorDao = factory.getSensorDao();

		measurementTreater = new TemperatureMeasurementTreater();
		PresenceMeasurementTreater presenTreater = new PresenceMeasurementTreater();
		measurementTreater.setNext(presenTreater);
	}

	public synchronized static MeasurementController getInstance() {
		return MEASURAMENT_CONTROLLER;
	}

	private void validateData(Measurement measurement)
			throws validateDataException, DAOException {

		if (measurement == null) {
			throw new validateDataException("Measurement is null");
		} else if (measurement.getValue() == null
				|| measurement.getValue().equals("")) {
			throw new validateDataException(
					"Measurement value is null or empty");
		} else if (measurement.getSensor() == null
				|| measurement.getSensor().getId() == null
				|| measurement.getSensor().getId() < 0) {

			throw new validateDataException("Measurement sensorId is invalidy");
		}

		Sensor s = sensorDao.findById(Sensor.class, measurement.getSensor()
				.getId());

		if (s == null) {
			throw new validateDataException("Measurement sensorId is invalidy");
		}

	}

	public Measurement saveMeasurement(Measurement measurement)
			throws validateDataException, DAOException {
		validateData(measurement);

		Measurement m = measurementDao.save(measurement);

		try {
			measurementTreater.treaterMeasurement(m);
		} catch (TreaterException e) {
			System.out.println("nao foi possivel tratar a medicao: "
					+ e.getMessage());
		}

		return m;

	}

	public void deleteMeasurement(long measurementId)
			throws validateDataException, DAOException {

		Measurement m = measurementDao.findById(Measurement.class,
				measurementId);
		if (m == null) {
			throw new validateDataException("Invalid id");
		}

		measurementDao.delete(m);
	}

	public List<Measurement> findMeasurementByDate(Date initialDate)
			throws DAOException {

		return measurementDao.listMeasurementsByDate(initialDate);
	}

	public List<Measurement> findMeasurementByDateAndRoomAndSensorType(
			Date intitalDate, RoomKey roomKey, SensorType sensorType)
			throws DAOException {

		List<Measurement> measurements = measurementDao
				.listMeasurementsByDate(intitalDate);
		List<Measurement> aux = new ArrayList<Measurement>();

		for (Measurement m : measurements) {
			if (m.getSensor().getSensorType() == sensorType
					&& m.getSensor().getRoom().getRoomName()
							.equals(roomKey.getRoomName())
					&& m.getSensor().getRoom().getLocal().getLocalName()
							.equals(roomKey.getLocalName())) {

				aux.add(m);
			}
		}

		return aux;
	}

	public List<Measurement> listMeasurementsBySensorAndDate(Date initialDate,
			Date finalDate, Long sensorId) throws DAOException {

		List<Measurement> measurements = measurementDao
				.listMeasurementsBetweendDate(initialDate, finalDate);

		List<Measurement> aux = new ArrayList<Measurement>();

		for (Measurement m : measurements) {
			//a operação m.getSensor().getId() == sensorId não esta funcionando :/
			if (m.getSensor().getId() - sensorId == 0) {
				aux.add(m);
			}
		}

		
		return aux;
	}
}
