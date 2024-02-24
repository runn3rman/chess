package server;

import handlers.*;
import service.ClearService;
import spark.*;

public class Server {
    private ClearService clearService = new ClearService();


    public static void main(String[] args){
        new Server().run(8080);
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearAll);
        Spark.post("/user", (req, res) ->
                (new RegisterHandler()).handleRequest(req,
                        res));



        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    private Object clearAll(Request req, Response res) {
        clearService.clearApplication();
        res.status(200);
        return "{}"; //TODO return JSON object {}.
    }


}