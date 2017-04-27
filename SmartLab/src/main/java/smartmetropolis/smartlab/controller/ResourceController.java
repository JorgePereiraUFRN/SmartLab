package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.ConcreteDaoFactory;
import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.ResourceDaoInterface;
import smartmetropolis.smartlab.dao.RoomDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Resource;
import smartmetropolis.smartlab.model.ResourceType;
import smartmetropolis.smartlab.model.Room;

public class ResourceController {

	private static final ResourceController RESOURCE_CONTROLLER = new ResourceController();
	private ResourceDaoInterface resourceDao;
	private final DAOFactory daoFactory = new ConcreteDaoFactory();

	private ResourceController() {
		resourceDao = daoFactory.getResourceDao();

	}

	public static ResourceController getInstance() {
		return RESOURCE_CONTROLLER;
	}

	private void validadeResource(Resource resource)
			throws validateDataException, DAOException {

		if (resource == null) {
			throw new validateDataException("Os valores do recurso são nulos!");
		} else if (resource.getResourceName() == null
				|| resource.getResourceName().equals("")) {
			throw new validateDataException("O nome do recusos é inválido!");
		} else if (resource.getInstalationDate() == null) {
			throw new validateDataException(
					"A data de instalação do recursos está vazia.");
		} else if (resource.getResourceType() == null) {
			throw new validateDataException(
					"A categoria do recurso não foi informada!");
		} else if (resource.getRoom() == null) {
			throw new validateDataException(
					"A sala onde o recurso foi instalada não foi informada!");
		}

		RoomDaoInterface roomDao = daoFactory.getRoomDao();

		Room room = roomDao.findById(resource.getRoom());

		if (room == null) {
			throw new validateDataException("A sala informada não existe!");
		}

	}

	public Resource saveResource(Resource resource) throws DAOException,
			validateDataException {

		validadeResource(resource);
		return resourceDao.save(resource);

	}

	public Resource updateResource(Resource resource)
			throws validateDataException, DAOException {
		if (resource.getId() != null) {

			validadeResource(resource);

			return resourceDao.update(resource);
		} else {

			throw new validateDataException("The objetc id is null");
		}
	}

	public Resource findById(Long id) throws DAOException {

		return resourceDao.findById(id);

	}

	public List<Resource> findAll() throws DAOException {

		return resourceDao.findAll();
	}

	public void delete(Long id) throws DAOException {
		resourceDao.delete(id);
	}

	public List<Resource> findResourcesByRoomAndResourceType(String room,
			ResourceType type) throws DAOException {

		return resourceDao.findResourcesByRoomAndResourceType(room, type);

	}

	public List<Resource> findResourcesByRoom(String room)
			throws DAOException {

		return resourceDao.findResourcesByRoom(room);

	}

}
