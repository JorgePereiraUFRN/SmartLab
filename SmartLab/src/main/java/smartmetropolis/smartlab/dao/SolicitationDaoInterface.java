package smartmetropolis.smartlab.dao;

import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Solicitation;

public interface SolicitationDaoInterface extends GenericDaoInterface<Solicitation, Long>{
	
	public List<Solicitation> listSoliciationsByUser(String userLogin) throws DAOException;

}
