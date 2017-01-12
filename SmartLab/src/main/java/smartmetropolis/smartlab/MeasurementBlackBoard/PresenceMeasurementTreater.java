package smartmetropolis.smartlab.MeasurementBlackBoard;

import java.util.List;

import org.apache.log4j.Logger;

import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.SensorType;

public class PresenceMeasurementTreater extends MeasurementTreater {

	private AirControlInterface AIR_CONTROL = AirControl.getInstance();

	private Logger logger = Logger.getLogger(PresenceMeasurementTreater.class);
	
	private ReservesSystem reservesSystem = new ReservesSystem();

	public PresenceMeasurementTreater() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void treaterMeasurement(Measurement measurement)
			throws TreaterException {

		if (measurement.getSensor().getSensorType() == SensorType.PRESENCE) {

			logger.debug("processando dados da medição: " + measurement);

			try {
				boolean hasPeople = Boolean
						.parseBoolean(measurement.getValue());

				Room room = measurement.getSensor().getRoom();
				float temperatureRoom = AIR_CONTROL.getAtualTemperature(room);

				boolean airconditionersAreOn = airConditionersAreOn(measurement
						.getSensor().getRoom().getAirConditioners());

				if (hasPeople
						&& !airconditionersAreOn
						&& (temperatureRoom > (AIR_CONTROL.targetTemperature - 3))) {
					// ligar todos os aparelhos de ar condicionado da sala tem > 20

					AIR_CONTROL.turOnAllAirCoditionerOfRoom(measurement
							.getSensor().getRoom());

				} else if (!AIR_CONTROL.hasPeopleInTheRoom(room, 15)
						&& airConditionersAreOn(room.getAirConditioners())
						&& reservesSystem.hasApprovedReserves(room.getRoomName(), 15)) {
					// caso nao tenha sido registrada nenhuma presença nos
					// ultimos 15 min o ar sera desligado
					AIR_CONTROL.turOffAllAirConditionersOfRom(room);
					
					logger.info("desativando todos os aparellhos da sala. Motivo: nao existem pessoas na sala e nao existem reservas para os proximos 15 min ");
				}

			} catch (Exception e) {
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
