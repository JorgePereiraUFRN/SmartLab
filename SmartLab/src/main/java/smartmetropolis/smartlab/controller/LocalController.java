package smartmetropolis.smartlab.controller;

import java.util.List;

import com.sun.org.apache.bcel.internal.generic.LLOAD;

import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.HibernateDAOFactory;
import smartmetropolis.smartlab.dao.LocalDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;

public class LocalController {

	private final DAOFactory factory = new HibernateDAOFactory();
	private final LocalDaoInterface localDao;
	private static final LocalController LOCAL_CONTROLLER = new LocalController();

	private LocalController() {
		localDao = factory.getLocalDao();
	}

	public static synchronized LocalController getInstance() {
		return LOCAL_CONTROLLER;
	}

	private void validateLocal(Local local) throws validateDataException {

		if (local == null) {
			throw new validateDataException("Local null");
		} else if (local.getLocaName() == null || local.getLocaName().equals("")) {
			throw new validateDataException("Local name is null");
		}
	}

	public Local saveLocal(Local local) throws validateDataException,
			DAOException {
		validateLocal(local);
		return localDao.save(local);
	}

	public Local updateLocal(Local local) throws validateDataException,
			DAOException {
		validateLocal(local);

		return localDao.update(local);
	}

	public List<Local> findAllLocals() throws DAOException {
		return localDao.findAll(Local.class);
	}

	public Local findLocal(String localName) throws DAOException {

		return localDao.findById(Local.class, localName);
	}

	public void deleteLocal(String locaName) throws DAOException {

		Local l = findLocal(locaName);

		if (l != null) {
			localDao.delete(l);
		}
	}

}
