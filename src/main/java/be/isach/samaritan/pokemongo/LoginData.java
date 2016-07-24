package be.isach.samaritan.pokemongo;

/**
 * Package: be.isach.samaritan.pokemongo
 * Created by: sachalewin
 * Date: 24/07/16
 * Project: samaritan
 */
public class LoginData {

    private String username, password;

    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
