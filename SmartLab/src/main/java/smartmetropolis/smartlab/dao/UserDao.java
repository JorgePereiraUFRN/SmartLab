package smartmetropolis.smartlab.dao;

import smartmetropolis.smartlab.model.User;

public class UserDao extends GenericHibernateDAO<User, String> implements UserDaoInterface{

	public UserDao() {
		super(User.class);
	}

}
