package smartmetropolis.smartlab.controller;

import java.util.List;

import smartmetropolis.smartlab.dao.ConcreteDaoFactory;
import smartmetropolis.smartlab.dao.DAOFactory;
import smartmetropolis.smartlab.dao.UserDaoInterface;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.UserAlreadyExistsException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.User;

public class UserController {

	private final DAOFactory factory = new ConcreteDaoFactory();
	private final UserDaoInterface userDao;
	private static final UserController userController = new UserController();

	private UserController() {
		userDao = factory.getUserDao();
	}

	public static synchronized UserController getInstance() {
		return userController;
	}

	private void validateUser(User user) throws validateDataException {

		if (user == null) {
			throw new validateDataException("User is null");
		} else if (user.getLogin() == null || user.getLogin().equals("")) {
			throw new validateDataException("Invalid login");
		} else if (user.getPassword() == null || user.getPassword().equals("")) {
			throw new validateDataException("Invalid password");
		}else if(user.getRole() == null ||  user.getRole().getRole() == null ||  user.getRole().getRole().equals("") ){
			throw new validateDataException("Invalid user role.");
		}

	}

	public User findUser(String login) throws DAOException {
		return userDao.findById( login);
	}

	public User saveUser(User user) throws UserAlreadyExistsException,
			DAOException, validateDataException {

		validateUser(user);

		if (findUser(user.getLogin()) != null) {
			throw new UserAlreadyExistsException(
					"Exists already a user with login: "
							+ user.getLogin());
		}

		return userDao.save(user);
	}

	public User updateUser(User user) throws validateDataException,
			DAOException {
		validateUser(user);

		return userDao.update(user);
	}

	public void removeUser(String login) throws DAOException {
			userDao.delete(login);
		
	}

	public List<User> findAllUsers() throws DAOException {
		return userDao.findAll();
	}
}
