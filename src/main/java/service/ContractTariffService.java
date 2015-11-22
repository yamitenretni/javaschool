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

    public List<ContractTariff> getActiveTariffs() {
        TypedQuery<ContractTariff> namedQuery = entityManager
                .createNamedQuery("ContractTariff.getAllActive", ContractTariff.class);
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
