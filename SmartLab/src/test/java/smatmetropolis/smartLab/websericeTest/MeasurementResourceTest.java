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


import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MeasurementResourceTest {

	public static Sensor sensor;
	private static Client webServiceClient;
	private static String uri = "http://localhost:8080/SmartLab/ws/measurement/";
	private static AtomicLong id;

	@BeforeClass
	public static void init() {
		sensor = new Sensor();

		sensor.setDescription("descricao sensor");
		sensor.setLocal("local sensor");
		sensor.setSensorType(SensorType.HUMIDITY);

		webServiceClient = Client.create();

		WebResource resource = webServiceClient
				.resource("http://localhost:8080/SmartLab/ws/sensor/");

		ClientResponse response = resource.type(MediaType.APPLICATION_XML)
				.post(ClientResponse.class, sensor);

		sensor = response.getEntity(Sensor.class);
		id = new AtomicLong(sensor.getSensorId());
	}

	@Test
	public void saveMeasurement() {

		webServiceClient = Client.create();
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = new SimpleDateFormat("dd-MM-yy*HH:mm:ss");
		String dt = df.format(date);
		System.out.println(dt);
		WebResource resource = webServiceClient.resource(uri + "?sensor=" + id
				+ "&value=50&dateTime=" +dt);

		ClientResponse response = resource
				.post(ClientResponse.class);

		Assert.assertEquals(200, response.getStatus());

	}

	@Test
	public void saveMeasurementWithoutTime() {

		webServiceClient = Client.create();
	
		WebResource resource = webServiceClient.resource(uri + "?sensor=" + id
				+ "&value=50");

		ClientResponse response = resource
				.post(ClientResponse.class);

		Assert.assertEquals(200, response.getStatus());

	}
	
	@AfterClass
	public static void after() {
		WebResource resource = webServiceClient.resource("http://localhost:8080/SmartLab/ws/sensor/" + id.get());

		resource.delete();
	}

}
