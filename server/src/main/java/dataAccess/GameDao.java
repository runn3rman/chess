package dataAccess;

import model.GameData;
import chess.ChessGame; // Ensure this import matches the location of your ChessGame class

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameDao {
    private final HashMap<Integer, GameData> games = new HashMap<>();
    private final AtomicInteger gameIDCounter = new AtomicInteger();

    public int insertGame(String gameName) {
        int gameID = gameIDCounter.incrementAndGet();
        // Initialize whiteUsername, blackUsername as null or empty, and create a new ChessGame instance
        GameData game = new GameData(gameID, "", "", gameName, new ChessGame());
        games.put(gameID, game);
        return gameID;
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public List<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    public void updateGame(int gameID, GameData updatedGame) {
        if (games.containsKey(gameID)) {
            games.put(gameID, updatedGame);
        }
    }

    public void clearGames() {
        games.clear();
    }

    // You might need additional methods for game-specific operations, like joining a game, making a move, etc.
}
