package service;

import dao.BaseDAO;
import dao.BaseDAOImpl;
import dao.TransactionManager;
import domain.Client;
import domain.Contract;
import domain.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Date;
import java.util.List;

/**
 * Implements main methods for working with clients in database.
 */
public class ClientService {

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
    private BaseDAO<Client> clientDao = new BaseDAOImpl<>();

    /**
     * Get client by id from database
     * @param id client's id
     * @return found client
     */
    public final Client getById(final long id) {
        return clientDao.getById(Client.class, id);
    }

    /**
     * Add or update client's common info
     * @param id
     * @param firstName
     * @param lastName
     * @param birthDate
     * @param passportData
     * @return created or updated client
     */
    public final Client upsertClient (final long id, final String firstName, final String lastName, final Date birthDate, final String passportData) {
        Client client;
        if (id != 0L) {
            client = getById(id);
        }
        else {
            client = new Client();
        }

        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setBirthDate(birthDate);
        client.setPassportData(passportData);

        transaction.begin();
        clientDao.merge(client);
        transaction.commit();

        return client;
    }

    /**
     * Add link between client and user in database
     * @param clientId client's id in database
     * @param user object contains user's data
     */
    public final void addUser(final long clientId, final User user) {
        Client client = getById(clientId);
        client.setUser(user);

        transaction.begin();
        clientDao.merge(client);
        transaction.commit();
    }

    /**
     * Update contract list of client in database
     * @param clientId client's id in database
     * @param contracts list of contracts
     */
    public final void updateContractList(final long clientId, final List<Contract> contracts) {
        Client client = getById(clientId);
        client.setContracts(contracts);

        transaction.begin();
        clientDao.merge(client);
        transaction.commit();
    }

    /**
     * Get all clients from database
     * @return list of clients
     */
    public final List<Client> getClients() {
        return clientDao.getAll(Client.class);
    }
}
