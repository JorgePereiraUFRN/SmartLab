package smartmetropolis.smartlab.dao;

import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Solicitation;

public class SolicitationDao extends GenericHibernateDAO<Solicitation, Long> implements SolicitationDaoInterface {

	public SolicitationDao() {
		super(Solicitation.class);
	
	}

	public List<Solicitation> listSoliciationsByUser(String userLogin)
			throws DAOException {
		
		List<Solicitation> list = null;
		
		try {
			list = getInstance()
					.createQuery(
							"select s from "
									+ Solicitation.class.getSimpleName()
									+ " s where  s.user.login = :user")
					.setParameter("user", userLogin).getResultList();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		return list;
	}
	
	
	
	

	
}
