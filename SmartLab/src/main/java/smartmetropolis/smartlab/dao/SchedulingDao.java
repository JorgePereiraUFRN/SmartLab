package smartmetropolis.smartlab.dao;

import smartmetropolis.smartlab.model.Scheduling;

public class SchedulingDao extends GenericHibernateDAO<Scheduling, Long> implements SchedulingDaoInterface {

	public SchedulingDao() {
		super(Scheduling.class);
	}

}
