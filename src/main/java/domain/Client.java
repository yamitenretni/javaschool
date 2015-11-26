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

/**
 * Client of mobile operator.
 */
@Entity
@Table(name = "clients")
public class Client {

    /**
     * Automatically generated id of the client.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * User which connected with the client.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * List of contracts (phones) of the client.
     */
    @OneToMany(mappedBy = "client")
    private List<Contract> contracts;

    /**
     * Client's first name.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Client's last name.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Client's birth date.
     */
    @Column(name = "birth_date")
    private Date birthDate;

    /**
     * Client's passport: all data in one line.
     */
    @Column(name = "passport_data")
    private String passportData;

    /**
     * Client's address: all data in one line.
     */
    @Column(name = "address")
    private String address;

    /**
     * The blank constructor for the client.
     */
    public Client() {
    }

    /**
     * The main constructor for the client.
     *
     * @param newUser         user for the new client
     * @param newContracts    list of contracts (phones)
     * @param newFirstName    first name
     * @param newLastName     last name
     * @param newBirthDate    date of birth
     * @param newPassportData passport (one line)
     * @param newAddress      address (one line)
     */
    public Client(final User newUser,
                  final List<Contract> newContracts,
                  final String newFirstName,
                  final String newLastName,
                  final Date newBirthDate,
                  final String newPassportData,
                  final String newAddress) {
        user = newUser;
        contracts = newContracts;
        firstName = newFirstName;
        lastName = newLastName;
        birthDate = newBirthDate;
        passportData = newPassportData;
        address = newAddress;
    }

    /**
     * id getter.
     *
     * @return client's id
     */
    public final long getId() {
        return id;
    }

    /**
     * user getter.
     *
     * @return client's linked user
     */
    public final User getUser() {
        return user;
    }

    /**
     * user setter.
     *
     * @param newUser user which will be linked to client
     */
    public final void setUser(final User newUser) {
        user = newUser;
    }

    /**
     * contracts getter.
     *
     * @return list of client's contracts
     */
    public final List<Contract> getContracts() {
        return contracts;
    }

    /**
     * contracts setter.
     *
     * @param newContracts list of contracts
     */
    public final void setContracts(final List<Contract> newContracts) {
        contracts = newContracts;
    }

    /**
     * first name getter.
     *
     * @return client's first name
     */
    public final String getFirstName() {
        return firstName;
    }

    /**
     * first name setter.
     *
     * @param newFirstName client's first name
     */
    public final void setFirstName(final String newFirstName) {
        firstName = newFirstName;
    }

    /**
     * last name getter.
     *
     * @return client's last name
     */
    public final String getLastName() {
        return lastName;
    }

    /**
     * last name setter.
     *
     * @param newLastName client's last name
     */
    public final void setLastName(final String newLastName) {
        lastName = newLastName;
    }

    /**
     * birth date getter.
     *
     * @return client's birth date
     */
    public final Date getBirthDate() {
        return birthDate;
    }

    /**
     * birth date setter.
     *
     * @param newBirthDate client's birth date
     */
    public final void setBirthDate(final Date newBirthDate) {
        birthDate = newBirthDate;
    }

    /**
     * passport data getter.
     *
     * @return client's passport data
     */
    public final String getPassportData() {
        return passportData;
    }

    /**
     * passport data setter.
     *
     * @param newPassportData client's passport data
     */
    public final void setPassportData(final String newPassportData) {
        passportData = newPassportData;
    }

    /**
     * address getter.
     *
     * @return client's address
     */
    public final String getAddress() {
        return address;
    }

    /**
     * address setter.
     *
     * @param newAddress client's address
     */
    public final void setAddress(final String newAddress) {
        address = newAddress;
    }
}
