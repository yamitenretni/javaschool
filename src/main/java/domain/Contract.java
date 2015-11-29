package domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "contracts")
@NamedQueries({
        @NamedQuery(name = Contract.HAS_UNIQUE_NUMBER,
                query = "select c from Contract c where c.id <> :id and c.number = :number"),
        @NamedQuery(name = Contract.GET_BY_CLIENT,
                query = "select c from Contract c where c.client = :client"),
        @NamedQuery(name = Contract.GET_BY_NUMBER,
                query = "select c from Contract c where c.number = :number")
})

public class Contract {
    public static final String HAS_UNIQUE_NUMBER = "Contract.hasUniqueNumber";
    public static final String GET_BY_CLIENT = "Contract.getByClient";
    public static final String GET_BY_NUMBER = "Contract.getByNumber";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "number", unique = true)
    private String number;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private ContractTariff tariff;

    @ManyToMany
    @JoinTable(name = "contract_options",
            joinColumns = {@JoinColumn(name = "contract_id")},
            inverseJoinColumns = {@JoinColumn(name = "option_id")})
    private List<ContractOption> activatedOptions;

    @Column(name = "blocking_date")
    private Date blockingDate;

    @ManyToOne
    @JoinColumn(name = "blocking_user")
    private User blockingUser;

    public Contract() {
    }

    public Contract(final Client newClient, final String newNumber, final ContractTariff newTariff, final List<ContractOption> newActivatedOptions) {
        client = newClient;
        number = newNumber;
        tariff = newTariff;
        activatedOptions = newActivatedOptions;
    }

    public final boolean isBlocked() {
        if (blockingDate == null && blockingUser == null) {
            return false;
        }
        return true;
    }

    public long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(final Client newClient) {
        client = newClient;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String newNumber) {
        number = newNumber;
    }

    public ContractTariff getTariff() {
        return tariff;
    }

    public void setTariff(final ContractTariff newTariff) {
        tariff = newTariff;
    }

    public List<ContractOption> getActivatedOptions() {
        return activatedOptions;
    }

    public void setActivatedOptions(final List<ContractOption> newActivatedOptions) {
        activatedOptions = newActivatedOptions;
    }

    public Date getBlockingDate() {
        return blockingDate;
    }

    public void setBlockingDate(final Date newBlockingDate) {
        blockingDate = newBlockingDate;
    }

    public User getBlockingUser() {
        return blockingUser;
    }

    public void setBlockingUser(final User newBlockingUser) {
        blockingUser = newBlockingUser;
    }
}
