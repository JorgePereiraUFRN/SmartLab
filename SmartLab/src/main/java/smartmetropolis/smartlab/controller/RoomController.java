package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.HibernateDAOFactory;
import smartmetropolis.smartlab.dao.RoomDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Room;

public class RoomController {

	private static final RoomController ROOM_CONTROLLER = new RoomController();
	private final DAOFactory factory = new HibernateDAOFactory();
	private final RoomDaoInterface roomDao;

	private RoomController() {
		roomDao = factory.getRoomDao();
	}

	public static synchronized RoomController getInstance() {
		return ROOM_CONTROLLER;
	}

	public void validateRoom(Room room) throws validateDataException {

		if (room == null) {
			throw new validateDataException("The room is null");
		} else if (room.getName() == null || room.getName().equals("")) {
			throw new validateDataException("The room name is null");
		} else if (room.getLocal() == null || room.getLocal().getId() == null) {
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

		if (room.getId() == null || room.getId() < 0) {
			throw new validateDataException("The room id is invalid");
		}

		return roomDao.update(room);
	}

	public void deleteRoom(long roomId) throws DAOException {

		Room room = findRoom(roomId);

		if (room != null) {
			roomDao.delete(room);
		}
	}

	public Room findRoom(Long roomId) throws DAOException {

		return roomDao.findById(Room.class, roomId);
	}

	public List<Room> findAllRooms() throws DAOException {
		return roomDao.findAll(Room.class);
	}

}
