package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.HibernateDAOFactory;
import smartmetropolis.smartlab.dao.RoomDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.RoomKey;

public class RoomController {

	private static final RoomController ROOM_CONTROLLER = new RoomController();
	private final LocalController localController;
	private final DAOFactory factory = new HibernateDAOFactory();
	private final RoomDaoInterface roomDao;

	private RoomController() {
		roomDao = factory.getRoomDao();
		localController = LocalController.getInstance();
	}

	public static synchronized RoomController getInstance() {
		return ROOM_CONTROLLER;
	}

	public void validateRoom(Room room) throws validateDataException {

		if (room == null) {
			throw new validateDataException("The room is null");
		} else if (room.getRoomName() == null || room.getRoomName().equals("")) {
			throw new validateDataException("The room name is null");
		} else if (room.getLocal() == null || room.getLocal().getLocalName() == null
				|| room.getLocal().getLocalName().equals("")) {
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

	public void deleteRoom(String roomName, String localName)
			throws DAOException, validateDataException {

		Room room = findRoom(roomName, localName);

		if (room != null) {
			roomDao.delete(room);
		}
	}

	public Room findRoom(String roomName, String localName)
			throws DAOException, validateDataException {

		Local local = localController.findLocal(localName);

		if (local == null) {
			throw new validateDataException("Invalid local name!");
		}

		RoomKey roomKey = new RoomKey();

		roomKey.setLocalName(localName);;
		roomKey.setRoomName(roomName);

		return roomDao.findById(Room.class, roomKey);
	}

	public List<Room> findAllRooms() throws DAOException {
		return roomDao.findAll(Room.class);
	}

}
