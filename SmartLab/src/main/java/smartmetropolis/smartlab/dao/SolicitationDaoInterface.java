package smartmetropolis.smartlab.dao;

import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Solicitation;
import smartmetropolis.smartlab.model.SolicitationStatus;

public interface SolicitationDaoInterface extends GenericDaoInterface<Solicitation, Long>{
	
	public List<Solicitation> listSoliciationsByUser(String userLogin) throws DAOException;
	
	public List<Solicitation> listSolicitationsByState(SolicitationStatus status) throws DAOException;

}
