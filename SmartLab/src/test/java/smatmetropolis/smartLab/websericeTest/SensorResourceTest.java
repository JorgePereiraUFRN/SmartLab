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

import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public class SensorResourceTest {

	public static Sensor sensor;
	private static Client webServiceClient;
	private static String uri = "http://localhost:8080/SmartLab/ws/sensor/";
	private static AtomicLong id;

	@BeforeClass
	public static void init() {
		sensor = new Sensor();

		sensor.setDescription("descricao sensor");
		sensor.setLocal("local sensor");
		sensor.setSensorType(SensorType.HUMIDITY);

		webServiceClient = Client.create();

		WebResource resource = webServiceClient.resource(uri);

		ClientResponse response = resource.type(MediaType.APPLICATION_XML)
				.post(ClientResponse.class, sensor);
		
		
		sensor = response.getEntity(Sensor.class);
		id = new AtomicLong(sensor.getSensorId());
	}

	@Test
	public void saveSensorInvalidType() {

		sensor = new Sensor();

		sensor.setDescription("descricao sensor");
		sensor.setLocal("local sensor");

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

		WebResource resource = webServiceClient.resource(uri
				+ id.get());

		ClientResponse response = resource.type(MediaType.APPLICATION_XML).get(
				ClientResponse.class);

		Assert.assertEquals(200, response.getStatus());

	}

	@Test
	public void updateSensor() {

		sensor.setDescription("new description");
		sensor.setLocal("new location");
		sensor.setSensorId(id.get());

		WebResource resource = webServiceClient.resource(uri);

		ClientResponse response = resource.type(MediaType.APPLICATION_XML).put(
				ClientResponse.class, sensor);

		
		
		Assert.assertEquals(200, response.getStatus());

		
	}

	@AfterClass
	public static void after() {
		WebResource resource = webServiceClient.resource(uri
				+ id.get());

		resource.delete();

	}

}
