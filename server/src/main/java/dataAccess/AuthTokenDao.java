//Maybe I should add an extract AuthToken
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

    public String extractUsername(String authToken) { //consider changing naming
        AuthData authData = authTokens.get(authToken);
        if (authData != null) {
            return authData.username();
        } else {
            return null; // Or throw an exception
        }
    }
    public boolean isValidAuthToken(String authToken) {
        // Check if the provided authToken exists in the authTokens map
        return authTokens.containsKey(authToken);
    }
    // Additional methods as needed...
}
