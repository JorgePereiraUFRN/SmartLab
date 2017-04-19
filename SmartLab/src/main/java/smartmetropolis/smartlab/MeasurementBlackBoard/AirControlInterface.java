package smartmetropolis.smartlab.MeasurementBlackBoard;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.UnavailableDataException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Room;


public interface AirControlInterface {
	
	public static final int targetTemperature = 23; 

	public  boolean hasPeopleInTheRoom(Room room, int minutes)
			throws TreaterException;

	public  Long timeFromLastAirChange(Room room);

	public  void turOffAllAirConditionersOfRom(Room room) throws DAOException,
			validateDataException;

	public void increaseTemperatureAllAirConditionersOfRoom(Room room)
			throws DAOException, validateDataException;

	public void turOnAllAirCoditionerOfRoom(Room room) throws DAOException,
			validateDataException;

	
	public  void decreaseTemperatureAllAirConditionersOfRoom(Room room)
			throws DAOException, validateDataException;
	
	public Float getAtualTemperature(Room rom) throws UnavailableDataException;
	
	public void setAtualTemp(Room room, float temp);

}