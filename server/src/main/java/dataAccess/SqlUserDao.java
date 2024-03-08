package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;

public class SqlUserDao implements UserDaoInterface {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void clearUsers() {
        String sql = "DELETE FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Database access error in clearUsers: " + e.getMessage(), e);
        }
    }

    @Override
    public UserData getUser(String username) {
        String sql = "SELECT username, password_hash, email FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String passwordHash = rs.getString("password_hash");
                    String email = rs.getString("email");
                    return new UserData(username, passwordHash, email);
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Database access error in getUser: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void insertUser(UserData user) throws DataAccessException {
        String sql = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.username());
            ps.setString(2, encoder.encode(user.password()));
            ps.setString(3, user.email());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting user: " + e.getMessage());
        }
    }

    // Additional methods as needed...
}
