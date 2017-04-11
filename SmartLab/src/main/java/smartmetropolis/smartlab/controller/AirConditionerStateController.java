package smartmetropolis.smartlab.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.FNEG;

import smartmetropolis.smartlab.dao.AirConditionerDaoInterface;
import smartmetropolis.smartlab.dao.AirConditionerStateDao;
import smartmetropolis.smartlab.dao.AirConditionerStateDaoInterface;
import smartmetropolis.smartlab.dao.ConcreteDaoFactory;
import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.AirConditionerState;

public class AirConditionerStateController {

	private final DAOFactory factory = new ConcreteDaoFactory();
	private static final AirConditionerStateController AIR_CONDITIONER_CONTROLLER = new AirConditionerStateController();
	private final AirConditionerStateDaoInterface airStateDao;
	private final AirConditionerDaoInterface airConditionerDao;

	private AirConditionerStateController() {
		airStateDao = factory.getAirStateDao();
		airConditionerDao = factory.getAirConditionerDao();
	}

	public static AirConditionerStateController getInstance() {
		return AIR_CONDITIONER_CONTROLLER;
	}

	private boolean validadeAirSatate(AirConditionerState airState)
			throws validateDataException {

		if (airState == null) {
			throw new validateDataException("object null");
		} else if (airState.getAirConditionerId() == null) {
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
		return airStateDao.findAll();
	}

	public List<AirConditionerState> listByRoomAndDate(String roomName,
			Date initialDate, Date finalDate) throws DAOException,
			validateDataException {

		if (initialDate == null || finalDate == null) {
			throw new validateDataException("invalid date: null");
		}

		List<AirConditionerState> states = airStateDao.listByDate(initialDate,
				finalDate);
		List<AirConditionerState> aux = new ArrayList<AirConditionerState>();

		List<AirConditioner> airCs = airConditionerDao
				.getAirConditionersByRoom(roomName);

		for (AirConditionerState airC_State : states) {

			for (AirConditioner airC : airCs) {

				if (airC_State.getAirConditionerId().equalsIgnoreCase(
						airC.getId())) {
					aux.add(airC_State);
				}
			}

		}

		return aux;

	}

	public AirConditionerState findById(Long id) throws DAOException {
		return airStateDao.findById(id);
	}

	public void delete(Long id) throws DAOException, validateDataException {
		airStateDao.delete(id);

	}

}
