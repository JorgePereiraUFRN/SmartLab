package smartmetropolis.smartlab.MeasurementBlackBoard;

import org.apache.log4j.Logger;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.SensorType;

public class TemperatureMeasurementTreater extends MeasurementTreater {

	private AirControl AIR_CONTROL_UTIL = AirControlUtil.getInstance();

	private Logger logger = Logger.getLogger(TemperatureMeasurementTreater.class);
	
	public TemperatureMeasurementTreater() {

	}

	@Override
	public void treaterMeasurement(Measurement measurement)
			throws TreaterException {

		if (measurement.getSensor().getSensorType() == SensorType.TEMPERATURE) {

			logger.debug("processando dados da medição: "+measurement);
			
			RoomKey roomKey = new RoomKey();

			roomKey.setLocalName(measurement.getSensor().getRoom().getLocal()
					.getLocalName());
			roomKey.setRoomName(measurement.getSensor().getRoom().getRoomName());

			if (AIR_CONTROL_UTIL.hasPeopleInTheRoom(roomKey, 15)) {

				// tempo em segundos desde o ultimo comando enviado ao ar
				// condicionado
				if (AIR_CONTROL_UTIL.timeFromLastAirChange(measurement.getSensor().getRoom()) > 5L) {

					try {
						float temperature = Float.parseFloat(measurement
								.getValue());

						if (temperature < 20.0) {

							int diference = 20 - (int) temperature;

							for (int i = 0; i < diference; i++) {
								AIR_CONTROL_UTIL
										.increaseTemperatureAllAirCofRoom(measurement
												.getSensor().getRoom());
							}
						} else if (temperature > 23.0) {
							int diference = (int) temperature - 23;

							for (int i = 0; i < diference; i++) {
								AIR_CONTROL_UTIL
										.decreaseTemperatureAllAirCofRoom(measurement
												.getSensor().getRoom());
							}

						}

					} catch (NumberFormatException e) {
						throw new TreaterException("erro ao converter float: "
								+ e.getMessage());
					} catch (Exception e) {
						throw new TreaterException("erro: " + e.getMessage());
					}
				}
			} else {
				// caso nao tenha sido registrada nenhuma presença nos ultimos
				// 15
				// min o ar sera desligado
				try {
					AIR_CONTROL_UTIL.turOffAllAirCofRomm(measurement
							.getSensor().getRoom());
				} catch (DAOException e) {
					throw new TreaterException("erro: " + e.getMessage());
				} catch (validateDataException e) {
					throw new TreaterException("erro: " + e.getMessage());
				}
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
