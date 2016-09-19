package smartmetropolis.smartlab.controller;

import java.util.Date;
import java.util.List;

import smartmetropolis.smartlab.dao.AirConditionerStateDao;
import smartmetropolis.smartlab.dao.AirConditionerStateDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.AirConditionerState;

public class AirConditionerStateController {

	private static final AirConditionerController AIR_CONDITIONER_CONTROLLER = new AirConditionerController();
	private static final AirConditionerStateDaoInterface airStateDao = new AirConditionerStateDao();

	private AirConditionerStateController() {

	}

	public static AirConditionerController getInstance() {
		return AIR_CONDITIONER_CONTROLLER;
	}

	private boolean validadeAirSatate(AirConditionerState airState)
			throws validateDataException {

		if (airState == null) {
			throw new validateDataException("object null");
		} else if (airState.getAirConditioner() == null
				|| airState.getAirConditioner().getId() == null) {
			throw new validateDataException("air conditioner id ivalid.");
		} else if (airState.getAction() == null) {
			throw new validateDataException("inavlid action.");
		} else if (airState.getTimestamp() == null) {
			airState.setTimestamp(new Date(System.currentTimeMillis()));
		}

		return true;
	}

	public AirConditionerState save(AirConditionerState airState)
			throws DAOException, validateDataException {

		validadeAirSatate(airState);
		return airStateDao.save(airState);

	}

	
	public AirConditionerState update(AirConditionerState airState)
			throws validateDataException, DAOException {

		validadeAirSatate(airState);
		if (airState.getId() != null && airState.getId() >= 0) {
			return airStateDao.save(airState);
		} else {
			throw new validateDataException("ivalid id");
		}
	}

	
	
	public List<AirConditionerState> listAll() throws DAOException {
		return airStateDao.findAll(AirConditionerState.class);
	}

	
	
	public AirConditionerState findById(Long id) throws DAOException {
		return airStateDao.findById(AirConditionerState.class, id);
	}
	
	

	public void delete(Long id) throws DAOException, validateDataException {

		AirConditionerState airState = airStateDao.findById(
				AirConditionerState.class, id);

		if (airState == null) {
			throw new validateDataException("invalid id");
		} else {
			airStateDao.delete(airState);
		}

	}

}
