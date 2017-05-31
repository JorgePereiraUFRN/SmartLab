package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.ConcreteDaoFactory;
import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.SolicitationDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Solicitation;
import smartmetropolis.smartlab.model.SolicitationStatus;

public class SolicitationController {

	private static final SolicitationController SOLICITATION_CONTROLLER = new SolicitationController();
	private SolicitationDaoInterface solicitationDao;
	private DAOFactory daoFactory;

	private SolicitationController() {

		daoFactory = new ConcreteDaoFactory();
		solicitationDao = daoFactory.getSolicitationDao();
	}

	public static SolicitationController getInstance() {
		return SOLICITATION_CONTROLLER;
	}

	public void validateSolicitation(Solicitation solicitation)
			throws validateDataException {

		if (solicitation == null) {
			throw new validateDataException("Obejto nulo!");
		} else if (solicitation.getDeadline() == null) {
			throw new validateDataException("Prazo não informado!");
		} else if (solicitation.getOpenDate() == null) {
			throw new validateDataException(
					"Data de abertura da solicitação não informada!");
		} else if (solicitation.getStatus() == null) {
			throw new validateDataException(
					"status da solicitação não informado!");
		} else if (solicitation.getUser() == null
				|| solicitation.getUser().getLogin() == null) {
			throw new validateDataException(
					"Usuário responsavel pela abertura da solicitação não informado!");
		}else if (solicitation.getResource() == null) {
			throw new validateDataException("Recurso não infromado");
		}

	}

	public Solicitation saveSolicitation(Solicitation solicitation)
			throws validateDataException, DAOException {

		validateSolicitation(solicitation);

		return solicitationDao.save(solicitation);
	}

	public Solicitation updateSolicitation(Solicitation solicitation)
			throws validateDataException, DAOException {

		validateSolicitation(solicitation);

		if (solicitation.getId() == null) {
			throw new validateDataException("Identificador invalido!");
		}

		return solicitationDao.save(solicitation);
	}

	public List<Solicitation> findAll() throws DAOException {
		return solicitationDao.findAll();
	}

	public Solicitation findById(Long id) throws DAOException {
		return solicitationDao.findById(id);
	}

	public void delete(long id) throws DAOException {
		solicitationDao.delete(id);
	}
	
	public List<Solicitation> listSoliciationsByUser(String userLogin) throws DAOException{
		return solicitationDao.listSoliciationsByUser(userLogin);
	}
	
	public List<Solicitation> listSolicitationsByState(SolicitationStatus status) throws DAOException{
		return solicitationDao.listSolicitationsByState(status);
	}

}
