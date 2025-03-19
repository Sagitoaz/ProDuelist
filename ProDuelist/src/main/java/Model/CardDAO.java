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

    public static Card getCardById(String idStr) {
        int id = Integer.parseInt(idStr);
        String sql = "SELECT * FROM Cards WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Card(
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Card> searchCardsByName(String cardName, int limit) {
        List<Card> cards = new ArrayList<>();
        String query = "SELECT * FROM Cards WHERE name LIKE ? LIMIT ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + cardName + "%");
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cards.add(new Card(
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
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    public List<Card> searchCardsByFilter(String name, String type, String race, String attribute, String monsterType, Integer level, Integer atk, Integer def, int limit) {
        List<Card> cards = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM Cards WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            query.append(" AND name LIKE ?");
            params.add("%" + name + "%");
        }
        if (type != null) {
            query.append(" AND type LIKE ?");
            params.add("%" + type + "%");
        }
        if (race != null) {
            query.append(" AND race LIKE ?");
            params.add("%" + race + "%");
        }
        if (attribute != null) {
            query.append(" AND attribute LIKE ?");
            params.add("%" + attribute + "%");
        }
        if (monsterType != null) {
            query.append(" AND type LIKE ?");
            params.add("%" + monsterType + "%");
        }
        if (level != null) {
            query.append(" AND level = ?");
            params.add(level);
        }
        if (atk != null) {
            query.append(" AND attack = ?");
            params.add(atk);
        }
        if (def != null) {
            query.append(" AND defense = ?");
            params.add(def);
        }
        query.append(" LIMIT ?");
        params.add(limit);

        String finalQuery = query.toString();
        for (Object param : params) {
            finalQuery = finalQuery.replaceFirst("\\?", param instanceof String ? "'" + param + "'" : param.toString());
        }
        System.out.println("Final SQL Query: " + finalQuery);

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cards.add(new Card(
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
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cards;
    }
}
