package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.HibernateDAOFactory;
import smartmetropolis.smartlab.dao.MeasurementDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Measurement;

public class MeasurementController {

	private final DAOFactory factory = new HibernateDAOFactory();
	private final MeasurementDaoInterface measurementDao;
	private static final MeasurementController MEASURAMENT_CONTROLLER = new MeasurementController();

	private MeasurementController() {
		measurementDao = factory.getMeasurementDao();
	}

	public synchronized static MeasurementController getInstance() {
		return MEASURAMENT_CONTROLLER;
	}

	private void validateData(Measurement measurement)
			throws validateDataException {

		if (measurement == null) {
			throw new validateDataException("Measurement is null");
		}
		else if (measurement.getValue() == null || measurement.getValue().equals("")) {
			throw new validateDataException(
					"Measurement value is null or empity");
		}
		else if (measurement.getSensor() == null
				|| measurement.getSensor().getSensorId() == null
				|| measurement.getSensor().getSensorId() < 0) {
			throw new validateDataException("Measurement sensorId is invalidy");
		}

	}
	
	public Measurement saveMeasurement(Measurement measurement) throws validateDataException, DAOException{
		validateData(measurement);
		return measurementDao.save(measurement);
		
	}
	
	public void deleteMeasurement(long measurementId) throws validateDataException, DAOException{
		
		Measurement m = measurementDao.findById(Measurement.class, measurementId);
		if(m == null){
			throw new validateDataException("Invalid id");
		}
		
	
		measurementDao.delete(m);
	}
	

	
}
