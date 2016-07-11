package smatmetropolis.smartLab.websericeTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MeasurementResourceTest {

	public static Sensor sensor;
	private static Local local;
	private static Room room;
	private static Client webServiceClient;
	private static String uri = "http://localhost:8080/SmartLab/ws/measurement/";
	private static AtomicLong id;
	
	private static LocalController localController;
	private static RoomController roomController;

	@BeforeClass
	public static void init() throws validateDataException, DAOException {
		
		localController = LocalController.getInstance();
		roomController = RoomController.getInstance();
		
		sensor = new Sensor();

		local = new Local();

		local.setName("IMD");
		local.setLatitude(40.5561462);
		local.setLongitude(-5.672383);

		local = localController.saveLocal(local);

		room = new Room();
		room.setLocal(local);
		room.setName("B206");

		room = roomController.saveRoom(room);

		sensor.setDescription("descricao sensor");
		sensor.setRoom(room);
		sensor.setSensorType(SensorType.HUMIDITY);

		webServiceClient = Client.create();

		WebResource resource = webServiceClient
				.resource("http://localhost:8080/SmartLab/ws/sensor/");

		ClientResponse response = resource.type(MediaType.APPLICATION_XML)
				.post(ClientResponse.class, sensor);

		sensor = response.getEntity(Sensor.class);
		id = new AtomicLong(sensor.getId());
	}

	@Test
	public void saveMeasurement() {

		webServiceClient = Client.create();
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = new SimpleDateFormat("dd-MM-yy*HH:mm:ss");
		String dt = df.format(date);
		System.out.println(dt);
		WebResource resource = webServiceClient.resource(uri + "?sensor=" + id
				+ "&value=50&dateTime=" + dt);

		ClientResponse response = resource.post(ClientResponse.class);

		Assert.assertEquals(200, response.getStatus());

	}

	@Test
	public void saveMeasurementWithoutTime() {

		webServiceClient = Client.create();

		WebResource resource = webServiceClient.resource(uri + "?sensor=" + id
				+ "&value=50");

		ClientResponse response = resource.post(ClientResponse.class);

		Assert.assertEquals(200, response.getStatus());

	}

	@AfterClass
	public static void after() throws DAOException {
		WebResource resource = webServiceClient
				.resource("http://localhost:8080/SmartLab/ws/sensor/"
						+ id.get());

		resource.delete();
		
/*		roomController.deleteRoom(room.getId());
		localController.deleteLocal(local.getId())*/;
	}

}
