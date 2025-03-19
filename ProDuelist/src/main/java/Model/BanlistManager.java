package Model;

import java.sql.*;
import java.util.*;

public class BanlistManager {
    private static final Map<String, Set<String>> banList = new HashMap<>();

    public static void loadBanlist(String format) {
        banList.clear();
        banList.put("forbidden", new HashSet<>());
        banList.put("limited", new HashSet<>());
        banList.put("semi_limited", new HashSet<>());

        String query = "SELECT forbidden, limited, semi_limited FROM ban_list WHERE name = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setString(1, format);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                loadBanlistData(rs, "forbidden");
                loadBanlistData(rs, "limited");
                loadBanlistData(rs, "semi_limited");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadBanlistData(ResultSet rs, String column) throws SQLException {
        Array array = rs.getArray(column);
        if (array != null) {
            String[] ids = (String[]) array.getArray();
            banList.get(column).addAll(Arrays.asList(ids));
        }
    }

    public static String getBanStatus(String cardId) {
        if (banList.get("forbidden").contains(cardId)) return "banlist-banned.png";
        if (banList.get("limited").contains(cardId)) return "banlist-limited.png";
        if (banList.get("semi_limited").contains(cardId)) return "banlist-semilimited.png";
        return null;
    }
}
