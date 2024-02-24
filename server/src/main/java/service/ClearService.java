package service;

import dataAccess.DataAccessException;
import dataAccess.UserDao;
import dataAccess.GameDao;
import dataAccess.AuthTokenDao;

public class ClearService {
    private UserDao userDao;
    private GameDao gameDao;
    private AuthTokenDao authTokenDao;

    // Constructor with DAOs
    public ClearService(UserDao userDao, GameDao gameDao, AuthTokenDao authTokenDao) {
        this.userDao = userDao;
        this.gameDao = gameDao;
        this.authTokenDao = authTokenDao;
    }

    // Method to clear all data
    public void clearAllData() throws DataAccessException {
        try {
            // Clear all users
            userDao.clearUsers();

            // Clear all games
            gameDao.clearGames();

            // Clear all auth tokens
            authTokenDao.clearAuthTokens();
        } catch (Exception e) {
            throw new DataAccessException("Failed to clear all data: " + e.getMessage());
        }
    }
}

