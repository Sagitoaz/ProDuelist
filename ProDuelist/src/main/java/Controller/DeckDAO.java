package Controller;

import Model.Card;
import Model.DatabaseConnection;
import Model.DatabaseManager;
import Model.Deck;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    public void updateDeckVisibility(Deck deck) {
        String sql = "UPDATE decks SET visibility = ? WHERE id = ?::uuid";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, deck.getVisibility());
            pstmt.setString(2, deck.getDeckId());
            pstmt.executeUpdate();

            System.out.println("Deck " + deck.getDeckName() + " đã update visibility thành công.");
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

    public List<Deck> searchDeckByName(String deckName, int limit) {
        List<Deck> decks = new ArrayList<>();
        String query = "SELECT * FROM decks WHERE name ILIKE ? AND visibility = 'public' LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + deckName + "%");
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    List<String> mainCardIDs = new ArrayList<>();
                    Array cardIdsArr = rs.getArray("card_ids");
                    if (cardIdsArr != null) {
                        mainCardIDs = Arrays.asList((String[]) cardIdsArr.getArray());
                    }

                    List<String> extraCardIDs = new ArrayList<>();
                    Array extraDeckArr = rs.getArray("extra_deck");
                    if (extraDeckArr != null) {
                        extraCardIDs = Arrays.asList((String[]) extraDeckArr.getArray());
                    }

                    List<String> sideCardIDs = new ArrayList<>();
                    Array sideDeckArr = rs.getArray("side_deck");
                    if (sideDeckArr != null) {
                        sideCardIDs = Arrays.asList((String[]) sideDeckArr.getArray());
                    }
                    decks.add(new Deck(
                            rs.getString("id"),
                            rs.getString("user_id"),
                            rs.getString("name"),
                            rs.getString("visibility"),
                            mainCardIDs,
                            extraCardIDs,
                            sideCardIDs
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return decks;
    }

    public String getDeckAuthor(String userID) throws SQLException {
        String Author = null;
        String query = "SELECT username FROM users WHERE id = ?::uuid";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setString(1, userID);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Author = rs.getString("username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Author;
    }

    public void rateDeck(String deckId, String userId, int rating) throws SQLException {
        String checkQuery = "SELECT * FROM Ratings WHERE deck_id = ?::uuid AND user_id = ?::uuid";
        try (PreparedStatement checkStmt = DatabaseConnection.getConnection().prepareStatement(checkQuery)) {
            checkStmt.setObject(1, UUID.fromString(deckId));
            checkStmt.setObject(2, UUID.fromString(userId));
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                throw new SQLException("Bạn đã đánh giá deck này rồi!");
            }
        }

        String insertQuery = "INSERT INTO Ratings (deck_id, user_id, rating) VALUES (?::uuid, ?::uuid, ?)";
        try (PreparedStatement insertStmt = DatabaseConnection.getConnection().prepareStatement(insertQuery)) {
            insertStmt.setObject(1, UUID.fromString(deckId));
            insertStmt.setObject(2, UUID.fromString(userId));
            insertStmt.setInt(3, rating);
            insertStmt.executeUpdate();
        }

        String updateDeckQuery = "UPDATE Decks SET total_rating = total_rating + ?, rating_count = rating_count + 1 WHERE id = ?::uuid";
        try (PreparedStatement updateStmt = DatabaseConnection.getConnection().prepareStatement(updateDeckQuery)) {
            updateStmt.setInt(1, rating);
            updateStmt.setObject(2, UUID.fromString(deckId));
            updateStmt.executeUpdate();
        }
    }

    public double getDeckRating(String deckId) throws SQLException {
        String query = "SELECT total_rating, rating_count FROM Decks WHERE id = ?::uuid";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            stmt.setObject(1, UUID.fromString(deckId));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total_rating");
                int count = rs.getInt("rating_count");
                return count == 0 ? 0 : (double) total / count;
            }
        }
        return 0;
    }
}
