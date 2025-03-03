package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {
    public static void saveCards(List<Card> cards) {
        String sql = "INSERT INTO Cards (id, name, type, frameType, race, attribute, level, attack, defense, desc, image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Card card : cards) {
                pstmt.setInt(1, card.getId());
                pstmt.setString(2, card.getName());
                pstmt.setString(3, card.getType());
                pstmt.setString(4, card.getFrameType());
                pstmt.setString(5, card.getRace());
                pstmt.setString(6, card.getAttribute());
                pstmt.setInt(7, card.getLevel());
                pstmt.setInt(8, card.getAttack());
                pstmt.setInt(9, card.getDefense());
                pstmt.setString(10, card.getDesc());
                pstmt.setString(11, card.getImageUrl());

                pstmt.executeUpdate();
                System.out.println("✔ Thêm thành công: " + card.getName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM Cards";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Card card = new Card(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("frameType"),
                        rs.getString("race"),
                        rs.getString("attribute"),
                        rs.getInt("level"),
                        rs.getInt("attack"),
                        rs.getInt("defense"),
                        rs.getString("desc"),
                        rs.getString("image_url")
                );
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }
}
