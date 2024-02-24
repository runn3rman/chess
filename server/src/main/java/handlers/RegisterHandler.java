package handlers;

import service.*;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    private RegisterService registerService = new RegisterService();
    public Object handleRequest(Request req, Response res) {
        registerService.register();
        res.status(200);
        return "{}"; //TODO return JSON object {}. use gson (will have authtoken, header, body) turn json request  into object and pass to service
    }
}
