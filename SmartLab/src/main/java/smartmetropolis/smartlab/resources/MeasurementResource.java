package smartmetropolis.smartlab.resources;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import smartmetropolis.smartlab.controller.MeasurementController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Sensor;

@Path("measurement")
public class MeasurementResource {

	private final SensorController sensorController;
	private final MeasurementController measurementController;

	public MeasurementResource() {
		sensorController = SensorController.getInstance();
		measurementController = MeasurementController.getInstance();
	}

	@POST
	public Response saveMeasurement(@QueryParam("sensor") long sensorId,
			@QueryParam("value") String value,
			@QueryParam("dateTime") String datetime) {
		try {
			Sensor sensor = sensorController.findSensor(sensorId);

			if (sensor == null) {
				return Response
						.status(Response.Status.BAD_REQUEST)
						.entity("The sensor with id: " + sensorId
								+ " doesn't exist").build();
			} else {
				Measurement measurement = new Measurement();
				measurement.setSensor(sensor);
				measurement.setValue(value);

				Date time;
				if (datetime == null || datetime.equals("")) {
					time = new Date(System.currentTimeMillis());
				} else {			
					DateFormat df = new SimpleDateFormat("dd-MM-yy*HH:mm:ss");
					time = df.parse(datetime);		
				}

				measurement.setTime(time);

				measurementController.saveMeasurement(measurement);

				return Response.status(Response.Status.OK)
						.entity("Measurement saved.").build();
			}

		} catch (DAOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Database error: " + e.getMessage()).build();
		} catch (validateDataException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Invalid value: " + e.getMessage()).build();
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Invalid dateTime value: " + e.getMessage())
					.build();
		}

	}
	
	

}
