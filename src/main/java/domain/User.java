package domain;

import javax.persistence.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static domain.User.CHECK;

/**
 * User of application.
 */
@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = CHECK,
                query = "select u from User u where u.login = :login and u.password = :password"),
        @NamedQuery(name = "User.hasUniqueLogin",
                query = "select u from User u where u.id <> :id and u.login = :login")
})
public class User {
    public static final String CHECK = "User.checkUser";
    public static final String HAS_UNIQUE_LOGIN = "User.hasUniqueLogin";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Role role;


    public User() {
    }

    public User(final String newLogin, final String newPassword, final Role newRole) {
        login = newLogin;
        password = getMd5(newPassword);
        role = newRole;
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

    public Role getRole() {
        return role;
    }

    public void setRole(final Role newRole) {
        role = newRole;
    }

    @Override
    public String toString() {
        return login;
    }
}
