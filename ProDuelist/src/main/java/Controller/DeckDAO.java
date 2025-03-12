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

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            Array mainDeckArray = connection.createArrayOf("text", deck.getMainCardIDs().toArray(new String[0]));
            Array extraDeckArray = connection.createArrayOf("text", deck.getExtraCardIDs().toArray(new String[0]));
            Array sideDeckArray = connection.createArrayOf("text", deck.getSideCardIDs().toArray(new String[0]));

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

    public boolean addDeckToSupabase(String deckID, String userID, String deckName, String visibility) {
        String query = "INSERT INTO decks (id, user_id, name, visibility) VALUES (?::uuid, ?::uuid, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setString(1, deckID);
            pstm.setString(2, userID);
            pstm.setString(3, deckName);
            pstm.setString(4, visibility);
            int rowsInserted = pstm.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDeckFromSupabase(String deckID) {
        String query = "DELETE FROM decks WHERE id = ?::uuid";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setString(1, deckID);
            int deletedRow = pstm.executeUpdate();
            return deletedRow > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
