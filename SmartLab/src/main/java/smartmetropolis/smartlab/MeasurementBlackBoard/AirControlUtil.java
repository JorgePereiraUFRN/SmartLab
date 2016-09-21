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

import smartmetropolis.smartlab.controller.AirConditionerStateController;
import smartmetropolis.smartlab.controller.MeasurementController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.AirConditionerAction;
import smartmetropolis.smartlab.model.AirConditionerState;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.SensorType;

public class AirControlUtil implements AirControl {

	private static final AirControlUtil AIR_CONTROL_UTIL = new AirControlUtil();
	private static final Calendar calendar = Calendar.getInstance();
	private static final AirConditionerStateController AIR_STATE_CONTROLLER = AirConditionerStateController.getInstance();
	private Map<Room, Date> lastAirChange = new HashMap<Room, Date>();
	
	private Logger logger = Logger.getLogger(AirControlUtil.class);
	
	private static Client webServiceClient;


	private AirControlUtil() {

	}

	public static synchronized AirControlUtil getInstance() {
		return AIR_CONTROL_UTIL;
	}

	/* (non-Javadoc)
	 * @see smartmetropolis.smartlab.MeasurementBlackBoard.AirControl#hasPeopleInTheRoom(smartmetropolis.smartlab.model.RoomKey, int)
	 */
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

	/* (non-Javadoc)
	 * @see smartmetropolis.smartlab.MeasurementBlackBoard.AirControl#timeFromLastAirChange(smartmetropolis.smartlab.model.Room)
	 */
	public Long timeFromLastAirChange(Room room) {

		
		if (lastAirChange.get(room) == null) {
			
			calendar.setTimeInMillis(System.currentTimeMillis());
			Date dt = calendar.getTime();
			lastAirChange.put(room, dt);

			// na primeira vez q esse metodo for chamado o tempo retornado será
			// de 100 min
			return 6000L;
		} else {

			// tempo em minutos
			calendar.setTimeInMillis(System.currentTimeMillis());
			long duration = calendar.getTime().getTime()
					- lastAirChange.get(room).getTime();

			long result = TimeUnit.MILLISECONDS.toSeconds(duration);
			return result;

		}

	}

	private void turOnAirConditioner(AirConditioner airConditioner) throws DAOException, validateDataException {
		
		logger.info("acionando aparelho de ar-condicionado: "+airConditioner);
		
		sendCommandToAirConditionerControl(airConditioner, "tur-on");
		
		AirConditionerState airState = new AirConditionerState();
		airState.setAction(AirConditionerAction.ligar);
		airState.setAirConditioner(airConditioner);
		airState.setTimestamp(new Date(System.currentTimeMillis()));
		
		AIR_STATE_CONTROLLER.save(airState);
	}

	private void turOffAirConditioner(AirConditioner airConditioner) throws DAOException, validateDataException {
		
		logger.info("desligando aparelho de ar-condicionado: "+airConditioner);
		sendCommandToAirConditionerControl(airConditioner, "tur-off");
		
		AirConditionerState airState = new AirConditionerState();
		airState.setAction(AirConditionerAction.desligar);
		airState.setAirConditioner(airConditioner);
		airState.setTimestamp(new Date(System.currentTimeMillis()));
		
		AIR_STATE_CONTROLLER.save(airState);

	}

	private void increaseTemperature(AirConditioner airConditioner)
			throws DAOException, validateDataException {
		logger.info("aumentando temperatura: "+airConditioner);
		
		sendCommandToAirConditionerControl(airConditioner, "increase-temp");
		
		AirConditionerState airState = new AirConditionerState();
		airState.setAction(AirConditionerAction.aumentar_temperatura);
		airState.setAirConditioner(airConditioner);
		airState.setTimestamp(new Date(System.currentTimeMillis()));
		
		AIR_STATE_CONTROLLER.save(airState);
		
	}

	private void decreaseTemperature(AirConditioner airConditioner) throws DAOException, validateDataException {
		logger.info("diminuindo temperatura: "+airConditioner);
		sendCommandToAirConditionerControl(airConditioner, "decrease-temp");
		
		AirConditionerState airState = new AirConditionerState();
		airState.setAction(AirConditionerAction.diminuir_temperatura);
		airState.setAirConditioner(airConditioner);
		airState.setTimestamp(new Date(System.currentTimeMillis()));
		
		AIR_STATE_CONTROLLER.save(airState);
	}
	
	
	private void updateTimeFromLastAirChange(Room room){
		lastAirChange.remove(room);
		calendar.setTimeInMillis(System.currentTimeMillis());
		Date dt = calendar.getTime();
		lastAirChange.put(room, dt);
	}
	

	/* (non-Javadoc)
	 * @see smartmetropolis.smartlab.MeasurementBlackBoard.AirControl#turOffAllAirCofRomm(smartmetropolis.smartlab.model.Room)
	 */
	public void turOffAllAirCofRomm(Room room) throws DAOException, validateDataException {
		logger.info("desligando todos os aprelhos da sala: "
				+ room);
		for (AirConditioner airC : room.getAirConditioners()) {
			turOffAirConditioner(airC);
		}
	
		updateTimeFromLastAirChange(room);
		

	}

	/* (non-Javadoc)
	 * @see smartmetropolis.smartlab.MeasurementBlackBoard.AirControl#increaseTemperatureAllAirCofRoom(smartmetropolis.smartlab.model.Room)
	 */
	public void increaseTemperatureAllAirCofRoom(Room room) throws DAOException, validateDataException {
		logger.info("aumentando temperatura de todos os aprelhos da sala: "
						+ room);
		for (AirConditioner airC : room.getAirConditioners()) {
			increaseTemperature(airC);
		}

		updateTimeFromLastAirChange(room);
	}

	/* (non-Javadoc)
	 * @see smartmetropolis.smartlab.MeasurementBlackBoard.AirControl#turOnAllAirCofRoomr(smartmetropolis.smartlab.model.Room)
	 */
	public void turOnAllAirCofRoomr(Room room) throws DAOException, validateDataException {

		logger.info("ligando todos os aprelhos da sala: "
				+ room);

		for (AirConditioner airC : room.getAirConditioners()) {
			turOnAirConditioner(airC);
		}

		updateTimeFromLastAirChange(room);
	}

	/* (non-Javadoc)
	 * @see smartmetropolis.smartlab.MeasurementBlackBoard.AirControl#decreaseTemperatureAllAirCofRoom(smartmetropolis.smartlab.model.Room)
	 */
	public void decreaseTemperatureAllAirCofRoom(Room room) throws DAOException, validateDataException {
		logger.info("diminuindo temperatura de todos os aprelhos da sala: "
						+ room);
		for (AirConditioner airC : room.getAirConditioners()) {
			decreaseTemperature(airC);
		}
		
		updateTimeFromLastAirChange(room);

	}

	private void sendCommandToAirConditionerControl(
			AirConditioner airConditioner, String command) {
		try {
			/*webServiceClient = Client.create();

			String uri = "http://" + airConditioner.getIpaddressAirControl()
					+ ":8080/WS-AirConditioner/air-conditioner/";
			WebResource resource = webServiceClient.resource(uri + command);

			resource.put();*/
		} catch (Exception e) {
			logger.error("erro ao enviar requisição para o controlador do condicionado: "
							+ e.getMessage());
		}
	}

}
