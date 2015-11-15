package domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "client")
    private List<Contract> contracts;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "passport_data")
    private String passportData;

    @Column(name = "address")
    private String address;

    public Client() {
    }

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
