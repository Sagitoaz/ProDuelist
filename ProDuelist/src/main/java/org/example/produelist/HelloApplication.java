package org.example.produelist;

import Model.*;
import com.google.gson.JsonArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class HelloApplication {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver"); // Nạp driver PostgreSQL
            Connection conn = DriverManager.getConnection("jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres", "postgres.wlrspadzuwpuvdzfnqvp", "241Thanh@");
            System.out.println("🟢 Kết nối thành công!");
        } catch (ClassNotFoundException e) {
            System.out.println("🔴 Không tìm thấy Driver!");
        } catch (SQLException e) {
            System.out.println("🔴 Lỗi SQL: " + e.getMessage());
        }
    }
}