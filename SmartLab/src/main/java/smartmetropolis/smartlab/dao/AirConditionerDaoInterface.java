package smartmetropolis.smartlab.dao;

import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.AirConditioner;

public interface AirConditionerDaoInterface extends GenericDaoInterface<AirConditioner, String>{

	
	List<AirConditioner> getAirConditionersByRoom(String RoomId) throws DAOException;
}
