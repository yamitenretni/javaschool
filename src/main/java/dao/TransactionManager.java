package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class TransactionManager {
    private static final TransactionManager TRANSACTION_MANAGER = new TransactionManager();
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    private TransactionManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("javaschool");
        entityManager = emf.createEntityManager();
        //entityTransaction = entityManager.getTransaction();
    }

    public static TransactionManager getInstance() {
        return TRANSACTION_MANAGER;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

//    public EntityTransaction getEntityTransaction() {
//        return entityTransaction;
//    }

}
