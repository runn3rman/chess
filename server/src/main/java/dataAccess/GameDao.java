package dataAccess;

import model.GameData;
import chess.ChessGame;
import dataAccess.AuthTokenDao;

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
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame()); //switched from empty to null
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

    public boolean joinGame(int gameID, String username, String playerColor) {
        GameData game = games.get(gameID);
        if (game == null) return false; // Game does not exist

        if ("WHITE".equalsIgnoreCase(playerColor) && (game.getWhiteUsername() == null || game.getWhiteUsername().isEmpty())) {
            game.setWhiteUsername(username);
            games.put(gameID, game);
            return true;
        } else if ("BLACK".equalsIgnoreCase(playerColor) && (game.getBlackUsername() == null || game.getBlackUsername().isEmpty())) {
            game.setBlackUsername(username);
            games.put(gameID, game);
            return true;
        } else {
            // Add as observer
            game.addObserver(username);
            return true; // Successfully added as observer
        }
    }

    public boolean isColorTaken(int gameID, String playerColor) {
        GameData game = games.get(gameID);
        if (game == null) return false; // Game does not exist

        if ("WHITE".equalsIgnoreCase(playerColor)) {
            return game.getWhiteUsername() != null && !game.getWhiteUsername().isEmpty();
        } else if ("BLACK".equalsIgnoreCase(playerColor)) {
            return game.getBlackUsername() != null && !game.getBlackUsername().isEmpty();
        } else {
            // For observers, color is not taken
            return false;
        }
    }

    public void clearGames() {
        games.clear();
    }

    // might need additional methods for game-specific operations, like joining a game, making a move, etc.
}
