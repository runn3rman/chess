package service;

import dataAccess.GameDao;
import model.GameData;
import java.util.List;

public class GameService {
    private final GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public List<GameData> listGames() {
        return gameDao.listGames();
    }
}
