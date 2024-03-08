package service;

import dataAccess.AuthTokenDaoInterface;
import dataAccess.UserDaoInterface;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class LoginService {
    private UserDaoInterface userDao;
    private AuthTokenDaoInterface authTokenDao;
    private BCryptPasswordEncoder encoder;

    public LoginService(UserDaoInterface userDao, AuthTokenDaoInterface authTokenDao) {
        this.userDao = userDao;
        this.authTokenDao = authTokenDao;
        this.encoder = new BCryptPasswordEncoder(); // Initialize the password encoder
    }

    public AuthData login(String username, String password) throws Exception {
        UserData user = userDao.getUser(username);

        // Verify the password with the hashed password stored in the database
        if (user != null && encoder.matches(password, user.password())) { //user.password().equals(password)
            // Generate a new auth token for the session
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, username);

            // Insert the new auth token into the database
            authTokenDao.insertAuthToken(authData);
            return authData;
        } else {
            // Throw an exception if the username doesn't exist or the password doesn't match
            throw new Exception("Invalid username or password"); // Consistent error message
        }
    }

    // Additional methods as needed...
}
