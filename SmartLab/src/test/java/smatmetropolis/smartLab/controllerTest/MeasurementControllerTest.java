package smatmetropolis.smartLab.controllerTest;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import smartmetropolis.smartlab.controller.MeasurementController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public class MeasurementControllerTest {

	private static Measurement measurement;
	private static MeasurementController measurementController;
	private static SensorController sensorController;
	private static Sensor sensor1;
	
	@BeforeClass
	public static void init() throws DAOException, validateDataException{
		
		measurementController = MeasurementController.getInstance();
		sensorController = SensorController.getInstance();
		
		sensor1 = new Sensor();
		sensor1.setDescription("descricao sensor");
		sensor1.setLocal("local sensor");
		sensor1.setSensorType(SensorType.PRESENCE);
		
		sensor1 = sensorController.saveSensor(sensor1);
		
		measurement = new Measurement();
		
		measurement.setSensor(sensor1);
		measurement.setTime(new Date(System.currentTimeMillis()));
		measurement.setValue("true");	
		
		measurement = measurementController.saveMeasurement(measurement);
		
		
	}
	
	
	@Test (expected = validateDataException.class)
	public void saveMeasurementInvalidSensor() throws validateDataException, DAOException{
		
		Measurement measurement = new Measurement();
		measurement.setValue("true");
		measurement.setTime(new Date(System.currentTimeMillis()));
		
		measurementController.saveMeasurement(measurement);
	}
	
	@Test (expected = validateDataException.class)
	public void saveMeasurementInvalidValue() throws validateDataException, DAOException{
		
		Measurement measurement = new Measurement();
		measurement.setSensor(sensor1);
		measurement.setTime(new Date(System.currentTimeMillis()));
		measurementController.saveMeasurement(measurement);
	}
	
	
	
	@AfterClass
	public static void after() throws DAOException, validateDataException{
		//deleta o sensor e as medições associadas a ele
		//measurementController.deleteMeasurement(measurement.getMeasurementId());
		sensorController.deleteSensor(sensor1.getSensorId());
	}
	

}
