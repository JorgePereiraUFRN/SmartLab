package smartmetropolis.smartlab.controller;

import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.HibernateDAOFactory;
import smartmetropolis.smartlab.dao.SchedulingDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Scheduling;

public class SchedulingController {

	private final DAOFactory factory = new HibernateDAOFactory();
	private final SchedulingDaoInterface schedulingDao;
	private static final SchedulingController schedulingController = new SchedulingController();

	private SchedulingController() {
		schedulingDao = factory.getsSchedulingDao();
	}

	public static synchronized SchedulingController getInstance() {
		return schedulingController;
	}

	private void validateScheduling(Scheduling scheduling)
			throws validateDataException {

		if (scheduling == null) {
			throw new validateDataException("Object null");
		} else if (scheduling.getDate() == null) {
			throw new validateDataException("Invalid Date");
		} else if (scheduling.getRoom() == null
				|| scheduling.getRoom().getRoomName() == null
				|| scheduling.getRoom().getLocal() == null
				|| scheduling.getRoom().getLocal().getLocalName() == null) {
			throw new validateDataException("Invalid scheduling room");
		} else if (scheduling.getUser() == null
				|| scheduling.getUser().getLogin() == null) {
			throw new validateDataException("Invalid scheduling user");
		}
	}

	public Scheduling saveScheduling(Scheduling scheduling)
			throws validateDataException, DAOException {

		validateScheduling(scheduling);

		return schedulingDao.save(scheduling);
	}

	public Scheduling updateScheduling(Scheduling scheduling)
			throws validateDataException, DAOException {

		validateScheduling(scheduling);

		if (scheduling.getId() == null || scheduling.getId() < 0) {
			throw new validateDataException("Invalid scheduling id");
		}

		return schedulingDao.update(scheduling);
	}

	public Scheduling findScheduling(Long id) throws DAOException {

		return schedulingDao.findById(Scheduling.class, id);
	}

	public void removeScheduling(Long id) throws DAOException {

		Scheduling s = findScheduling(id);

		if (s != null) {
			schedulingDao.delete(s);
		}
	}

}
