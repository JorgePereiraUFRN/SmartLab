package smartmetropolis.smartlab.MeasurementBlackBoard;

import java.util.List;

import org.apache.log4j.Logger;

import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.RoomKey;

public class MonitoreAirConditioner extends Thread {

	private static volatile AirControlUtil airControlUtil;
	private static final RoomController ROOM_CONTROLLER = RoomController
			.getInstance();

	private Logger logger = Logger.getLogger(MonitoreAirConditioner.class);

	public MonitoreAirConditioner() {

		airControlUtil = airControlUtil.getInstance();
	}

	public void run() {

		logger.debug("iniciando thread que monitora o estado dos aparelhos de ar condicionado.");

		for (;;) {
			try {
				// dormir por 15 min
				Thread.sleep(15 * 60 * 1000);
			} catch (InterruptedException e1) {
				logger.error(e1.getMessage());
			}

			try {
				List<Room> rooms = ROOM_CONTROLLER.findAllRooms();

				for (Room r : rooms) {
					try {

						RoomKey roomKey = new RoomKey();
						roomKey.setLocalName(r.getLocalName());
						roomKey.setRoomName(r.getRoomName());

						boolean hasPeople = airControlUtil.hasPeopleInTheRoom(
								roomKey, 15);

						if (!hasPeople) {

							Room room = ROOM_CONTROLLER.findRoom(
									r.getRoomName(), r.getLocalName());

							if (room != null) {
								logger.info("desativando aparelhos de ar condicionado da sala: "
										+ room
										+ "\nmotivo: ausencia de pessoas na sala");
								airControlUtil.turOffAllAirCofRomm(room);
							} else {
								logger.error("sala " + room + " não encontrada");
							}

						}

					} catch (TreaterException e) {

						logger.error(e.getMessage());
					} catch (DAOException e) {
						logger.error(e.getMessage());
					} catch (validateDataException e) {
						logger.error(e.getMessage());
					}
				}

			} catch (DAOException e) {
				logger.error(e.getMessage());
			}

		}
	}
}
