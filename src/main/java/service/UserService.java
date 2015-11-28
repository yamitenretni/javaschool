package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.Role;
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

    public void addUser(final String login, final String password, final Role role) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);

        transaction.begin();
        userDao.merge(user);
        transaction.commit();
    }

    /**
     * Check unique login constraint for user.
     *
     * @param id    id of checking user
     * @param login checking login
     * @return true if user doesn't violate unique constraints
     */
    public boolean hasUniqueLogin(final long id, final String login) {
        List<User> resultList = entityManager
                .createNamedQuery(User.HAS_UNIQUE_LOGIN, User.class)
                .setParameter("id", id)
                .setParameter("login", login).getResultList();
        if (resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<User> getUsers() {
        List<User> users = userDao.getAll(User.class);
        return users;
    }

    public User getById(final long userId) {
        return userDao.getById(User.class, userId);
    }

    public long checkUser(String login, String password) {
        List<User> resultList = entityManager
                .createNamedQuery(User.CHECK, User.class)
                .setParameter("login", login)
                .setParameter("password", User.getMd5(password)).getResultList();

        if (!resultList.isEmpty()) {
            return resultList.get(0).getId();
        }
        return 0L;
    }

}
