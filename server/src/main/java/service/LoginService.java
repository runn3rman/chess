package service;

import dataAccess.UserDao;
import dataAccess.AuthTokenDao;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class LoginService {
    private UserDao userDao;
    private AuthTokenDao authTokenDao;

    public LoginService(UserDao userDao, AuthTokenDao authTokenDao) {
        this.userDao = userDao;
        this.authTokenDao = authTokenDao;
    }

    public AuthData login(String username, String password) throws Exception {
        UserData user = userDao.getUser(username);
        if (user != null && user.password().equals(password)) {
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, username);
            authTokenDao.insertAuthToken(authData);
            return authData;
        } else {
            throw new Exception("Invalid username or password"); // Consistent error message
        }
    }

}
