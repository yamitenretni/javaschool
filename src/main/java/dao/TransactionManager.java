package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Created by Лена on 08.11.2015.
 */
public class TransactionManager {
    private static final TransactionManager transactionManager = new TransactionManager();
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    private TransactionManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("javaschool");
        entityManager = emf.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    public static TransactionManager getInstance() {
        return transactionManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntityTransaction getEntityTransaction() {
        return entityTransaction;
    }

}
