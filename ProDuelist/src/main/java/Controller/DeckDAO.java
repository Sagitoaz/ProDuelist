package Controller;

import Model.DatabaseConnection;
import Model.Deck;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeckDAO {
    public void updateDeck(Deck deck) {
        String sql = "UPDATE decks SET card_ids = ?, extra_deck = ?, side_deck = ? WHERE id = ?::uuid";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Array mainDeckArray = conn.createArrayOf("text", deck.getMainCardIDs().toArray(new String[0]));
            Array extraDeckArray = conn.createArrayOf("text", deck.getExtraCardIDs().toArray(new String[0]));
            Array sideDeckArray = conn.createArrayOf("text", deck.getSideCardIDs().toArray(new String[0]));

            pstmt.setArray(1, mainDeckArray);
            pstmt.setArray(2, extraDeckArray);
            pstmt.setArray(3, sideDeckArray);
            pstmt.setString(4, deck.getDeckId());
            pstmt.executeUpdate();

            System.out.println("Deck " + deck.getDeckName() + " đã update card_ids thành công.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
