package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.ContractOption;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     *
     * @param id option's id
     * @return found option
     */
    public final ContractOption getById(final long id) {
        ContractOption option = optionDao.getById(ContractOption.class, id);
        return option;
    }

    /**
     * Add option in database
     *
     * @param name           option's name
     * @param connectionCost option's connection price
     * @param monthlyCost    option's regular price
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
     * Add or update option in database.
     *
     * @param option updating option
     * @return updated option
     */
    public final ContractOption upsertOption(final ContractOption option) {
        transaction.begin();
        ContractOption updatedOption = optionDao.merge(option);
        transaction.commit();

        return updatedOption;
    }

    /**
     * Add or update option in database
     *
     * @param id             option's id in database, if =0L then new option will be inserted in database
     * @param name           option's name
     * @param connectionCost option's connection price
     * @param monthlyCost    option's regular price
     */
    public final ContractOption upsertOption(final long id, final String name, final double connectionCost, final double monthlyCost) {
        ContractOption option;
        if (id != 0L) {
            option = optionDao.getById(ContractOption.class, id);
        } else {
            option = new ContractOption();
        }
        option.setName(name);
        option.setConnectionCost(connectionCost);
        option.setMonthlyCost(monthlyCost);

        transaction.begin();
        ContractOption updatedOption = optionDao.merge(option);
        transaction.commit();

        return updatedOption;
    }

    /**
     * Check unique name constraint for option.
     *
     * @param id   id of checking option
     * @param name checking name
     * @return true if option doesn't violate unique constraints
     */
    public final boolean hasUniqueName(final long id, final String name) {
        List<ContractOption> resultList = entityManager
                .createNamedQuery(ContractOption.HAS_UNIQUE_NAME, ContractOption.class)
                .setParameter("id", id)
                .setParameter("name", name).getResultList();
        if (resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Delete option with the specified id
     *
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
     *
     * @return list of options
     */
    public final List<ContractOption> getActiveOptions() {
        TypedQuery<ContractOption> namedQuery = entityManager
                .createNamedQuery(ContractOption.GET_ACTIVE, ContractOption.class);
        List<ContractOption> options = namedQuery.getResultList();

        return options;
    }

    /**
     * Add pair of incompatible options in the database and needed objects.
     *
     * @param firstOption  first option in the pair
     * @param secondOption second option in the pair
     */
    private void addIncompatibleOptions(final ContractOption firstOption, final ContractOption secondOption) {
        Set<ContractOption> firstIncompatibleOptions = firstOption.getIncompatibleOptions();
        Set<ContractOption> secondIncompatibleOptions = secondOption.getIncompatibleOptions();

        if (!firstIncompatibleOptions.contains(secondOption)) {
            firstIncompatibleOptions.add(secondOption);
        }
        if (!secondIncompatibleOptions.contains(firstOption)) {
            secondIncompatibleOptions.add(firstOption);
        }

        transaction.begin();
        optionDao.merge(firstOption);
        optionDao.merge(secondOption);
        transaction.commit();
    }

    /**
     * Remove pair of incompatible options in the database and needed objects.
     *
     * @param firstOption  first option in the pair
     * @param secondOption second option in the pair
     */
    private void deleteIncompatibleOptions(final ContractOption firstOption, final ContractOption secondOption) {
        Set<ContractOption> firstIncompatibleOptions = firstOption.getIncompatibleOptions();
        Set<ContractOption> secondIncompatibleOptions = secondOption.getIncompatibleOptions();

        firstIncompatibleOptions.remove(secondOption);
        secondIncompatibleOptions.remove(firstOption);

        transaction.begin();
        optionDao.merge(firstOption);
        optionDao.merge(secondOption);
        transaction.commit();
    }

    /**
     * Update lists of incompatible options for all needed options.
     *
     * @param option        updating option
     * @param newIncOptions new list of options
     */
    public final void updateIncompatibleList(final ContractOption option, final List<ContractOption> newIncOptions) {
        Set<ContractOption> currentIncOptions = option.getIncompatibleOptions();

        Set<ContractOption> removingOptions = new HashSet<>(currentIncOptions);
        removingOptions.removeAll(newIncOptions);

        Set<ContractOption> newOptions = new HashSet<>(newIncOptions);
        newOptions.removeAll(currentIncOptions);

        for (ContractOption removingOption : removingOptions) {
            deleteIncompatibleOptions(option, removingOption);
        }
        for (ContractOption newOption : newOptions) {
            addIncompatibleOptions(option, newOption);
        }
    }

    /**
     * Add pair of mandatory-depend options in the database and needed objects.
     *
     * @param dependOption    depend option
     * @param mandatoryOption mandatory option
     */
    private void addMandatoryOption(final ContractOption dependOption, final ContractOption mandatoryOption) {
        dependOption.getMandatoryOptions().add(mandatoryOption);
        mandatoryOption.getDependOptions().add(dependOption);

        transaction.begin();
        optionDao.merge(dependOption);
        transaction.commit();
    }

    /**
     * Remove pair of mandatory-depend options in the database and needed objects.
     *
     * @param dependOption    depend option
     * @param mandatoryOption mandatory option
     */
    private void deleteMandatoryOption(final ContractOption dependOption, final ContractOption mandatoryOption) {
        dependOption.getMandatoryOptions().remove(mandatoryOption);
        mandatoryOption.getDependOptions().remove(dependOption);

        transaction.begin();
        optionDao.merge(dependOption);
        transaction.commit();
    }

    /**
     * Update lists of mandatory and depend options for all needed options.
     *
     * @param option         updating option
     * @param newMandOptions new list of options
     */
    public final void updateMandatoryList(final ContractOption option, final List<ContractOption> newMandOptions) {
        Set<ContractOption> currentMandOptions = option.getMandatoryOptions();

        Set<ContractOption> removingOptions = new HashSet<>(currentMandOptions);
        removingOptions.removeAll(newMandOptions);

        Set<ContractOption> newOptions = new HashSet<>(newMandOptions);
        newOptions.removeAll(currentMandOptions);

        for (ContractOption removingOption : removingOptions) {
            deleteMandatoryOption(option, removingOption);
        }
        for (ContractOption newOption : newOptions) {
            addMandatoryOption(option, newOption);
        }
    }

}
