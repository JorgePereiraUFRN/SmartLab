package smartmetropolis.smartlab.MeasurementBlackBoard;

import java.util.List;

import org.apache.log4j.Logger;

import smartmetropolis.smartlab.controller.AirConditionerController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public class TemperatureMeasurementTreater extends MeasurementTreater {

	private AirControlInterface AIR_CONTROL = AirControl.getInstance();
	private SensorController sensorController = SensorController.getInstance();
	private RoomController roomController = RoomController.getInstance();
	private AirConditionerController airConditionerController = AirConditionerController
			.getInstance();

	private Logger logger = Logger
			.getLogger(TemperatureMeasurementTreater.class);

	public TemperatureMeasurementTreater() {

	}

	@Override
	public void treaterMeasurement(Measurement measurement)
			throws TreaterException {

		try {
			Sensor sensor = sensorController.findSensor(measurement
					.getSensorId());

			if (sensor.getSensorType().ordinal() == SensorType.TEMPERATURE
					.ordinal()) {

				logger.debug("processando dados da medição: " + measurement);

				Room room = roomController.findRoom(sensor.getRoomName());

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
					if (AIR_CONTROL.timeFromLastAirChange(room) > 10 * 60L) {

						try {

							if (temperature < AIR_CONTROL.targetTemperature) {
								logger.info("teperatura menor que "
										+ AIR_CONTROL.targetTemperature
										+ " graus");
								int diference = (int) (AIR_CONTROL.targetTemperature - temperature);

								if (diference > 3) {
									// temp < 20
									AIR_CONTROL
											.turOffAllAirConditionersOfRom(room);
								} else if (diference > 2) {
									// temp > 20
									if (!airConditionersAreOn(airConditionerController
											.findAirconditionerByRoom(room
													.getRoomName()))) {
										AIR_CONTROL
												.turOnAllAirCoditionerOfRoom(room);
									}
									AIR_CONTROL
											.increaseTemperatureAllAirConditionersOfRoom(room);

								}
							} else if (temperature > (AIR_CONTROL.targetTemperature + 1)) {
								// 23
								logger.info("teperatura maior que "
										+ AIR_CONTROL.targetTemperature + 1
										+ " graus");

								AIR_CONTROL
										.decreaseTemperatureAllAirConditionersOfRoom(room);

							}

						} catch (Exception e) {
							throw new TreaterException("erro: "
									+ e.getMessage());
						}
					}
				} else if (airConditionersAreOn(airConditionerController
						.findAirconditionerByRoom(room.getRoomName()))) {
					// caso nao tenha sido registrada nenhuma presença nos
					// ultimos
					// 15
					// min e or ar esteja ligado, um comando sera enviado para q
					// o
					// mesmo seja desligado
					logger.info("dado de temperatura recebido, no entanto não foi registrada nenhuma presença nos ultimos 10 min");
					try {
						AIR_CONTROL.turOffAllAirConditionersOfRom(room);
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
		} catch (Exception e) {
			throw new TreaterException(e.getMessage());
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
