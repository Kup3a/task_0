package session;

/**
 * Created by user on 20.10.2015.
 */
public class User {
    private String login;
    private String salt;
    private String password;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    private String nick;

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    private String hashedPassword;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String s, String password, String hashedPassword) {
        this.login = login;
        this.salt = s;
        this.password = password;
        this.hashedPassword = hashedPassword;
    }

    public User() {}

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
}
