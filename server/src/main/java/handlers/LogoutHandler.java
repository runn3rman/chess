package handlers;

import com.google.gson.Gson;
import model.ErrorResponse;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    private LogoutService logoutService;
    private Gson gson = new Gson();

    public LogoutHandler(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    public Object handleRequest(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            if (authToken == null || authToken.isEmpty()) {
                throw new Exception("Authorization token is required");
            }

            logoutService.logout(authToken);

            res.status(200); // OK
            return "{}"; // Empty JSON object as a success response
        } catch (Exception e) {
            res.status(e.getMessage().equals("Invalid or expired authToken") ? 401 : 500);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}
