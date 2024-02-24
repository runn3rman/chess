package dataAccess;

import model.AuthData;

import java.util.HashMap;

public class AuthTokenDao {
    private static final HashMap<String, AuthData> authTokens = new HashMap<>();

    public void clearAuthTokens() {
        authTokens.clear();
    }
    public void insertAuthToken(AuthData authData) {
        authTokens.put(authData.authToken(), authData);
    }

    public AuthData getAuthData(String authToken) {
        return authTokens.get(authToken);
    }

    public boolean deleteAuthToken(String authToken) {
        if (authTokens.containsKey(authToken)) {
            authTokens.remove(authToken);
            return true; // Token was found and removed
        } else {
            return false; // Token was not found
        }
    }
    // Additional methods as needed...
}
