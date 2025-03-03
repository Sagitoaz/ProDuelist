package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    private boolean executeCountQuery(String query, String param) {
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, param);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0; // >0 => already exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUsernameExists(String username) {
        return executeCountQuery("SELECT COUNT(*) FROM Users WHERE username = ?", username);
    }

    public boolean isEmailExists(String email) {
        return executeCountQuery("SELECT COUNT(*) FROM Users WHERE email = ?", email);
    }

    public boolean checkPassword(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registUser(String email, String username, String password) {
        String query = "INSERT INTO Users (email, username, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, username);
            pstmt.setString(3, password); // Chưa mã hóa password
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
