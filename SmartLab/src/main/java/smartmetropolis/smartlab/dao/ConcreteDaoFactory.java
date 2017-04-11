package smartmetropolis.smartlab.dao;

public class ConcreteDaoFactory extends DAOFactory {
	
	private GenericHibernateDAO instantiateDAO(Class daoClass) {
        try {
            GenericHibernateDAO dao = (GenericHibernateDAO) daoClass.newInstance();
           
            return dao;
        } catch (Exception ex) {
            throw new RuntimeException("Can not instantiate DAO: " + daoClass, ex);
        }
    }

	@Override
	public SensorDaoInterface getSensorDao() {
		return new SensorDao();
	}

	@Override
	public MeasurementDaoInterface getMeasurementDao() {
		return (MeasurementDaoInterface) instantiateDAO(MeasurementDao.class);
	}

	@Override
	public RoomDaoInterface getRoomDao() {
		return new RoomDao();
	}

	@Override
	public AirConditionerDaoInterface getAirConditionerDao() {
		return new AirConditionerDao();
	}

	@Override
	public LocalDaoInterface getLocalDao() {
		return new LocalDao();
	}

	@Override
	public SchedulingDaoInterface getsSchedulingDao() {
		return  (SchedulingDaoInterface) instantiateDAO(SchedulingDao.class);
	}

	@Override
	public UserDaoInterface getUserDao() {
		return (UserDaoInterface) instantiateDAO(UserDao.class);
	}

	@Override
	public AirConditionerStateDaoInterface getAirStateDao() {
		return (AirConditionerStateDao) instantiateDAO(AirConditionerStateDao.class);
	}

}
