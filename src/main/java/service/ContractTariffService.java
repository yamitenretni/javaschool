package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.ContractOption;
import domain.ContractTariff;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class ContractTariffService {

    private EntityManager entityManager = TransactionManager.getInstance().getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    private BaseDAO<ContractTariff> tariffDao = new BaseDAOImpl<ContractTariff>();

    public void addTariff(final String name, final double monthlyCost, final List<ContractOption> availableOptions) {
        ContractTariff tariff = new ContractTariff();

        List<ContractOption> options = new ArrayList<ContractOption>();
        if (!availableOptions.isEmpty()) {
            options = availableOptions;
        }

        tariff.setName(name);
        tariff.setMonthlyCost(monthlyCost);
        tariff.setAvailableOptions(options);

        transaction.begin();
        tariffDao.merge(tariff);
        transaction.commit();
    }

    public void updateTariff(final long id, final String name, final double monthlyCost, final List<ContractOption> availableOptions) {
        ContractTariff tariff = getById(id);
        tariff.setName(name);
        tariff.setMonthlyCost(monthlyCost);
        tariff.setAvailableOptions(availableOptions);

        transaction.begin();
        tariffDao.merge(tariff);
        transaction.commit();
    }

    public void upsertTariff(final long id, final String name, final double monthlyCost, final List<ContractOption> availableOptions) {
        ContractTariff tariff;
        if (id != 0L) {
            tariff = getById(id);
        } else {
            tariff = new ContractTariff();
        }

            tariff.setName(name);
            tariff.setMonthlyCost(monthlyCost);
            tariff.setAvailableOptions(availableOptions);

            transaction.begin();
            tariffDao.merge(tariff);
            transaction.commit();
    }

    /**
     * Check unique name constraint for tariff.
     *
     * @param id   id of checking tariff
     * @param name checking name
     * @return true if tariff doesn't violate unique constraints
     */
    public boolean hasUniqueName(final long id, final String name) {
        List<ContractTariff> resultList = entityManager
                .createNamedQuery(ContractTariff.HAS_UNIQUE_NAME, ContractTariff.class)
                .setParameter("id", id)
                .setParameter("name", name).getResultList();
        if (resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<ContractTariff> getActiveTariffs() {
        TypedQuery<ContractTariff> namedQuery = entityManager
                .createNamedQuery(ContractTariff.GET_ACTIVE, ContractTariff.class);
        List<ContractTariff> tariffs = namedQuery.getResultList();
        return tariffs;
    }

    public ContractTariff getById(final long id) {
        return tariffDao.getById(ContractTariff.class, id);
    }

    public void deleteTariff(final long id) {
        ContractTariff tariff = getById(id);
        tariff.setDeleted(true);

        transaction.begin();
        tariffDao.merge(tariff);
        transaction.commit();

    }
}
