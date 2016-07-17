package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.AirConditionerDaoInterface;
import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.HibernateDAOFactory;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.AirConditioner;

public class AirConditionerController {

	private final DAOFactory factory = new HibernateDAOFactory();
	private static final AirConditionerController AIR_CONDITIONER_CONTROLLER = new AirConditionerController();
	private final AirConditionerDaoInterface airConditionerDao;

	public AirConditionerController() {
		airConditionerDao = factory.getAirConditionerDao();
	}

	private static synchronized AirConditionerController getInstance() {
		return AIR_CONDITIONER_CONTROLLER;
	}

	private void validateAirConditioner(AirConditioner airC)
			throws validateDataException {

		if (airC == null) {
			throw new validateDataException("Invalide Values: null");
		}
		if (airC.getRoom() == null || airC.getRoom().getRoomName() == null
				|| airC.getRoom().getLocal() == null
				|| airC.getRoom().getLocal().getLocaName() == null) {
			throw new validateDataException("Invalide Room");
		}
	}

	public AirConditioner saveAirConditioner(AirConditioner airC)
			throws validateDataException, DAOException {

		validateAirConditioner(airC);
		return airConditionerDao.save(airC);

	}

	public void removeAirconditioner(Long airId) throws DAOException,
			validateDataException {

		AirConditioner air = findAirConditioner(airId);

		if (air != null) {
			airConditionerDao.delete(air);
		} else {
			throw new validateDataException("Invalide id");
		}
	}

	public AirConditioner updateAirConditioner(AirConditioner airC)
			throws DAOException, validateDataException {
		validateAirConditioner(airC);

		if (airC.getId() == null || airC.getId() < 0) {
			throw new validateDataException("Invalide air conditioner id");
		}

		return airConditionerDao.update(airC);

	}

	public AirConditioner findAirConditioner(Long airId) throws DAOException {
		return airConditionerDao.findById(AirConditioner.class, airId);
	}

	public List<AirConditioner> getAllAircAirConditioners() throws DAOException {
		return airConditionerDao.findAll(AirConditioner.class);
	}
}
