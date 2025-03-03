package Model;

public class Deck {
    private int deckId;
    private int userId;
    private String deckName;
    private String visibility;

    public Deck(int deckId, int userId, String deckName, String visibility) {
        this.deckId = deckId;
        this.userId = userId;
        this.deckName = deckName;
        this.visibility = visibility;
    }

    public int getDeckId() {
        return deckId;
    }

    public int getUserId() {
        return userId;
    }

    public String getDeckName() {
        return deckName;
    }

    public String getVisibility() {
        return visibility;
    }
}
