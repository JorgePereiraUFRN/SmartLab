package smartmetropolis.smartlab.dao;


public class HibernateDAOFactory extends DAOFactory {

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
		
		return (SensorDaoInterface) instantiateDAO(SensorDao.class);
	}

	@Override
	public MeasurementDaoInterface getMeasurementDao() {
		
		return (MeasurementDaoInterface) instantiateDAO(MeasurementDao.class);
	}

	@Override
	public RoomDaoInterface getRoomDao() {
		
		return (RoomDaoInterface) instantiateDAO(RoomDao.class);
	}

	@Override
	public AirConditionerDaoInterface getAirConditionerDao() {
		return  (AirConditionerDaoInterface) instantiateDAO(AirConditionerDao.class);
	}

	@Override
	public LocalDaoInterface getLocalDao() {
		return  (LocalDaoInterface) instantiateDAO(LocalDao.class);
	}

	@Override
	public SchedulingDaoInterface getsSchedulingDao() {
		return  (SchedulingDaoInterface) instantiateDAO(SchedulingDao.class);
	}

	@Override
	public UserDaoInterface getUserDao() {
		return  (UserDaoInterface) instantiateDAO(UserDao.class);
	}

	@Override
	public AirConditionerStateDaoInterface getAirStateDao() {
		return (AirConditionerStateDao) instantiateDAO(AirConditionerStateDao.class);
	}


	
	
}
