package domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

// TODO: 15.11.2015 set const CHECK_USER = "User.checkUser"

@Entity
@Table(name = "users")
@NamedQuery(name = "User.checkUser",
            query = "select u from User u where u.login = :login and u.password = :password")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public User(final String newLogin, final String newPassword) {
        login = newLogin;
        password = newPassword;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String newLogin) {
        login = newLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String newPassword) {
        password = newPassword;
    }

    @Override
    public String toString() {
        return login;
    }
}
