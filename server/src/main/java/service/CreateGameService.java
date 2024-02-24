package service;

import dataAccess.GameDao;
import dataAccess.AuthTokenDao;

public class CreateGameService {
    private GameDao gameDao;
    private AuthTokenDao authTokenDao;

    public CreateGameService(GameDao gameDao, AuthTokenDao authTokenDao) {
        this.gameDao = gameDao;
        this.authTokenDao = authTokenDao;
    }

    public int createGame(String authToken, String gameName) throws Exception {
        // Verify authToken is valid
        if (authTokenDao.getAuthData(authToken) == null) {
            throw new Exception("Error: unauthorized");
        }

        // Insert the new game
        return gameDao.insertGame(gameName);
    }
}

