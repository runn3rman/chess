package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {
    private UserDao userDao = new UserDao();
    private AuthTokenDao authTokenDao = new AuthTokenDao(); // Assuming you have a DAO for AuthTokens

    public AuthData register(UserData newUser) throws DataAccessException {
        // Check if user already exists
        UserData existingUser = userDao.getUser(newUser.username());
        if (existingUser != null) {
            throw new DataAccessException("Username is already taken");
        }

        // If user does not exist, use the DAO to insert the new user
        userDao.insertUser(newUser);

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
