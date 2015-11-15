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

/**
 * Created by Лена on 15.11.2015.
 */
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
        tariffDao.add(tariff);
        transaction.commit();
    }

    public int addTariffOption(final ContractTariff tariff, final ContractOption option) {

        TypedQuery<ContractTariff> namedQuery = entityManager
                .createNamedQuery("ContractTariff.addAvailableOption", ContractTariff.class)
                .setParameter("tariff", tariff.getId())
                .setParameter("option", option.getId());
        return namedQuery.executeUpdate();
    }

    public List<ContractTariff> getTariffs() {
        List<ContractTariff> tariffs = tariffDao.getAll(ContractTariff.class);
        return tariffs;
    }
}
