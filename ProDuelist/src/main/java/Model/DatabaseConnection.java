package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres";
    private static final String USER = "postgres.wlrspadzuwpuvdzfnqvp";
    private static final String PASSWORD = "2401TrungLinh"; // database password

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối thành công!");
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối Database: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
}
