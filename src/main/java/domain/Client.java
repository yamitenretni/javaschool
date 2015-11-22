package domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {

    /**
     * Automatically generated id of the client
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * User which connected with the client
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * List of contracts (phones) of the client
     */
    @OneToMany(mappedBy = "client")
    private List<Contract> contracts;

    /**
     * Client's first name
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Client's last name
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Client's birth date
     */
    @Column(name = "birth_date")
    private Date birthDate;

    /**
     * Client's passport: all data in one line
     */
    @Column(name = "passport_data")
    private String passportData;

    /**
     * Client's address: all data in one line
     */
    @Column(name = "address")
    private String address;

    /**
     * The blank constructor for the client
     */
    public Client() {
    }

    /**
     * The main constructor for the client
     * @param newUser user for the new client
     * @param newContracts list of contracts (phones)
     * @param newFirstName first name
     * @param newLastName last name
     * @param newBirthDate date of birth
     * @param newPassportData passport (one line)
     * @param newAddress address (one line)
     */
    public Client(final User newUser, final List<Contract> newContracts, final String newFirstName, final String newLastName, final Date newBirthDate, final String newPassportData, final String newAddress) {
        user = newUser;
        contracts = newContracts;
        firstName = newFirstName;
        lastName = newLastName;
        birthDate = newBirthDate;
        passportData = newPassportData;
        address = newAddress;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User newUser) {
        user = newUser;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(final List<Contract> newContracts) {
        contracts = newContracts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String newFirstName) {
        firstName = newFirstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String newLastName) {
        lastName = newLastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final Date newBirthDate) {
        birthDate = newBirthDate;
    }

    public String getPassportData() {
        return passportData;
    }

    public void setPassportData(final String newPassportData) {
        passportData = newPassportData;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String newAddress) {
        address = newAddress;
    }
}
