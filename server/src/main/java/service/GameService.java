package service;

import dataAccess.GameDaoInterface;
import model.GameData;
import java.util.List;

public class GameService {
    private final GameDaoInterface gameDao;

    public GameService(GameDaoInterface gameDao) {
        this.gameDao = gameDao;
    }

    public List<GameData> listGames() {
        return gameDao.listGames();
    }
}
