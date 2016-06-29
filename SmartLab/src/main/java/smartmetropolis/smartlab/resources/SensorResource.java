package smartmetropolis.smartlab.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Sensor;

@Path("Sensor")
public class SensorResource {

	private final SensorController sensorController;
	
	public SensorResource() {
		sensorController = SensorController.getInstance();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public void saveSensor(Sensor sensor) throws DAOException, validateDataException{
		sensorController.saveSensor(sensor);
		
	}
	
	@GET
	@Path("/{sensorId}")
	@Produces(MediaType.APPLICATION_XML)
	public Sensor getSensor(@PathParam("sensorId") long sensorId) throws DAOException{
		
		return sensorController.findSensor(sensorId);
	}
	
	
}
