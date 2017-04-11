package smartmetropolis.smartlab.dao;


import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Room;

public interface RoomDaoInterface extends GenericDaoInterface<Room, String>{

	
	 List<Room> findRoomsByBuilding(String predio) throws DAOException;
}
