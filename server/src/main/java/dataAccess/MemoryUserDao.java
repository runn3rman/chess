package dataAccess;

import model.UserData;
import java.util.HashMap;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MemoryUserDao implements UserDaoInterface {
    private static final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void clearUsers() {
        users.clear();
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void insertUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.username())) {
            throw new DataAccessException("User already exists");
        }
        users.put(user.username(), user);
    }



    //
}



