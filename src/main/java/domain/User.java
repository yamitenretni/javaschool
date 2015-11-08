package domain;

import javax.persistence.*;

/**
 * Created by Лена on 08.11.2015.
 */

@Entity
@Table(name = "users")
@NamedQuery(name = "User.checkUser", query = "select u from User u where u.login = :login and u.password = :password")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "login", length = 32)
    private String login;

    @Column(name = "password", length = 32)
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return login;
    }
}
