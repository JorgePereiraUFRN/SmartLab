package smartmetropolis.smartlab.MeasurementBlackBoard;

import java.util.List;

import org.apache.log4j.Logger;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.SensorType;

public class TemperatureMeasurementTreater extends MeasurementTreater {

	private AirControlInterface AIR_CONTROL = AirControl.getInstance();

	private Logger logger = Logger
			.getLogger(TemperatureMeasurementTreater.class);

	public TemperatureMeasurementTreater() {

	}

	@Override
	public void treaterMeasurement(Measurement measurement)
			throws TreaterException {

		if (measurement.getSensor().getSensorType() == SensorType.TEMPERATURE) {

			logger.debug("processando dados da medição: " + measurement);

			Room room = measurement.getSensor().getRoom();
			
			

			float temperature = 0;
			try {
				temperature = Float.parseFloat(measurement.getValue());

				AIR_CONTROL.setAtualTemp(room, temperature);
			} catch (NumberFormatException e) {
				throw new TreaterException("erro ao converter float: "
						+ e.getMessage());
			}

			if (AIR_CONTROL.hasPeopleInTheRoom(room, 15)) {

				// tempo em segundos desde o ultimo comando enviado ao ar
				// condicionado
				if (AIR_CONTROL.timeFromLastAirChange(measurement.getSensor()
						.getRoom()) > 10 * 60L) {

					try {

						if (temperature < AIR_CONTROL.targetTemperature) {
							logger.info("teperatura menor que "
									+ AIR_CONTROL.targetTemperature + " graus");
							int diference = (int) (AIR_CONTROL.targetTemperature - temperature);

							if (diference > 3) {
								//temp < 20
								AIR_CONTROL
										.turOffAllAirConditionersOfRom(measurement
												.getSensor().getRoom());
							} else if(diference > 2) {
								//temp > 20
								if (!airConditionersAreOn(room
										.getAirConditioners())) {
									AIR_CONTROL
											.turOnAllAirCoditionerOfRoom(room);
								}
								AIR_CONTROL
										.increaseTemperatureAllAirConditionersOfRoom(measurement
												.getSensor().getRoom());

							}
						} else if (temperature > (AIR_CONTROL.targetTemperature + 1)) {
							//23
							logger.info("teperatura maior que "
									+ AIR_CONTROL.targetTemperature + 1
									+ " graus");

							AIR_CONTROL
									.decreaseTemperatureAllAirConditionersOfRoom(measurement
											.getSensor().getRoom());

						}

					} catch (Exception e) {
						throw new TreaterException("erro: " + e.getMessage());
					}
				}
			} else if (airConditionersAreOn(room.getAirConditioners())) {
				// caso nao tenha sido registrada nenhuma presença nos ultimos
				// 15
				// min e or ar esteja ligado, um comando sera enviado para q o
				// mesmo seja desligado
				logger.info("dado de temperatura recebido, no entanto não foi registrada nenhuma presença nos ultimos 10 min");
				try {
					AIR_CONTROL.turOffAllAirConditionersOfRom(measurement
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

	private boolean airConditionersAreOn(List<AirConditioner> airConditioners) {

		for (AirConditioner a : airConditioners) {
			if (a.getItsOn()) {
				return true;
			}
		}

		return false;
	}

}
