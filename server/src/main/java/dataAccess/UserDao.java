package dataAccess;

import model.UserData;

import java.util.HashMap;

public class UserDao {
    private static final HashMap<String, UserData> users = new HashMap<>();

    public void clearUsers() {
        users.clear();
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void insertUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.username())) {
            throw new DataAccessException("User already exists");
        }
        users.put(user.username(), user);
    }

    // Other necessary DAO methods
}


