package smatmetropolis.smartLab.controllerTest;


import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public class SensorControllerTest {
	
	private static SensorController sensorController;
	private static Sensor sensor;

	
	@BeforeClass
	public static void init() throws DAOException, validateDataException{
		
		sensorController = SensorController.getInstance();
		
		sensor = new Sensor();
		sensor.setLocal("A301");
		sensor.setDescription("descrição sensor");
		sensor.setSensorType(SensorType.HUMIDITY);
		
		sensor = sensorController.saveSensor(sensor);
		
	}
	
	@Test(expected = validateDataException.class)
	public void saveSensorInvalidLocalTest() throws DAOException, validateDataException{
		
		Sensor sensor1 = new Sensor();
		
		sensor1.setSensorType(SensorType.PRESENCE);
		
		sensorController.saveSensor(sensor1);
	}
	
	@Test(expected = validateDataException.class)
	public void saveSensorInvalidTypeTest() throws DAOException, validateDataException{
		
		Sensor sensor1 = new Sensor();
		
		sensor1.setLocal("B204");
		
		sensorController.saveSensor(sensor1);
	}
	
	
	@Test
	public void searchSensorTest() throws DAOException{
		
		Sensor s = sensorController.findSensor(sensor.getSensorId());
		
		Assert.assertEquals(s, sensor);
	}
	
	
	
	
	@AfterClass
	public static void after() throws DAOException, validateDataException{
		
		sensorController.deleteSensor(sensor.getSensorId());
	}

}
