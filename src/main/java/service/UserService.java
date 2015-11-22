package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserService {

    private EntityManager entityManager = TransactionManager.getInstance().getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    private BaseDAO<User> userDao = new BaseDAOImpl<User>();

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
        TypedQuery<User> namedQuery = entityManager
                .createNamedQuery("User.checkUser", User.class)
                .setParameter("login", login)
                .setParameter("password", password);

        if (!namedQuery.getResultList().isEmpty()) {
            return true;
        }
        return false;
    }

}
