package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.*;
import form.CartContractForm;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.*;

/**
 * Implements main methods for working with clients in database.
 */
public class ContractService {
    /**
     * Get entity manager from TransactionManager.
     */
    private EntityManager entityManager = TransactionManager.getInstance().getEntityManager();
    /**
     * Get transaction from entity manager.
     */
    private EntityTransaction transaction = entityManager.getTransaction();

    /**
     * Create DAO for clients.
     */
    private BaseDAO<Contract> contractDao = new BaseDAOImpl<>();

    /**
     * Get service for tariffs.
     */
    private static final ContractTariffService TARIFF_SVC = new ContractTariffService();

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
    public final boolean hasUniqueNumber(final long id, final String number) {
        List<Contract> resultList = entityManager
                .createNamedQuery(Contract.HAS_UNIQUE_NUMBER, Contract.class)
                .setParameter("id", id)
                .setParameter("number", number).getResultList();
        if (resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Block contract since current date.
     *
     * @param contract  blocking contract
     * @param blockUser user, who block contract
     * @return blocked contract
     */
    public final Contract blockContract(final Contract contract, final User blockUser) {
        contract.setBlockingDate(new Date());
        contract.setBlockingUser(blockUser);

        transaction.begin();
        Contract blockedContract = contractDao.merge(contract);
        transaction.commit();

        return blockedContract;
    }

    /**
     * Unlock given contract.
     *
     * @param contract unlocking contract
     * @return unlocked contract
     */
    public final void unlockContract(final Contract contract) {
        if (contract.getClient().getBlockingDate() == null) {
            contract.setBlockingDate(null);
            contract.setBlockingUser(null);

            transaction.begin();
            contractDao.merge(contract);
            transaction.commit();
        }

    }

    /**
     * Get contracts of the given client.
     *
     * @param client client for the search
     * @return list of client's contracts
     */
    public final List<Contract> getByClient(final Client client) {
        List<Contract> resultList = entityManager
                .createNamedQuery(Contract.GET_BY_CLIENT, Contract.class)
                .setParameter("client", client).getResultList();
        return resultList;
    }

    /**
     * Get contract with given number.
     *
     * @param number number of contract
     * @return found contract
     */
    public final Contract getByNumber(final String number) {
        List<Contract> resultList = entityManager
                .createNamedQuery(Contract.GET_BY_NUMBER, Contract.class)
                .setParameter("number", number).getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    /**
     * Get list of available tariffs for the contract and current cart position.
     *
     * @param contract     given contract
     * @param cartContract current cart position
     * @return list of tariffs
     */
    public final List<ContractTariff> getAvailableTariffs(final Contract contract, final CartContractForm cartContract) {
        List<ContractTariff> resultList = new ArrayList<>();
        resultList.addAll(TARIFF_SVC.getActiveTariffs());
        resultList.remove(contract.getTariff());
        if (cartContract != null && cartContract.getNewTariff() != null) {
            resultList.remove(cartContract.getNewTariff());
        }
        return resultList;
    }

    /**
     * Get list of the options which could be added to the contract with given cart position.
     *
     * @param contract     given contract
     * @param cartContract current cart position
     * @return list of options
     */
    public final List<ContractOption> getAvailableOptions(final Contract contract, final CartContractForm cartContract) {
        List<ContractOption> availableOptions = getAvailableOptionsForTariff(contract, cartContract);
        List<ContractOption> activeOptions = getActiveOptionsForContract(contract, cartContract);

        availableOptions.removeAll(getIncompatibleOptions(contract, cartContract).keySet());
        availableOptions.removeAll(getDependOptions(contract, cartContract).keySet());
//        for (ContractOption option : activeOptions) {
//            availableOptions.removeAll(option.getIncompatibleOptions());
//        }
        availableOptions.removeAll(activeOptions);
        availableOptions.removeAll(contract.getActivatedOptions());

        return availableOptions;
    }

    /**
     * Get list of options, which disabled for given contracts with current cart position.
     *
     * @param contract     given contract object
     * @param cartContract current cart position
     * @return map, where key is disable option and value is the list of obstructive options
     */
    public final Map<ContractOption, List<ContractOption>> getIncompatibleOptions(final Contract contract, final CartContractForm cartContract) {
        Map<ContractOption, List<ContractOption>> resultMap = new HashMap<>();
        List<ContractOption> availableOptions = getAvailableOptionsForTariff(contract, cartContract);
        List<ContractOption> activeOptions = getActiveOptionsForContract(contract, cartContract);
        List<ContractOption> incompatibleOptions = new ArrayList<>();

        for (ContractOption activeOption : activeOptions) {
            incompatibleOptions.addAll(activeOption.getIncompatibleOptions());
        }

        incompatibleOptions.retainAll(availableOptions);
        incompatibleOptions.removeAll(contract.getActivatedOptions());

        for (ContractOption incOption : incompatibleOptions) {
            List<ContractOption> obstructiveOptions = new ArrayList<>(incOption.getIncompatibleOptions());
            obstructiveOptions.retainAll(activeOptions);
            resultMap.put(incOption, obstructiveOptions);
        }

        return resultMap;
    }

    /**
     * Get list of options, which disabled for given contracts with current cart position.
     *
     * @param contract     given contract object
     * @param cartContract current cart position
     * @return map, where key is disable option and value is the list of mandatory options
     */
    public final Map<ContractOption, List<ContractOption>> getDependOptions(final Contract contract, final CartContractForm cartContract) {
        Map<ContractOption, List<ContractOption>> resultMap = new HashMap<>();
        List<ContractOption> availableOptions = getAvailableOptionsForTariff(contract, cartContract);
        List<ContractOption> activeOptions = getActiveOptionsForContract(contract, cartContract);
        List<ContractOption> dependOptions = new ArrayList<>();

        for (ContractOption availableOption : availableOptions) {
            boolean isDepend = true;
            Set<ContractOption> mandatoryOptions = availableOption.getMandatoryOptions();
            if (mandatoryOptions.size() == 0) {
                isDepend = false;
            }
            else {
                for (ContractOption mandatoryOption : mandatoryOptions) {
                    if (activeOptions.contains(mandatoryOption)) {
                        isDepend = false;
                        break;
                    }
                }
            }
            if (isDepend) {
                dependOptions.add(availableOption);
            }
        }

        for (ContractOption depOption : dependOptions) {
            List<ContractOption> mandatoryOptions = new ArrayList<>(depOption.getMandatoryOptions());
            mandatoryOptions.retainAll(availableOptions);
            resultMap.put(depOption, mandatoryOptions);
        }

        return resultMap;
    }



    private List<ContractOption> getAvailableOptionsForTariff(final Contract contract, final CartContractForm cartContract) {
        List<ContractOption> availableOptions = new ArrayList<>();
        if (cartContract != null && cartContract.getNewTariff() != null) {
            availableOptions.addAll(cartContract.getNewTariff().getAvailableOptions());
        }
        else {
            availableOptions.addAll(contract.getTariff().getAvailableOptions());
        }

        return availableOptions;
    }

    private List<ContractOption> getActiveOptionsForContract(final Contract contract, final CartContractForm cartContract) {
        List<ContractOption> activeOptions = new ArrayList<>();
        if (cartContract != null) {
            activeOptions.addAll(cartContract.getFutureOptionList());

        }
        else {
            activeOptions.addAll(contract.getActivatedOptions());
        }

        return activeOptions;
    }
}
