package smartmetropolis.smartlab.MeasurementBlackBoard;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import smartmetropolis.smartlab.controller.AirConditionerController;
import smartmetropolis.smartlab.controller.AirConditionerStateController;
import smartmetropolis.smartlab.controller.MeasurementController;
import smartmetropolis.smartlab.exceptions.ComunicationException;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.UnavailableDataException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.AirConditionerAction;
import smartmetropolis.smartlab.model.AirConditionerState;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.SensorType;

public class AirControl implements AirControlInterface {

	private static final AirControl AIR_CONTROL_UTIL = new AirControl();
	private static final Calendar calendar = Calendar.getInstance();
	private static final AirConditionerStateController AIR_STATE_CONTROLLER = AirConditionerStateController
			.getInstance();
	private static final AirConditionerController AIR_CONTROLLER = AirConditionerController
			.getInstance();
	private Map<String, Date> lastAirChange = new HashMap<String, Date>();

	private Map<String, Float> atualTemp = new HashMap<String, Float>();

	private Logger logger = Logger.getLogger(AirControl.class);

	private static Client webServiceClient;

	private AirControl() {

	}

	public static synchronized AirControl getInstance() {
		return AIR_CONTROL_UTIL;
	}

	public boolean hasPeopleInTheRoom(String room, int minutes)
			throws TreaterException {
		try {

			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.MINUTE, (minutes * -1));

			MeasurementController mController = MeasurementController
					.getInstance();

			List<Measurement> measurements = mController
					.findMeasurementByDateAndRoomAndSensorType(
							calendar.getTime(), room,
							SensorType.PRESENCE);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see smartmetropolis.smartlab.MeasurementBlackBoard.AirControl#
	 * timeFromLastAirChange(smartmetropolis.smartlab.model.Room)
	 */
	public Long timeFromLastAirChange(String roomId) {

		if (lastAirChange.get(roomId) == null) {

			calendar.setTimeInMillis(System.currentTimeMillis());
			Date dt = calendar.getTime();
			lastAirChange.put(roomId, dt);

			// na primeira vez q esse metodo for chamado o tempo retornado será
			// de 100 min
			return 6000L;
		} else {

			// tempo em minutos
			calendar.setTimeInMillis(System.currentTimeMillis());
			long duration = calendar.getTime().getTime()
					- lastAirChange.get(roomId).getTime();

			long result = TimeUnit.MILLISECONDS.toSeconds(duration);
			return result;

		}

	}

	private void turOnAirConditioner(AirConditioner airConditioner)
			throws DAOException, validateDataException {

		logger.info("acionando aparelho de ar-condicionado: " + airConditioner);

		try {
			sendCommandToAirConditionerControl(airConditioner, "turn-on/"
					+ targetTemperature);

			airConditioner.setItsOn(true);
			AirConditionerState airState = new AirConditionerState();
			airState.setAction(AirConditionerAction.ligar);
			airState.setAirConditionerId(airConditioner.getId());
			airState.setTimestamp(new Date(System.currentTimeMillis()));

			AIR_CONTROLLER.updateAirConditioner(airConditioner);
			AIR_STATE_CONTROLLER.save(airState);
		} catch (ComunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void turOffAirConditioner(AirConditioner airConditioner)
			throws DAOException, validateDataException {

		logger.info("desligando aparelho de ar-condicionado: " + airConditioner);
		try {
			sendCommandToAirConditionerControl(airConditioner, "turn-off");

			if (airConditioner.itsOn) {
				airConditioner.setItsOn(false);
				AirConditionerState airState = new AirConditionerState();
				airState.setAction(AirConditionerAction.desligar);
				airState.setAirConditionerId(airConditioner.getId());
				airState.setTimestamp(new Date(System.currentTimeMillis()));

				AIR_CONTROLLER.updateAirConditioner(airConditioner);

				AIR_STATE_CONTROLLER.save(airState);
			}

		} catch (ComunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void increaseTemperature(AirConditioner airConditioner)
			throws DAOException, validateDataException {
		logger.info("aumentando temperatura: " + airConditioner);

		try {
			sendCommandToAirConditionerControl(airConditioner, "turn-on/"
					+ targetTemperature);
			sendCommandToAirConditionerControl(airConditioner, "increase-temp/"
					+ targetTemperature);

			if (airConditioner.itsOn) {
				AirConditionerState airState = new AirConditionerState();
				airState.setAction(AirConditionerAction.aumentar_temperatura);
				airState.setAirConditionerId(airConditioner.getId());
				airState.setTimestamp(new Date(System.currentTimeMillis()));
				AIR_STATE_CONTROLLER.save(airState);
			}

		} catch (ComunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void decreaseTemperature(AirConditioner airConditioner)
			throws DAOException, validateDataException {
		logger.info("diminuindo temperatura: " + airConditioner);
		try {

			sendCommandToAirConditionerControl(airConditioner, "decrease-temp/"
					+ targetTemperature);
			sendCommandToAirConditionerControl(airConditioner, "turn-on/"
					+ targetTemperature);

			if (airConditioner.itsOn) {
				AirConditionerState airState = new AirConditionerState();
				airState.setAction(AirConditionerAction.diminuir_temperatura);
				airState.setAirConditionerId(airConditioner.getId());
				airState.setTimestamp(new Date(System.currentTimeMillis()));

				AIR_STATE_CONTROLLER.save(airState);
			}
		} catch (ComunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void updateTimeFromLastAirChange(String roomId) {
		lastAirChange.remove(roomId);
		calendar.setTimeInMillis(System.currentTimeMillis());
		Date dt = calendar.getTime();
		lastAirChange.put(roomId, dt);
	}

	public void turOffAllAirConditionersOfRom(String room) throws DAOException,
			validateDataException {
		logger.info("desligando todos os aprelhos da sala: " + room);
		for (AirConditioner airC : AIR_CONTROLLER.findAirconditionerByRoom(room)) {
			turOffAirConditioner(airC);
		}

		// updateTimeFromLastAirChange(room);

	}

	public void increaseTemperatureAllAirConditionersOfRoom(String roomId)
			throws DAOException, validateDataException {
		logger.info("aumentando temperatura de todos os aprelhos da sala: "
				+ roomId);
		for (AirConditioner airC : AIR_CONTROLLER.findAirconditionerByRoom(roomId)) {
			increaseTemperature(airC);
		}

		updateTimeFromLastAirChange(roomId);
	}

	public void turOnAllAirCoditionerOfRoom(String room) throws DAOException,
			validateDataException {

		logger.info("ligando todos os aprelhos da sala: " + room);

		for (AirConditioner airC : AIR_CONTROLLER.findAirconditionerByRoom(room
				)) {
			turOnAirConditioner(airC);
		}

		// updateTimeFromLastAirChange(room);
	}

	public void decreaseTemperatureAllAirConditionersOfRoom(String roomId)
			throws DAOException, validateDataException {
		logger.info("diminuindo temperatura de todos os aprelhos da sala: "
				+ roomId);
		for (AirConditioner airC : AIR_CONTROLLER.findAirconditionerByRoom(roomId)) {
			decreaseTemperature(airC);
		}

		updateTimeFromLastAirChange(roomId);

	}

	private void sendCommandToAirConditionerControl(
			AirConditioner airConditioner, String command)
			throws ComunicationException {
		try {
			/*
			webServiceClient = Client.create();

			String uri = "http://" + airConditioner.getIpAddressAirControl()
					+ ":8080/WS-AirConditioner/air-conditioner/";
			WebResource resource = webServiceClient.resource(uri + command);

			logger.info("enviando requisição: " + uri + command);
			resource.put();
			*/
			
		} catch (Exception e) {
			logger.error("erro ao enviar requisição para o controlador do condicionado: "
					+ e.getMessage());

			throw new ComunicationException(
					"erro ao enviar requisição para o controlador do condicionado");
		}
	}

	public Float getAtualTemperature(String roomId) throws UnavailableDataException {
		
		Float temp = atualTemp.get(roomId);

		if(temp == null){
			throw new UnavailableDataException(
					"erro ao consultar temperatura da sala " + roomId);
		}
		
		return temp;
	}

	public void setAtualTemp(String roomId, Float temp) {

		if (atualTemp.get(roomId) == null) {
			atualTemp.put(roomId, temp);
			System.out.println("setando temp: "+temp+" "+roomId);
		} else {
			atualTemp.remove(roomId);
			atualTemp.put(roomId, temp);
		}

	}

	

}
