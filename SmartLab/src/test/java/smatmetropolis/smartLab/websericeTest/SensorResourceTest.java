package smatmetropolis.smartLab.websericeTest;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public class SensorResourceTest {

	private static Sensor sensor;
	private static Local local;
	private static Room room;
	private static Client webServiceClient;
	private static String uri = "http://localhost:8080/SmartLab/ws/sensor/";
	private static AtomicLong id;

	private static LocalController localController;
	private static RoomController roomController;

	@BeforeClass
	public static void init() throws validateDataException, DAOException {
		localController = LocalController.getInstance();
		roomController = RoomController.getInstance();

		sensor = new Sensor();

		local = new Local();

		local.setLocalName("IMD");
		local.setLatitude(40.5561462);
		local.setLongitude(-5.672383);

		local = localController.saveLocal(local);

		room = new Room();
		room.setLocal(local);
		room.setRoomName("B206");

		room = roomController.saveRoom(room);

		sensor.setDescription("descricao sensor");
		
		Local l = new Local();
		l.setLocalName(local.getLocalName());
		Room r = new Room();
		r.setRoomName(room.getRoomName());
		r.setLocal(l);
		sensor.setRoom(r);
		sensor.setSensorType(SensorType.HUMIDITY);

		webServiceClient = Client.create();

		WebResource resource = webServiceClient.resource(uri);

		ClientResponse response = resource.type(MediaType.APPLICATION_XML)
				.post(ClientResponse.class, sensor);

		sensor = response.getEntity(Sensor.class);
		id = new AtomicLong(sensor.getId());
	}

	@Test
	public void saveSensorInvalidType() {

		sensor = new Sensor();

		sensor.setDescription("descricao sensor");
		sensor.setRoom(room);

		webServiceClient = Client.create();

		WebResource resource = webServiceClient.resource(uri);

		ClientResponse response = resource.type(MediaType.APPLICATION_XML)
				.post(ClientResponse.class, sensor);

		Assert.assertEquals(400, response.getStatus());
	}

	@Test
	public void saveSensorInvalidLocal() {

		sensor = new Sensor();

		sensor.setDescription("descricao sensor");
		sensor.setSensorType(SensorType.HUMIDITY);

		webServiceClient = Client.create();

		WebResource resource = webServiceClient.resource(uri);

		ClientResponse response = resource.type(MediaType.APPLICATION_XML)
				.post(ClientResponse.class, sensor);

		Assert.assertEquals(400, response.getStatus());
	}

	@Test
	public void getSensor() {

		WebResource resource = webServiceClient.resource(uri + id.get());

		ClientResponse response = resource.type(MediaType.APPLICATION_XML).get(
				ClientResponse.class);

		Assert.assertEquals(200, response.getStatus());

	}

	@Test
	public void updateSensor() {

		sensor.setDescription("new description");
		sensor.setRoom(room);
		sensor.setId(id.get());

		WebResource resource = webServiceClient.resource(uri);

		ClientResponse response = resource.type(MediaType.APPLICATION_XML).put(
				ClientResponse.class, sensor);

		Assert.assertEquals(200, response.getStatus());

	}

	//@AfterClass
	public static void after() throws DAOException {
		WebResource resource = webServiceClient.resource(uri + id.get());

		resource.delete();
		/*
		 * roomController.deleteRoom(room.getId());
		 * localController.deleteLocal(local.getId());
		 */

	}

}
