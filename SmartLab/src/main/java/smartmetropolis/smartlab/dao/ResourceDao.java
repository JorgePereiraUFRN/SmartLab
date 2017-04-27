package smartmetropolis.smartlab.dao;

import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Resource;
import smartmetropolis.smartlab.model.ResourceType;



public class ResourceDao extends GenericHibernateDAO<Resource, Long> implements ResourceDaoInterface{

	public ResourceDao() {
		super(Resource.class);
		
	}

	public List<Resource> findResourcesByRoomAndResourceType(String room,
			ResourceType type) throws DAOException {
		
		List<Resource> list = null;
		try {
			list = getInstance()
					.createQuery(
							"select m from "
									+ Resource.class.getSimpleName()
									+ " m where  m.room = :room and m.resourceType = :type ")
					.setParameter("room", room).setParameter("type", type).getResultList();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		return list;
		
		
	}

	public List<Resource> findResourcesByRoom(String room) throws DAOException {
		
		List<Resource> list = null;
		try {
			list = getInstance()
					.createQuery(
							"select m from "
									+ Resource.class.getSimpleName()
									+ " m where  m.room = :room")
					.setParameter("room", room).getResultList();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		return list;
		
		
	}
	
	
	


}
