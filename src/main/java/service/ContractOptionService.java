package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.ContractOption;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class ContractOptionService {

    private EntityManager entityManager = TransactionManager.getInstance().getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    private BaseDAO<ContractOption> optionDao = new BaseDAOImpl<ContractOption>();

    public ContractOption getById(final long id) {
        ContractOption option = optionDao.getById(ContractOption.class, id);
        return option;
    }

    public void addOption(final String name, final double connectionCost, final double monthlyCost) {
        ContractOption option = new ContractOption();
        option.setName(name);
        option.setConnectionCost(connectionCost);
        option.setMonthlyCost(monthlyCost);

        transaction.begin();
        optionDao.merge(option);
        transaction.commit();
    }

    public void deleteOption(final long id) {
        ContractOption option = new ContractOption();
        option.setDeleted(true);

        transaction.begin();
        optionDao.merge(option);
        transaction.commit();
    }

    public List<ContractOption> getActiveOptions() {
        TypedQuery<ContractOption> namedQuery = entityManager
                .createNamedQuery("ContractOption.getAllActive", ContractOption.class);
        List<ContractOption> options = namedQuery.getResultList();

        return options;
    }

}
