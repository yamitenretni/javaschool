package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * Implements main methods for working with users in database.
 */
public class UserService {

    private EntityManager entityManager = TransactionManager.getInstance().getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    private BaseDAO<User> userDao = new BaseDAOImpl<>();

    public User addUser(final User user) {
        transaction.begin();
        User updatedUser = userDao.merge(user);
        transaction.commit();

        return updatedUser;
    }

    public void addUser(final String login, final String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);

        transaction.begin();
        userDao.merge(user);
        transaction.commit();
    }

    public List<User> getUsers() {
        List<User> users = userDao.getAll(User.class);
        return users;
    }

    public boolean checkUser(String login, String password) {
        List<User> resultList = entityManager
                .createNamedQuery(User.CHECK, User.class)
                .setParameter("login", login)
                .setParameter("password", User.getMd5(password)).getResultList();

        if (!resultList.isEmpty()) {
            return true;
        }
        return false;
    }

}
