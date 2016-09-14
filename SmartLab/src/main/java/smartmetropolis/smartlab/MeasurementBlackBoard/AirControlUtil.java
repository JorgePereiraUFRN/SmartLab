package smartmetropolis.smartlab.MeasurementBlackBoard;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import smartmetropolis.smartlab.controller.MeasurementController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.SensorType;

public class AirControlUtil {


	private static final AirControlUtil AIR_CONTROL_UTIL = new AirControlUtil();
	private static final Calendar calendar = Calendar.getInstance();
	
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

	
	public void registerAirChange(){
		lastAirChange = calendar.getTime();
	}
	
	

}
