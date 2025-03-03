package Model;

import com.google.gson.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardAPI {
    private static final String API_URL = "https://db.ygoprodeck.com/api/v7/cardinfo.php";

    public static List<Card> fetchCards() {
        List<Card> cardList = new ArrayList<>();
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed: HTTP error code " + conn.getResponseCode());
            }

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray cards = jsonObject.getAsJsonArray("data");

            for (JsonElement element : cards) {
                JsonObject card = element.getAsJsonObject();

                int id = card.get("id").getAsInt();
                String name = card.get("name").getAsString();
                String type = card.get("type").getAsString();
                String frameType = card.get("frameType").getAsString();
                String race = card.get("race").getAsString();
                String attribute = (card.has("attribute") && !card.get("attribute").isJsonNull()) ? card.get("attribute").getAsString() : null;
                int level = (card.has("level") && !card.get("level").isJsonNull()) ? card.get("level").getAsInt() : 0;
                int attack = (card.has("atk") && !card.get("atk").isJsonNull()) ? card.get("atk").getAsInt() : 0;
                int defense = (card.has("def") && !card.get("def").isJsonNull()) ? card.get("def").getAsInt() : 0;
                String desc = card.get("desc").getAsString();

                JsonArray images = card.getAsJsonArray("card_images");
                String imageUrl = images.size() > 0 ? images.get(0).getAsJsonObject().get("image_url").getAsString() : null;

                cardList.add(new Card(id, name, type, frameType, race, attribute, level, attack, defense, desc, imageUrl));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cardList;
    }
}
