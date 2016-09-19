package smartmetropolis.smartlab.MeasurementBlackBoard;

import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.SensorType;

public class TemperatureMeasurementTreater extends MeasurementTreater {

	private AirControlUtil AIR_CONTROL_UTIL = AirControlUtil
			.getInstance();

	public TemperatureMeasurementTreater() {

	}

	@Override
	public void treaterMeasurement(Measurement measurement)
			throws TreaterException {

		if (measurement.getSensor().getSensorType() == SensorType.TEMPERATURE) {

			RoomKey roomKey = new RoomKey();

			roomKey.setLocalName(measurement.getSensor().getRoom().getLocal()
					.getLocalName());
			roomKey.setRoomName(measurement.getSensor().getRoom().getRoomName());

			if (AIR_CONTROL_UTIL.hasPeopleInTheRoom(roomKey, 15)) {

				// tempo em segundos desde o ultimo comando enviado ao ar
				// condicionado
				if (AIR_CONTROL_UTIL.timeFromLastAirChange() > 5L) {

					try {
						float temperature = Float.parseFloat(measurement
								.getValue());

						if (temperature < 20.0) {

							int diference = 20 - (int) temperature;

							for (int i = 0; i < diference; i++) {
								AIR_CONTROL_UTIL.increaseTemperature();
							}
						} else if (temperature > 23.0) {
							int diference = (int) temperature - 23;

							for (int i = 0; i < diference; i++) {
								AIR_CONTROL_UTIL.decreaseTemperature();
							}
							
						}

					} catch (NumberFormatException e) {
						throw new TreaterException("erro ao converter float: "
								+ e.getMessage());
					}
				}
			} else {
				// caso natenha sido registrada nenhuma presença nos ultimos 15
				// min o ar sera desligado
				AIR_CONTROL_UTIL.turOffAirConditioner();
			}

		} else {
			if (this.next != null) {
				this.next.treaterMeasurement(measurement);
			} else if (this.next == null) {
				System.out.println("next nulo");
				throw new TreaterException(
						"Nao foi possivel processar essa medicao");
			}
		}

	}

	
	

}
