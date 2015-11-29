package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.Client;
import domain.Contract;
import domain.ContractOption;
import domain.ContractTariff;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
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
     *
     * @param id contract's id
     * @return found contract
     */
    public final Contract getById(final long id) {
        return contractDao.getById(Contract.class, id);
    }

    /**
     * Add or update contract in database
     *
     * @param contract contract object
     * @return created or updated contract
     */
    public final Contract upsertContract(final Contract contract) {
        transaction.begin();
        Contract updatedContract = contractDao.merge(contract);
        transaction.commit();

        return updatedContract;
    }

    /**
     * Add or update contract in database
     *
     * @param id      contract's id in database
     * @param client  contract's owner
     * @param number  contract phone number
     * @param tariff  contract's tariff
     * @param options list of activated options
     * @return created or updated contract
     */
    public final Contract upsertContract(final long id, final Client client, final String number, final ContractTariff tariff, final List<ContractOption> options) {
        Contract contract;
        if (id != 0L) {
            contract = getById(id);
        } else {
            contract = new Contract();
        }

        contract.setClient(client);
        contract.setNumber(number);
        contract.setTariff(tariff);
        contract.setActivatedOptions(options);

        transaction.begin();
        Contract updatedContract = contractDao.merge(contract);
        transaction.commit();

        return updatedContract;
    }

    /**
     * Check unique number constraint for contract.
     *
     * @param id     id of checking contract
     * @param number checking number
     * @return true if number is unique
     */
    public boolean hasUniqueNumber(final long id, final String number) {
        List<Contract> resultList = entityManager
                .createNamedQuery(Contract.HAS_UNIQUE_NUMBER, Contract.class)
                .setParameter("id", id)
                .setParameter("number", number).getResultList();
        if (resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    public final List<Contract> getByClient(final Client client) {
        List<Contract> resultList = entityManager
                .createNamedQuery(Contract.GET_BY_CLIENT, Contract.class)
                .setParameter("client", client).getResultList();
        return resultList;
    }

    /**
     * Get list of the options which could be added to the contract
     *
     * @param contract given contract
     * @return list of options
     */
    public final List<ContractOption> getAvailableOptions(final Contract contract) {
        List<ContractOption> options = new ArrayList<>();
        options.addAll(contract.getTariff().getAvailableOptions());
        options.removeAll(contract.getActivatedOptions());
        return options;
    }
}
