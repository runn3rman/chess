package service;

import dataAccess.AuthTokenDao;

public class LogoutService {
    private AuthTokenDao authTokenDao;

    public LogoutService(AuthTokenDao authTokenDao) {
        this.authTokenDao = authTokenDao;
    }

    public void logout(String authToken) throws Exception {
        boolean success = authTokenDao.deleteAuthToken(authToken);
        if (!success) {
            throw new Exception("Invalid or expired authToken");
        }
    }
}
