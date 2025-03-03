package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:src/main/resources/Database/produelist.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Cards (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "type TEXT, " +
                "frameType TEXT, " +
                "race TEXT, " +
                "attribute TEXT, " +
                "level INTEGER, " +
                "attack INTEGER, " +
                "defense INTEGER, " +
                "desc TEXT, " +
                "image_url TEXT)";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
