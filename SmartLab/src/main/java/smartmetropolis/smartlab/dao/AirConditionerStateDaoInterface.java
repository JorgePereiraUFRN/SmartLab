package smartmetropolis.smartlab.dao;

import java.util.Date;
import java.util.List;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.AirConditionerState;

public interface AirConditionerStateDaoInterface extends
		GenericDaoInterface<AirConditionerState, Long> {

	public List<AirConditionerState> listByDate(Date initialDate, Date finalDate)
			throws DAOException;
}
