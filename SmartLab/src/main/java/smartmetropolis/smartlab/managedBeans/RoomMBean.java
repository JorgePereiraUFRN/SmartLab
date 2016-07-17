package smartmetropolis.smartlab.managedBeans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Room;

@ManagedBean(name = "roomB")
public class RoomMBean {

	private Room room;
	private RoomController roomController;
	private LocalController localController;
	private List<Room> rooms;
	private Map<String, String> localsMap;

	public RoomMBean() {

		room = new Room();
		roomController = RoomController.getInstance();
		localController = LocalController.getInstance();
		room.setLocal(new Local());

		initMap();
	}

	private void initMap() {
		localsMap = new HashMap<String, String>();

		try {
			List<Local> locals = localController.findAllLocals();

			for (Local l : locals) {
				localsMap.put(l.getLocaName(), l.getLocaName());
			}

		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveRoom() {

		try {

			// Local local = localController.findLocal(localId);
			// room.setLocal(local);
			roomController.saveRoom(room);
			room = new Room();
			room.setLocal(new Local());

			listRoons();

		} catch (validateDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listRoons() {

		try {
			rooms = roomController.findAllRooms();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public Map<String, String> getLocalsMap() {
		return localsMap;
	}

	public void setLocalsMap(Map<String, String> localsMap) {
		this.localsMap = localsMap;
	}

}
