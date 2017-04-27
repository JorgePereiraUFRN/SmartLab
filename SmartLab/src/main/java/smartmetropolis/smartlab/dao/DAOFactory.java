package smartmetropolis.smartlab.dao;



public abstract class DAOFactory {

    /**
     * Creates a standalone DAOFactory that returns unmanaged DAO
     * beans for use in any environment Hibernate has been configured
     * for. Uses HibernateUtil/SessionFactory and Hibernate context
     * propagation (CurrentSessionContext), thread-bound or transaction-bound,
     * and transaction scoped.
     */
   // public static final Class HIBERNATE = org.hibernate.ce.auction.dao.hibernate.HibernateDAOFactory.class;
 
    /**
     * Factory method for instantiation of concrete factories.
     */
    public static DAOFactory instance(Class factory) {
        try {
            return (DAOFactory)factory.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't create DAOFactory: " + factory);
        }
    }
 
    
   
    public abstract SensorDaoInterface getSensorDao();
    
    public abstract MeasurementDaoInterface getMeasurementDao();
    
    public abstract RoomDaoInterface getRoomDao();
    
    public abstract AirConditionerDaoInterface getAirConditionerDao();
    
    public abstract LocalDaoInterface getLocalDao();
    
    
    public abstract UserDaoInterface getUserDao();
    
    public abstract AirConditionerStateDaoInterface getAirStateDao();
    
    public abstract ResourceDaoInterface getResourceDao();
}
