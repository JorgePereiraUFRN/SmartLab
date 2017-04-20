package smartmetropolis.smartlab.MeasurementBlackBoard;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.UnavailableDataException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Room;


public interface AirControlInterface {
	
	public static final int targetTemperature = 23; 

	public  boolean hasPeopleInTheRoom(String room, int minutes)
			throws TreaterException;

	public  Long timeFromLastAirChange(String room);

	public  void turOffAllAirConditionersOfRom(String room) throws DAOException,
			validateDataException;

	public void increaseTemperatureAllAirConditionersOfRoom(String room)
			throws DAOException, validateDataException;

	public void turOnAllAirCoditionerOfRoom(String room) throws DAOException,
			validateDataException;

	
	public  void decreaseTemperatureAllAirConditionersOfRoom(String room)
			throws DAOException, validateDataException;
	
	public Float getAtualTemperature(String rom) throws UnavailableDataException;
	
	public void setAtualTemp(String room, Float temp);

}