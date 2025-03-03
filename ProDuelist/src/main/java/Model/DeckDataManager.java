package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeckDataManager {
    public List<Deck> getDecksByUserId(int userId) {
        List<Deck> decks = new ArrayList<>();
        String query = "SELECT * FROM Decks WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Deck deck = new Deck(
                        rs.getInt("deck_id"),
                        rs.getInt("user_id"),
                        rs.getString("deck_name"),
                        rs.getString("visibility")
                );
                decks.add(deck);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return decks;
    }
}
