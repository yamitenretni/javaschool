package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.ContractOption;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * Created by Лена on 15.11.2015.
 */
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
        optionDao.add(option);
        transaction.commit();
    }

    public List<ContractOption> getOptions() {
        List<ContractOption> options = optionDao.getAll(ContractOption.class);
        return options;
    }

}
