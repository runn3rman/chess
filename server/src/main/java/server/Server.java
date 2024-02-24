package server;

import handlers.*;
import service.ClearService;
import dataAccess.UserDao;
import dataAccess.GameDao;
import dataAccess.AuthTokenDao;
import service.CreateGameService;
import service.LoginService;
import service.LogoutService;
import spark.*;

public class Server {
    private ClearService clearService;


    public static void main(String[] args){
        new Server().run(8080);
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        UserDao userDao = new UserDao();
        GameDao gameDao = new GameDao();
        AuthTokenDao authTokenDao = new AuthTokenDao();
        clearService = new ClearService(userDao, gameDao, authTokenDao);
        ClearHandler clearHandler = new ClearHandler(clearService);
        LoginService loginService = new LoginService(new UserDao(), new AuthTokenDao());
        LoginHandler loginHandler = new LoginHandler(loginService);
        LogoutService logoutService = new LogoutService(new AuthTokenDao());
        LogoutHandler logoutHandler = new LogoutHandler(logoutService);
        CreateGameService createGameService = new CreateGameService(new GameDao(), new AuthTokenDao());
        CreateGameHandler createGameHandler = new CreateGameHandler(createGameService);

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::handleRequest);
        Spark.post("/user", (req, res) ->
                (new RegisterHandler()).handleRequest(req,
                        res));
        Spark.post("/session", loginHandler::handleRequest);
        Spark.delete("/session", logoutHandler::handleRequest);
        Spark.post("/game", createGameHandler::handleRequest);



        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}