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
                Array mainCardArray = rs.getArray("card_ids");
                Array extraCardArray = rs.getArray("extra_deck");
                Array sideCardArray = rs.getArray("side_deck");
                List<String> mainCardIds = new ArrayList<>();
                List<String> extraCardIds = new ArrayList<>();
                List<String> sideCardIds = new ArrayList<>();
                if (mainCardArray != null) {
                    String[] cardIdsArray = (String[]) mainCardArray.getArray();
                    mainCardIds = Arrays.asList(cardIdsArray);
                }
                if (extraCardArray != null) {
                    String[] cardIdsArray = (String[]) extraCardArray.getArray();
                    extraCardIds = Arrays.asList(cardIdsArray);
                }
                if (sideCardArray != null) {
                    String[] cardIdsArray = (String[]) sideCardArray.getArray();
                    sideCardIds = Arrays.asList(cardIdsArray);
                }
                Deck deck = new Deck(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("visibility"),
                        mainCardIds,
                        extraCardIds,
                        sideCardIds
                );
                decks.add(deck);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return decks;
    }
}
