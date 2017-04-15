package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.AirConditionerDaoInterface;
import smartmetropolis.smartlab.dao.ConcreteDaoFactory;
import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.RoomDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.AirConditioner;

public class AirConditionerController {

	private final DAOFactory factory = new ConcreteDaoFactory();
	private static final AirConditionerController AIR_CONDITIONER_CONTROLLER = new AirConditionerController();
	private final AirConditionerDaoInterface airConditionerDao;
	private final RoomDaoInterface roomDao;

	private AirConditionerController() {
		airConditionerDao = factory.getAirConditionerDao();
		roomDao = factory.getRoomDao();
	}

	public static synchronized AirConditionerController getInstance() {
		return AIR_CONDITIONER_CONTROLLER;
	}

	private void validateAirConditioner(AirConditioner airC)
			throws validateDataException, DAOException {

		if (airC == null || airC.getId() == null) {
			throw new validateDataException("Invalid Values: null");
			
		} else if (airC.getRoomId() == null) {

			throw new validateDataException("Invalid Room Id");

		} else if (airC.getId() != null) {
			if (roomDao.findById(airC.getRoomId()) == null) {
				throw new validateDataException("Invalid Room Id");
			}
			
			
		} else if (airC.getIpAddressAirControl() == null
				|| airC.getIpAddressAirControl().equals("")) {
			throw new validateDataException("Invalid IP Adddres of air control");
		}
	}

	public AirConditioner saveAirConditioner(AirConditioner airC)
			throws validateDataException, DAOException {

		validateAirConditioner(airC);
		return airConditionerDao.save(airC);

	}

	public void removeAirconditioner(Long airId) throws DAOException,
			validateDataException {

		airConditionerDao.delete(airId.toString());
	}

	public AirConditioner updateAirConditioner(AirConditioner airC)
			throws DAOException, validateDataException {
		validateAirConditioner(airC);

		if (airC.getId() == null || !airC.getId().equals("")) {
			throw new validateDataException("Invalide air conditioner id");
		}

		return airConditionerDao.update(airC);

	}

	public AirConditioner findAirConditioner(Long airId) throws DAOException {
		return airConditionerDao.findById(airId.toString());
	}

	public List<AirConditioner> listAllAircAirConditioners()
			throws DAOException {
		return airConditionerDao.findAll();
	}
	
	public List<AirConditioner> findAirconditionerByRoom(String roomId){
		
		return findAirconditionerByRoom(roomId);
	}
}
