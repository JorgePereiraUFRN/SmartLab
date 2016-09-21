package smartmetropolis.smartlab.managedBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import smartmetropolis.smartlab.controller.AirConditionerController;
import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Room;

@ManagedBean(name="airConditionerB")
@SessionScoped
public class AirConditionerMBean {
	
	private Map<String, String> localsMap;
	private Map<String, String> roomsMap;
	private String localName;
	private String roomName;
	private List<AirConditioner> airConditioners = new ArrayList<AirConditioner>();
	
	private AirConditioner airConditioner;
	
	private LocalController localController;
	private RoomController roomController;
	private AirConditionerController airController;

	public AirConditionerMBean() {
		localController = LocalController.getInstance();
		roomController = RoomController.getInstance();
		airController = AirConditionerController.getInstance();
		airConditioner = new AirConditioner();
		initLocalsMap();
	}
	
	private void initLocalsMap() {

		try {
			List<Local> locals = localController.findAllLocals();
			localsMap = new HashMap<String, String>();

			for (Local l : locals) {
				localsMap.put(l.getLocalName(), l.getLocalName());
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initRoomsMap() {

		roomsMap = new HashMap<String, String>();

		try {
			Local l = localController.findLocal(localName);
			
			if (l != null) {
				
				for (Room r : l.getRooms()) {
					roomsMap.put(r.getRoomName(), r.getRoomName());
				}
			}

		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void saveAirconditioner(){
		
		try {
			Room r = roomController.findRoom(roomName, localName);
			
			
			if(r != null){
				airConditioner.setRoom(r);
				airConditioner.setItsOn(false);
				
				airController.saveAirConditioner(airConditioner);
				listAirConditioners();
				airConditioner = new AirConditioner();
			}
			
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (validateDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void listAirConditioners(){
		
		try {
			airConditioners = airController.listAllAircAirConditioners();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String, String> getLocalsMap() {
		return localsMap;
	}

	public void setLocalsMap(Map<String, String> localsMap) {
		this.localsMap = localsMap;
	}

	public Map<String, String> getRoomsMap() {
		return roomsMap;
	}

	public void setRoomsMap(Map<String, String> roomsMap) {
		this.roomsMap = roomsMap;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public List<AirConditioner> getAirConditioners() {
		return airConditioners;
	}

	public void setAirConditioners(List<AirConditioner> airConditioners) {
		this.airConditioners = airConditioners;
	}

	public AirConditioner getAirConditioner() {
		return airConditioner;
	}

	public void setAirConditioner(AirConditioner airConditioner) {
		this.airConditioner = airConditioner;
	}
	
	
	
	

}
