package Model;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private String deckId;
    private String userId;
    private String deckName;
    private String visibility;
    private List<String> cardIDs;

    public Deck(String deckId, String userId, String deckName, String visibility, List<String> cardIDs) {
        this.deckId = deckId;
        this.userId = userId;
        this.deckName = deckName;
        this.visibility = visibility;
        this.cardIDs = (cardIDs == null) ? new ArrayList<>() : new ArrayList<>(cardIDs);
    }

    public List<String> getCardIDs() {
        return cardIDs;
    }

    public String getDeckId() {
        return deckId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDeckName() {
        return deckName;
    }

    public String getVisibility() {
        return visibility;
    }
}
