package handlers;

import com.google.gson.Gson;
import model.ErrorResponse;
import model.AuthData;
import model.LoginRequest;
import service.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    private LoginService loginService;
    private Gson gson = new Gson();

    public LoginHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    public Object handleRequest(Request req, Response res) {
        try {
            LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
            AuthData authData = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());

            res.status(200); // OK
            return gson.toJson(authData);
        } catch (Exception e) {
            res.status(e.getMessage().equals("Invalid username or password") ? 401 : 500);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}
