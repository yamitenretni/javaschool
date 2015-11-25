package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.Client;
import domain.Contract;
import domain.ContractOption;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * Implements main methods for working with clients in database.
 */
public class ContractService {
    /**
     * Get entity manager from TransactionManager
     */
    private EntityManager entityManager = TransactionManager.getInstance().getEntityManager();
    /**
     * Get transaction from entity manager
     */
    private EntityTransaction transaction = entityManager.getTransaction();

    /**
     * Create DAO for clients
     */
    private BaseDAO<Contract> contractDao = new BaseDAOImpl<>();

    /**
     * Get contract by id from database
     * @param id contract's id
     * @return found contract
     */
    public final Contract getById(final long id) {
        return contractDao.getById(Contract.class, id);
    }

    /**
     * Add or update contract in database
     * @param id contract's id in database
     * @param number contract phone number
     * @param client contract's owner
     * @param options list of activated options
     * @return created or updated contract
     */
    public final Contract upsertContract(final long id, final String number, final Client client, final List<ContractOption> options) {
        Contract contract;
        if (id != 0L) {
            contract = getById(id);
        }
        else {
            contract = new Contract();
        }

        contract.setNumber(number);
        contract.setClient(client);
        contract.setActivatedOptions(options);

        transaction.begin();
        contractDao.merge(contract);
        transaction.commit();

        return contract;
    }
}
