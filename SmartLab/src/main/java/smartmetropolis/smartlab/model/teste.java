package smartmetropolis.smartlab.model;

import java.sql.Timestamp;

import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.HibernateDAOFactory;
import smartmetropolis.smartlab.exceptions.DAOException;

public class teste {

	public static void main(String[] args) throws DAOException {
		
		DAOFactory daoFactory = new HibernateDAOFactory();
		
		Sensor sensor = new Sensor();
		Measurement measurement = new Measurement();
		
		//sensor.setAtualMeasurement(measurement);
		sensor.setId(1L);
		sensor.setLocal("A301");
		sensor.setSensorType(SensorType.PRESENCE);
		
		measurement.setSensor(sensor);
		measurement.setTimestamp(new Timestamp(System.currentTimeMillis()));
		measurement.setValue("true");
		
		
		daoFactory.getSensorDao().save(sensor);
		daoFactory.getMeasurementDao().save(measurement);
		

	}

}
