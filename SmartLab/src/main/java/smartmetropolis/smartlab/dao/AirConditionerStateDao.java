package smartmetropolis.smartlab.dao;

import java.util.Date;
import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.AirConditionerState;
import smartmetropolis.smartlab.model.Room;

public class AirConditionerStateDao extends GenericHibernateDAO<AirConditionerState, Long> implements AirConditionerStateDaoInterface {

	public AirConditionerStateDao() {
		super(AirConditionerState.class);
	}

	public List<AirConditionerState> listByDate(Date initialDate, Date finalDate)
			throws DAOException {
		List<AirConditionerState> list = null;
		try {
			list = getInstance()
					.createQuery(
							"select s from "
									+ AirConditionerState.class.getSimpleName()
									+ " s where  s.timestamp between :initialDate and :finalDate")
					.setParameter("initialDate", initialDate).setParameter("finalDate", finalDate).getResultList();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		return list;
	}

	


}
