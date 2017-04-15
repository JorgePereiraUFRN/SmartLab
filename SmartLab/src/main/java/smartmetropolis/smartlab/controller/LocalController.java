package smartmetropolis.smartlab.controller;

import java.util.List;

import com.sun.org.apache.bcel.internal.generic.LLOAD;

import smartmetropolis.smartlab.dao.ConcreteDaoFactory;
import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.LocalDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;

public class LocalController {

	private final DAOFactory factory = new ConcreteDaoFactory();
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
		} else if (local.getLocalName() == null
				|| local.getLocalName().equals("")) {
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
		return localDao.findAll();
	}

	public Local findLocal(String localName) throws DAOException {

		return localDao.findById(localName);
	}

	public void deleteLocal(String locaName) throws DAOException {

		localDao.delete(locaName);

	}

}
