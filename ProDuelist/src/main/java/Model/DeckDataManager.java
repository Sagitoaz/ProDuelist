package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DeckDataManager {
    public List<Deck> getDecksByUserId(String userId) {
        List<Deck> decks = new ArrayList<>();
        String query = "SELECT * FROM Decks WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, UUID.fromString(userId));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Array cardArray = rs.getArray("card_ids");
                List<String> cardIds = new ArrayList<>();
                if (cardArray != null) {
                    String[] cardIdsArray = (String[]) cardArray.getArray();
                    cardIds = Arrays.asList(cardIdsArray);
                }
                Deck deck = new Deck(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("visibility"),
                        cardIds
                );
                decks.add(deck);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return decks;
    }
}
