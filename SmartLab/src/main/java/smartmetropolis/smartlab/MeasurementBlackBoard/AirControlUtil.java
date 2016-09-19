package smartmetropolis.smartlab.MeasurementBlackBoard;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import smartmetropolis.smartlab.controller.MeasurementController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.SensorType;

public class AirControlUtil {

	private static final AirControlUtil AIR_CONTROL_UTIL =  new AirControlUtil();
	private static final Calendar calendar = Calendar.getInstance();

	private static Client webServiceClient;
	private static String uri = "http://10.7.172.254:8080/WS-AirConditioner/air-conditioner/";

	private static volatile Date lastAirChange = null;

	private AirControlUtil() {
		
	}

	public static synchronized AirControlUtil getInstance() {
		return AIR_CONTROL_UTIL;
	}

	public boolean hasPeopleInTheRoom(RoomKey roomKey, int minutes)
			throws TreaterException {
		try {

			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.MINUTE, (minutes * -1));

			MeasurementController mController = MeasurementController
					.getInstance();

			List<Measurement> measurements = mController
					.findMeasurementByDateAndRoomAndSensorType(
							calendar.getTime(), roomKey, SensorType.PRESENCE);

			for (Measurement m : measurements) {
				
				try {
					boolean b = Boolean.parseBoolean(m.getValue());
					// caso alguma das medições indique q existe pessoa na sala
					// retornar verdadeiro
					if (b) {
						return true;
					}

				} catch (Exception e) {
					// erro ao fazer conversao de booleano
				}
			}

		} catch (DAOException e) {
			e.printStackTrace();
			throw new TreaterException(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			throw new TreaterException(e.getMessage());
		}

		return false;

	}

	public Long timeFromLastAirChange() {

		if (lastAirChange == null) {
			calendar.setTimeInMillis(System.currentTimeMillis());
			lastAirChange = calendar.getTime();

			// na primeira vez q esse metodo for chamado o tempo retornado será
			// de 100 min
			return 6000L;
		} else {

			// tempo em minutos
			calendar.setTimeInMillis(System.currentTimeMillis());
			long duration = calendar.getTime().getTime()
					- lastAirChange.getTime();

			long result = TimeUnit.MILLISECONDS.toSeconds(duration);
			return result;

		}

	}

	public void turOnAirConditioner() {
		try {
			webServiceClient = Client.create();
			WebResource resource = webServiceClient.resource(uri + "tur-on");

			resource.put();

			System.out.println("ligando ar");
			lastAirChange = calendar.getTime();
		} catch (Exception e) {
			System.out
					.println("erro ao enviar requisição (ligar) para o controlador do condicionado: "
							+ e.getMessage());
		}
	}

	public void turOffAirConditioner() {
		try {
			webServiceClient = Client.create();
			WebResource resource = webServiceClient.resource(uri + "tur-off");

			resource.put();

			System.out.println("desligando ar");
			lastAirChange = calendar.getTime();
		} catch (Exception e) {
			System.out
					.println("erro ao enviar requisição  (desligar) para o controlador do condicionado: "
							+ e.getMessage());
		}
	}

	public void increaseTemperature() {
		try {
			webServiceClient = Client.create();
			WebResource resource = webServiceClient.resource(uri
					+ "increase-temp");

			resource.put();

			System.out.println("aumentando temperatura");
			lastAirChange = calendar.getTime();
		} catch (Exception e) {
			System.out
					.println("erro ao enviar requisição (aumentar temp) para o controlador do condicionado: "
							+ e.getMessage());
		}
	}

	public void decreaseTemperature() {
		try {
			webServiceClient = Client.create();

			WebResource resource = webServiceClient.resource(uri
					+ "decrease-temp");

			resource.put();

			System.out.println("diminuindo temperatura");
			lastAirChange = calendar.getTime();
		} catch (Exception e) {
			System.out
					.println("erro ao enviar requisição (diminuir temp) para o controlador do condicionado: "
							+ e.getMessage());
		}
	}

}
