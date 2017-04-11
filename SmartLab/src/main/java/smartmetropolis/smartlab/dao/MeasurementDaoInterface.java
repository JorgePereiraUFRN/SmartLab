package smartmetropolis.smartlab.dao;

import java.util.Date;
import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Measurement;


public interface MeasurementDaoInterface extends GenericDaoInterface<Measurement, Long>{

	List<Measurement> listMeasurementsByDate(Date date) throws DAOException;
	
	List<Measurement> listMeasurementsBetweendDate(Date initialDate, Date finalDate, String sensorId) throws DAOException;
}
