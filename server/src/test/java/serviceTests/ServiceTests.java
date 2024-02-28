//TODO, make sure that the tests all pass. join game successes.
package serviceTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataAccess.*;
import service.*;
import model.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {

    private static GameDao gameDao;
    private static AuthTokenDao authTokenDao;
    private static CreateGameService createGameService;
    private static JoinGameService joinGameService;
    private static GameService gameService;
    private static String validAuthToken;
    private static String invalidAuthToken = "invalidToken";
    private static String gameName = "testGame";
    private static int testGameId;

    @BeforeAll
    static void setUp() {
        gameDao = new GameDao();
        authTokenDao = new AuthTokenDao();
        createGameService = new CreateGameService(gameDao, authTokenDao);
        gameService = new GameService(gameDao);
        joinGameService = new JoinGameService(gameDao, authTokenDao);

        validAuthToken = "validToken";
        authTokenDao.insertAuthToken(new AuthData(validAuthToken, "testUser"));

        try {
            testGameId = createGameService.createGame(validAuthToken, gameName + System.currentTimeMillis());
        } catch (Exception e) {
            fail("Setup failed due to exception: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDown() {
        // Clean up resources, if necessary
        gameDao.clearGames();
        authTokenDao.clearAuthTokens();
    }

    @Test
    @Order(1)
    @DisplayName("Create Game Successfully")
    void createGameSuccess() throws Exception {
        String uniqueGameName = gameName + System.currentTimeMillis();
        int gameId = createGameService.createGame(validAuthToken, uniqueGameName);
        assertNotNull(gameId, "Game ID should not be null");
        assertTrue(gameId > 0, "Game ID should be positive");
    }

    @Test
    @Order(2)
    @DisplayName("Create Game Failure - Unauthorized")
    void createGameFailureUnauthorized() {
        Exception exception = assertThrows(Exception.class, () -> createGameService.createGame(invalidAuthToken, gameName + System.currentTimeMillis()),
                "Expected to throw, but it didn't");

        assertTrue(exception.getMessage().contains("Error: unauthorized"), "Exception message should indicate authorization failure");
    }

    @Test
    @Order(3)
    @DisplayName("List Games - Success")
    void listGamesSuccess() {
        List<GameData> games = gameService.listGames();
        assertNotNull(games, "List of games should not be null");
        assertFalse(games.isEmpty(), "List of games should not be empty");
    }

    @Test
    @Order(4)
    @DisplayName("List Games - No Games Available")
    void listGamesNoGames() {
        gameDao.clearGames(); // Ensure this does not affect other tests or move it to a specific setup for this test

        List<GameData> games = gameService.listGames();
        assertNotNull(games, "List of games should not be null");
        assertTrue(games.isEmpty(), "List of games should be empty when no games are available");
    }

    @Test
    @Order(5)
    @DisplayName("Join Game Failure - Invalid Auth Token")
    void joinGameInvalidAuthToken() {
        JoinGameRequest request = new JoinGameRequest("WHITE", testGameId);
        Exception exception = assertThrows(Exception.class, () -> joinGameService.joinGame(invalidAuthToken, request),
                "Expected to throw due to invalid auth token, but it didn't");

        assertTrue(exception.getMessage().contains("Invalid or expired authToken"), "Exception message should indicate auth token issue");
    }

    @Test
    @Order(6)
    @DisplayName("Join Game Failure - Invalid Team Color")
    void joinGameInvalidTeamColor() {
        JoinGameRequest request = new JoinGameRequest("RED", testGameId);
        Exception exception = assertThrows(Exception.class, () -> joinGameService.joinGame(validAuthToken, request),
                "Expected to throw due to invalid team color, but it didn't");

        assertTrue(exception.getMessage().contains("Invalid team color"), "Exception message should indicate team color issue");
    }

    @Test
    @Order(7)
    @DisplayName("Join Game Failure - Team Color Already Taken")
    void joinGameColorTaken() {
        JoinGameRequest request = new JoinGameRequest("WHITE", testGameId);
        // Ensure a player has already joined with this color if required by game logic
        Exception exception = assertThrows(Exception.class, () -> joinGameService.joinGame(validAuthToken, request),
                "Expected to throw due to team color already taken, but it didn't");

        assertTrue(exception.getMessage().contains("The team color is already taken"), "Exception message should indicate color taken issue");
    }

    @Test
    @Order(8)
    @DisplayName("Join Game Success - As Player")
    void joinGameAsPlayerSuccess() {
        JoinGameRequest request = new JoinGameRequest("BLACK", testGameId); // Ensure this color is available in the game setup
        assertDoesNotThrow(() -> joinGameService.joinGame(validAuthToken, request),
                "Should successfully join the game as a player");
    }

    @Test
    @Order(9)
    @DisplayName("Join Game Success - As Observer")
    void joinGameAsObserverSuccess() {
        JoinGameRequest request = new JoinGameRequest(null, testGameId); // Adjust if the constructor does not support null color
        assertDoesNotThrow(() -> joinGameService.joinGame(validAuthToken, request),
                "Should successfully join the game as an observer");
    }
}
