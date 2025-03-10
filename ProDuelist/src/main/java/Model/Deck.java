package Model;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private String deckId;
    private String userId;
    private String deckName;
    private String visibility;
    private List<String> mainCardIDs;
    private List<String> extraCardIDs;
    private List<String> sideCardIDs;

    public Deck(String deckId, String userId, String deckName, String visibility, List<String> mainCardIDs, List<String> extraCardIDs, List<String> sideCardIDs) {
        this.deckId = deckId;
        this.userId = userId;
        this.deckName = deckName;
        this.visibility = visibility;
        this.mainCardIDs = mainCardIDs;
        this.extraCardIDs = extraCardIDs;
        this.sideCardIDs = sideCardIDs;
    }

    public List<String> getMainCardIDs() {
        return mainCardIDs;
    }

    public void setMainCardIDs(List<String> mainCardIDs) {
        this.mainCardIDs = mainCardIDs;
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

    public List<String> getExtraCardIDs() {
        return extraCardIDs;
    }

    public void setExtraCardIDs(List<String> extraCardIDs) {
        this.extraCardIDs = extraCardIDs;
    }

    public List<String> getSideCardIDs() {
        return sideCardIDs;
    }

    public void setSideCardIDs(List<String> sideCardIDs) {
        this.sideCardIDs = sideCardIDs;
    }
}
