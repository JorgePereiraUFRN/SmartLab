package smartmetropolis.smartlab.MeasurementBlackBoard;

import org.apache.log4j.Logger;

import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.SensorType;

public class PresenceMeasurementTreater extends MeasurementTreater {

	private AirControl AIR_CONTROL_UTIL = AirControlUtil.getInstance();
	
	private Logger logger = Logger.getLogger(PresenceMeasurementTreater.class);

	public PresenceMeasurementTreater() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void treaterMeasurement(Measurement measurement)
			throws TreaterException {

		
		
		if (measurement.getSensor().getSensorType() == SensorType.PRESENCE) {

			logger.debug("processando dados da medição: "+measurement);
			
			try {
				boolean hasPeople = Boolean
						.parseBoolean(measurement.getValue());
				if (hasPeople) {
					// ligar todos os aparelhos de ar condicionado da sala

					AIR_CONTROL_UTIL.turOnAllAirCofRoomr(measurement
							.getSensor().getRoom());

				} else {

					RoomKey roomKey = new RoomKey();

					roomKey.setLocalName(measurement.getSensor().getRoom()
							.getLocal().getLocalName());
					roomKey.setRoomName(measurement.getSensor().getRoom()
							.getRoomName());

					if (!AIR_CONTROL_UTIL.hasPeopleInTheRoom(roomKey, 1)) {
						// caso nao tenha sido registrada nenhuma presença nos
						// ultimos 15 min o ar sera desligado
						AIR_CONTROL_UTIL.turOffAllAirCofRomm(measurement.getSensor().getRoom());
					}

				}

			} catch (Exception e) {
				throw new TreaterException(
						"Erro: "+e.getMessage());
			}

		} else {
			if (next != null) {
				next.treaterMeasurement(measurement);
			} else if (next == null) {
				throw new TreaterException(
						"Nao foi possivel processar essa medicao");
			}
		}

	}

}
