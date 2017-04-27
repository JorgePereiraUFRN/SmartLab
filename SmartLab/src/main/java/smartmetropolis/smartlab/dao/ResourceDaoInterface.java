package smartmetropolis.smartlab.dao;

import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Resource;
import smartmetropolis.smartlab.model.ResourceType;


public interface ResourceDaoInterface extends GenericDaoInterface<Resource, Long>{

	
	public List<Resource> findResourcesByRoomAndResourceType(String room, ResourceType type) throws DAOException;
	
	public List<Resource> findResourcesByRoom(String room) throws DAOException;
}
