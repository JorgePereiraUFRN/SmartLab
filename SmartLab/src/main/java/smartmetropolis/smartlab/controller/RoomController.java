package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.HibernateDAOFactory;
import smartmetropolis.smartlab.dao.RoomDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Room;

public class RoomController {

	private static final RoomController ROOM_CONTROLLER = new RoomController();
	private final LocalController localController;
	private final DAOFactory factory = new HibernateDAOFactory();
	private final RoomDaoInterface roomDao = null;

	private RoomController() {
		//roomDao = factory.getRoomDao();
		localController = LocalController.getInstance();
	}

	public static synchronized RoomController getInstance() {
		return ROOM_CONTROLLER;
	}

	public void validateRoom(Room room) throws validateDataException {

		if (room == null ) {
			throw new validateDataException("The room is null");
		} else if (room.getRoomName() == null || room.getRoomName().equals("")) {
			throw new validateDataException("The room name is null");
		} else if (room.getPredio() == null || room.getPredio().equals("")) {
			
			throw new validateDataException("The room local is null");
		}
	}

	public Room saveRoom(Room room) throws validateDataException, DAOException {
		validateRoom(room);
		return roomDao.save(room);

	}

	public Room updateRoom(Room room) throws validateDataException,
			DAOException {
		validateRoom(room);

		return roomDao.update(room);
	}

	public void deleteRoom(String roomName)
			throws DAOException, validateDataException {

			roomDao.delete(roomName);
	
	}

	public Room findRoom(String roomName)
			throws DAOException, validateDataException {

		return roomDao.findById(roomName);
	}

	public List<Room> findAllRooms() throws DAOException {
		return roomDao.findAll();
	}
	
	
	public List<Room> findRoomsByBuilding(String predio) throws DAOException {
		return roomDao.findRoomsByBuilding(predio);
	}
	
	
	

}
