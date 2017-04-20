package smartmetropolis.smartlab.MeasurementBlackBoard;

import java.util.List;

import org.apache.log4j.Logger;

import smartmetropolis.smartlab.controller.AirConditionerController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public class PresenceMeasurementTreater extends MeasurementTreater {

	private AirControlInterface AIR_CONTROL = AirControl.getInstance();

	private Logger logger = Logger.getLogger(PresenceMeasurementTreater.class);

	private ReservesSystem reservesSystem = new ReservesSystem();
	private SensorController sensorController = SensorController.getInstance();
	private RoomController roomController = RoomController.getInstance();
	private AirConditionerController airConditionerController = AirConditionerController
			.getInstance();

	public PresenceMeasurementTreater() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void treaterMeasurement(Measurement measurement)
			throws TreaterException {

		Sensor sensor;
		try {
			sensor = sensorController.findSensor(measurement.getSensorId());
			
			

			if (sensor.getSensorType().ordinal() == SensorType.PRESENCE
					.ordinal()) {

				logger.debug("processando dados da medição: " + measurement);

				try {
					boolean hasPeople = Boolean.parseBoolean(measurement
							.getValue());

					Room room = roomController.findRoom(sensor.getRoomName());
					
					System.out.println(room);

					Float temperatureRoom = AIR_CONTROL
							.getAtualTemperature(room.getRoomName());
					
					if(temperatureRoom == null){
						
						logger.debug("impossivel tratar medição: os dados de temperatura da sala estao indisponiveis.");
						
						return;
					}

					boolean airconditionersAreOn = airConditionersAreOn(airConditionerController
							.findAirconditionerByRoom(room.getRoomName()));

					if (hasPeople
							&& !airconditionersAreOn
							&& (temperatureRoom > (AIR_CONTROL.targetTemperature - 3))) {
						// ligar todos os aparelhos de ar condicionado da sala
						// tem > 20

						AIR_CONTROL.turOnAllAirCoditionerOfRoom(room.getRoomName());

					} else if (!AIR_CONTROL.hasPeopleInTheRoom(room.getRoomName(), 15)
							&& airConditionersAreOn(airConditionerController
									.findAirconditionerByRoom(room
											.getRoomName()))
							&& reservesSystem.hasApprovedReserves(
									room.getRoomName(), 15)) {
						// caso nao tenha sido registrada nenhuma presença nos
						// ultimos 15 min o ar sera desligado
						AIR_CONTROL.turOffAllAirConditionersOfRom(room.getRoomName());

						logger.info("desativando todos os aparellhos da sala. Motivo: nao existem pessoas na sala e nao existem reservas para os proximos 15 min ");
					}

				} catch (Exception e) {
					e.printStackTrace();
					throw new TreaterException("Erro: " + e.getMessage());
				}

			} else {
				if (next != null) {
					next.treaterMeasurement(measurement);
				} else if (next == null) {
					throw new TreaterException(
							"Nao foi possivel processar essa medicao");
				}
			}
		} catch (Exception e1) {
			throw new TreaterException(e1.getMessage());
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
