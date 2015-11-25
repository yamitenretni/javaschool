package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Provides singleton connection with database.
 */
public final class TransactionManager {
    /**
     * Lazy initialization of TransactionManager object
     */
    private static final TransactionManager TRANSACTION_MANAGER = new TransactionManager();
    /**
     * Entity manager object (captain obvious)
     */
    private EntityManager entityManager;

    /**
     * Private constructor
     */
    private TransactionManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("javaschool");
        entityManager = emf.createEntityManager();
    }

    /**
     * Get singleton TransactionManager
     * @return transaction manager
     */
    public static TransactionManager getInstance() {
        return TRANSACTION_MANAGER;
    }

    /**
     * Get singleton EntityManager
     * @return entity manager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
