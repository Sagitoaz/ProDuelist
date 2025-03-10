package Controller;

import Model.Card;
import Model.CardDAO;
import Model.CardImageView;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.util.List;

public class ExtraDeckBuilderManager extends DeckSectionManager{
    private final HBox extraDeckHbox;
    private final int hboxMaxSize = 15;

    public ExtraDeckBuilderManager(HBox extraDeckHbox) {
        this.extraDeckHbox = extraDeckHbox;
        setOnDragAndDrop(extraDeckHbox);
    }

    @Override
    public void addCard(Node node) {
        if (extraDeckHbox.getChildren().size() < hboxMaxSize) {
            extraDeckHbox.getChildren().add(node);
        }
    }

    @Override
    public void removeCard(Node node) {
        extraDeckHbox.getChildren().remove(node);
    }

    @Override
    public void loadDeckSection(List<String> cardIds) {
        extraDeckHbox.getChildren().clear();
        for (String cardID : cardIds) {
            Card card = CardDAO.getCardById(cardID);
            if (card != null) {
                CardImageView cardImageView = new CardImageView(card);
                addCard(cardImageView);
                cardImageView.setOnMouseEntered(_ -> showCardInfo(card));
            }
        }
    }

    @Override
    protected void rearrangDeck() {

    }
}
