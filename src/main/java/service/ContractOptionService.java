package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.ContractOption;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Implements main methods for working with options in database.
 */
public class ContractOptionService {

    /**
     * Get entity manager from TransactionManager
     */
    private EntityManager entityManager = TransactionManager.getInstance().getEntityManager();
    /**
     * Get transaction from entity manager
     */
    private EntityTransaction transaction = entityManager.getTransaction();

    /**
     * Create DAO for options
     */
    private BaseDAO<ContractOption> optionDao = new BaseDAOImpl<>();

    /**
     * Get option by id from database
     * @param id option's id
     * @return founded option
     */
    public final ContractOption getById(final long id) {
        ContractOption option = optionDao.getById(ContractOption.class, id);
        return option;
    }

    /**
     * Add option in database
     * @param name option's name
     * @param connectionCost option's connection price
     * @param monthlyCost option's regular price
     */
     public final void addOption(final String name, final double connectionCost, final double monthlyCost) {
        ContractOption option = new ContractOption();
        option.setName(name);
        option.setConnectionCost(connectionCost);
        option.setMonthlyCost(monthlyCost);

        transaction.begin();
        optionDao.merge(option);
        transaction.commit();
    }

    /**
     * Delete option with the specified id
     * @param id option's id
     */
    public final void deleteOption(final long id) {
        ContractOption option = getById(id);
        option.setDeleted(true);

        transaction.begin();
        optionDao.merge(option);
        transaction.commit();
    }

    /**
     * Returns all active (not deleted) options from database
     * @return list of options
     */
    public final List<ContractOption> getActiveOptions() {
        TypedQuery<ContractOption> namedQuery = entityManager
                .createNamedQuery("ContractOption.getAllActive", ContractOption.class);
        List<ContractOption> options = namedQuery.getResultList();

        return options;
    }

}
