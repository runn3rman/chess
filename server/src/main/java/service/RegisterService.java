package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class RegisterService {
    private UserDaoInterface userDao = new MemoryUserDao();
    private AuthTokenDaoInterface authTokenDao = new MemoryAuthTokenDao();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // Add the encoder here

    public AuthData register(UserData newUser) throws DataAccessException {
        // Check if user already exists
        UserData existingUser = userDao.getUser(newUser.username());
        if (existingUser != null) {
            throw new DataAccessException("Username is already taken");
        }

        // Hash the user's password
        String hashedPassword = encoder.encode(newUser.password());

        // Create a new UserData object with the hashed password
        UserData userWithHashedPassword = new UserData(newUser.username(), hashedPassword, newUser.email());

        // If user does not exist, use the DAO to insert the new user with hashed password
        userDao.insertUser(userWithHashedPassword);

        // Generate an authToken for the user
        String authToken = generateAuthToken();

        // Store the authToken associated with the user
        AuthData authData = new AuthData(authToken, newUser.username());
        authTokenDao.insertAuthToken(authData);

        return authData;
    }

    private String generateAuthToken() {
        // Generate a unique authToken using UUID
        return UUID.randomUUID().toString();
    }
}
