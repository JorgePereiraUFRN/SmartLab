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
import smartmetropolis.smartlab.dao.SensorDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Measurement;
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
		} else if (measurement.getSensorId() == null
				|| measurement.getSensorId().equals("")) {

			throw new validateDataException("Measurement sensorId is invalidy");
		}

		Sensor s = sensorDao.findById(measurement.getSensorId());

		if (s == null) {
			throw new validateDataException("Measurement sensorId is invalidy");
		}

		if (s.getSensorType() == SensorType.TEMPERATURE) {
			try {
				Float.parseFloat(measurement.getValue());
			} catch (Exception e) {
				throw new validateDataException("Invalid data: deve ser float");

			}
		}

		else if (s.getSensorType() == SensorType.PRESENCE) {

			if (!measurement.getValue().equals("true")
					&& !measurement.getValue().equals("TRUE")
					&& !measurement.getValue().equals("false")
					&& !measurement.getValue().equals("FALSE")) {
				throw new validateDataException(
						"Invalid data: deve ser booleano (true or false)");

			}
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


		measurementDao.delete(measurementId);
	}

	public List<Measurement> findMeasurementByDate(Date initialDate)
			throws DAOException {

		return measurementDao.listMeasurementsByDate(initialDate);
	}

	public List<Measurement> findMeasurementByDateAndRoomAndSensorType(
			Date intitalDate, String RoomId, SensorType sensorType)
			throws DAOException {

		List<Measurement> measurements = measurementDao
				.listMeasurementsByDate(intitalDate);
		
		List<Sensor> sensors = sensorDao.findSensorByRoomAndType(RoomId, sensorType);
		
		List<Measurement> aux = new ArrayList<Measurement>();

		for (Measurement m : measurements) {
			
			
			for(Sensor s : sensors){
				
				if (m.getSensorId().equals(s.getId())) {

					aux.add(m);
				}
			}
			
			
		}

		return aux;
	}

	public List<Measurement> listMeasurementsBySensorAndDate(Date initialDate,
			Date finalDate, String sensorId) throws DAOException {

		List<Measurement> measurements = measurementDao
				.listMeasurementsBetweendDate(initialDate, finalDate, sensorId);

		List<Measurement> aux = new ArrayList<Measurement>();
		for (Measurement m : measurements) {
			if (m.getSensorId().equals(sensorId)) {
				aux.add(m);
			}
		}

		return aux;
	}
	
	
}
