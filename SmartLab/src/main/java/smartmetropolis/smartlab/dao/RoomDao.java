package smartmetropolis.smartlab.dao;

import java.util.ArrayList;
import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Room;
import br.ufrn.NGSI_10Client.entities.Attribute;
import br.ufrn.NGSI_10Client.entities.ContextElement;

public class RoomDao extends GenericDaoOrion<Room> implements RoomDaoInterface {

	private static final String type = "Sala";
	
	public RoomDao() {
		super(type);
	}

	

	public ContextElement entityToContextElement(Room entity) {

		ContextElement context = new ContextElement(type,
				entity.getRoomName(), false);

		Attribute predio = new Attribute("Predio", "String", entity.getPredio());
		context.addAttribute(predio);

		return context;

	}

	public Room contextElementToEntity(ContextElement contextElement) {
		
		if (contextElement != null && contextElement.getAttributes() != null
				&& contextElement.getId() != null) {
			
			Room room = new Room();
			
			room.setRoomName(contextElement.getId());
			
			room.setPredio(getAttributeValue("Predio", contextElement));

			
			return room;
		}
		
		
		return null;
	}

	public static void main(String[] args) throws DAOException {

		RoomDao dao = new RoomDao();

		Room room = new Room();

		room.setPredio("IMD");
		room.setRoomName("B318");

		//dao.save(room);
		
		for(Room r : dao.findAll()){
			System.out.println(r);
		}
	}

	public List<Room> findRoomsByBuilding(String predio) throws DAOException {
		
		List<Room> aux = null;
		
		
		List<Room> rooms = findAll();
		
		if(rooms != null && rooms.size() > 0){
			
			aux = new ArrayList<Room>();
			
			for(Room r : rooms){
				if(r.getPredio().equals(predio)){
					aux.add(r);
				}
			}
		}
		
		return aux;
	}

}
