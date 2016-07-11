package smatmetropolis.smartLab.controllerTest;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public class SensorControllerTest {

	private static SensorController sensorController;
	private static LocalController localController;
	private static RoomController roomController;
	private static Sensor sensor;
	private static Local local;
	private static Room room;

	@BeforeClass
	public static void init() throws DAOException, validateDataException {

		sensorController = SensorController.getInstance();
		localController = LocalController.getInstance();
		roomController = RoomController.getInstance();

		local = new Local();

		local.setName("IMD");
		local.setLatitude(40.5561462);
		local.setLongitude(-5.672383);

		local = localController.saveLocal(local);

		room = new Room();
		room.setLocal(local);
		room.setName("B206");

		room = roomController.saveRoom(room);

		sensor = new Sensor();
		sensor.setRoom(room);
		sensor.setDescription("descrição sensor");
		sensor.setSensorType(SensorType.HUMIDITY);

		sensor = sensorController.saveSensor(sensor);

	}

	@Test(expected = validateDataException.class)
	public void saveSensorInvalidLocalTest() throws DAOException,
			validateDataException {

		Sensor sensor1 = new Sensor();

		sensor1.setSensorType(SensorType.PRESENCE);

		sensorController.saveSensor(sensor1);
	}

	@Test(expected = validateDataException.class)
	public void saveSensorInvalidTypeTest() throws DAOException,
			validateDataException {

		Sensor sensor1 = new Sensor();

		sensor1.setRoom(room);

		sensorController.saveSensor(sensor1);
	}

	@Test
	public void searchSensorTest() throws DAOException {

		Sensor s = sensorController.findSensor(sensor.getId());

		Assert.assertEquals(s, sensor);
	}

	@AfterClass
	public static void after() throws DAOException, validateDataException {

		sensorController.deleteSensor(sensor.getId());
		roomController.deleteRoom(room.getId());
		localController.deleteLocal(local.getId());
	}

}
