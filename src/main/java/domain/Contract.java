package domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "number")
    private String number;

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

    public Contract(final Client newClient, final String newNumber, final List<ContractOption> newActivatedOptions) {
        client = newClient;
        number = newNumber;
        activatedOptions = newActivatedOptions;
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
