package Controller;

import Model.DatabaseConnection;
import Model.Deck;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeckDAO {
    public void updateDeck(Deck deck) {
        String sql = "UPDATE decks SET card_ids = ? WHERE id = ?::uuid";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String[] arr = deck.getCardIDs().toArray(new String[0]);

            java.sql.Array sqlArray = conn.createArrayOf("text", arr);

            pstmt.setArray(1, sqlArray);
            pstmt.setString(2, deck.getDeckId());
            pstmt.executeUpdate();

            System.out.println("Deck " + deck.getDeckName() + " đã update card_ids thành công.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
