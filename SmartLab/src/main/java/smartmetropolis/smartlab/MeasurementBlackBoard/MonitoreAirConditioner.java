package smartmetropolis.smartlab.MeasurementBlackBoard;

import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.model.RoomKey;

public class MonitoreAirConditioner extends Thread {

	private static volatile AirControlUtil airControlUtil;

	public MonitoreAirConditioner() {

		airControlUtil = airControlUtil.getInstance();
	}

	public void run() {
		
		System.out.println("thread started");

		for (;;) {
			try {
				// dormir por 15 min
				Thread.sleep(15 * 60 * 1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			RoomKey roomKey = new RoomKey();

			roomKey.setLocalName("Instituto Metrópole Digital - IMD");
			roomKey.setRoomName("A307");

			try {

				boolean hasPeople = airControlUtil.hasPeopleInTheRoom(roomKey,
						15);

				if (!hasPeople) {
					airControlUtil.turOffAirConditioner();
				}

			} catch (TreaterException e) {

				e.printStackTrace();
			}

		}
	}
}
