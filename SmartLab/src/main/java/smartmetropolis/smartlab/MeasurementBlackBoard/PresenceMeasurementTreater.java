package smartmetropolis.smartlab.MeasurementBlackBoard;

import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.SensorType;

public class PresenceMeasurementTreater extends MeasurementTreater {

	private AirControlUtil AIR_CONTROL_UTIL = AirControlUtil
			.getInstance();

	public PresenceMeasurementTreater() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void treaterMeasurement(Measurement measurement)
			throws TreaterException {

		if (measurement.getSensor().getSensorType() == SensorType.PRESENCE) {

			try {
				boolean hasPeople = Boolean
						.parseBoolean(measurement.getValue());
				if (hasPeople) {
					AIR_CONTROL_UTIL.turOnAirConditioner();
				} else {

					RoomKey roomKey = new RoomKey();

					roomKey.setLocalName(measurement.getSensor().getRoom()
							.getLocal().getLocalName());
					roomKey.setRoomName(measurement.getSensor().getRoom()
							.getRoomName());

					if (!AIR_CONTROL_UTIL.hasPeopleInTheRoom(roomKey, 1)) {
						// caso nao tenha sido registrada nenhuma presença nos
						// ultimos 15 min o ar sera desligado
						AIR_CONTROL_UTIL.turOffAirConditioner();
					}

				}

			} catch (Exception e) {
				throw new TreaterException("Erro ao converter valor da medicaoo");
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
