package smartmetropolis.smartlab.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Sensor;

@Path("sensor")
public class SensorResource {

	private final SensorController sensorController;

	public SensorResource() {
		sensorController = SensorController.getInstance();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	// @Produces(MediaType.APPLICATION_XML)
	public Response saveSensor(Sensor sensor) {

		try {
			sensor = sensorController.saveSensor(sensor);
			return Response.ok(sensor, MediaType.APPLICATION_XML).build();

		} catch (DAOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Database error: " + e.getMessage()).build();

		} catch (validateDataException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Invalid data: " + e.getMessage()).build();
		}

	}

	@GET
	@Path("/{sensorId}")
	// @Produces(MediaType.APPLICATION_XML)
	public Response getSensor(@PathParam("sensorId") long sensorId)
			throws DAOException {

		Sensor sensor = sensorController.findSensor(sensorId);

		if (sensor != null) {
			return Response.ok(sensor, MediaType.APPLICATION_XML).build();
		} else {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("The sensor with id: " + sensorId
							+ " doesn't exist").build();
		}
	}

	@DELETE
	@Path("/{sensorId}")
	// @Produces(MediaType.APPLICATION_XML)
	public Response deleteSensor(@PathParam("sensorId") long sensorId) {

		try {

			sensorController.deleteSensor(sensorId);
			return Response.status(Response.Status.OK)
					.entity("Sensor deleted.").build();

		} catch (DAOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Databse error: " + e.getMessage()).build();

		} catch (validateDataException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Invalid data: " + e.getMessage()).build();
		}

	}

	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	// @Produces(MediaType.APPLICATION_XML)
	public Response updateSensor(Sensor sensor) {

		try {
			sensorController.updateSensor(sensor);
			return Response.status(Response.Status.OK)
					.entity("Sensor updated.").build();

		} catch (DAOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Databse error: " + e.getMessage()).build();

		} catch (validateDataException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Invalid data: " + e.getMessage()).build();
		}

	}

}
