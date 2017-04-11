package smartmetropolis.smartlab.dao;

import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public interface SensorDaoInterface extends GenericDaoInterface<Sensor, String>{
	
	
	List<Sensor> findSensorByRoom(String rooomId) throws DAOException;
	
	List<Sensor> findSensorByRoomAndType(String rooomId, SensorType sensorType) throws DAOException;

}
