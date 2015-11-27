package domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static domain.User.CHECK;

/**
 * User of application.
 */
@Entity
@Table(name = "users")
@NamedQuery(name = CHECK,
            query = "select u from User u where u.login = :login and u.password = :password")
public class User {
    public static final String CHECK = "User.checkUser";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public User() {
    }

    public User(final String newLogin, final String newPassword) {
        login = newLogin;
        password = getMd5(newPassword);
    }

    public static String getMd5(final String s) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO: 27.11.2015 log it
        }
        byte[] digest = md.digest(s.getBytes());
        BigInteger bigInt = new BigInteger(1, digest);
        return bigInt.toString(16);
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
        password = getMd5(newPassword);
    }

    @Override
    public String toString() {
        return login;
    }
}
