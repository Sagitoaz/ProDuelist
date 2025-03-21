package Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:" + getDatabasePath();

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

    private static String getDatabasePath() {
        String dbName = "produelist.db";
        String jarDir = System.getProperty("user.dir"); // Lấy thư mục chạy JAR
        String dbPath = jarDir + "/" + dbName;

        File dbFile = new File(dbPath);

        // Nếu file chưa tồn tại (chạy lần đầu khi đóng gói JAR), sao chép từ resources
        if (!dbFile.exists()) {
            try (InputStream is = DatabaseManager.class.getResourceAsStream("/Database/" + dbName);
                 FileOutputStream os = new FileOutputStream(dbFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                System.out.println("Copied database to: " + dbPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return dbPath;
    }
}
