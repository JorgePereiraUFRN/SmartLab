package smatmetropolis.smartLab.controllerTest;


import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.MeasurementController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public class MeasurementControllerTest {

	private static Measurement measurement;
	private static MeasurementController measurementController;
	private static SensorController sensorController;
	private static LocalController localController;
	private static RoomController roomController;
	private static Sensor sensor1;
	private static Local local;
	private static Room room;
	
	@BeforeClass
	public static void init() throws DAOException, validateDataException{
		
		measurementController = MeasurementController.getInstance();
		sensorController = SensorController.getInstance();
		localController = LocalController.getInstance();
		roomController = RoomController.getInstance();
		
		local =  new Local();
		
		local.setLocalName("IMD");
		local.setLatitude(40.5561462);
		local.setLongitude(-5.672383);
		
		local = localController.saveLocal(local);
		
		room = new Room();
		room.setLocal(local);
		room.setRoomName("B206");
		
		room = roomController.saveRoom(room);
		
		sensor1 = new Sensor();
		sensor1.setDescription("descricao sensor");
		sensor1.setRoom(room);
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
		measurementController.deleteMeasurement(measurement.getId());
		sensorController.deleteSensor(sensor1.getId());
		roomController.deleteRoom(room.getRoomName(), local.getLocalName());
		localController.deleteLocal(local.getLocalName());
	}
	

}
