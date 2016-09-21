package smartmetropolis.smartlab.MeasurementBlackBoard;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.RoomKey;

public interface AirControl {

	public  boolean hasPeopleInTheRoom(RoomKey roomKey, int minutes)
			throws TreaterException;

	public  Long timeFromLastAirChange(Room room);

	public  void turOffAllAirCofRomm(Room room) throws DAOException,
			validateDataException;

	public void increaseTemperatureAllAirCofRoom(Room room)
			throws DAOException, validateDataException;

	public void turOnAllAirCofRoomr(Room room) throws DAOException,
			validateDataException;

	public  void decreaseTemperatureAllAirCofRoom(Room room)
			throws DAOException, validateDataException;

}