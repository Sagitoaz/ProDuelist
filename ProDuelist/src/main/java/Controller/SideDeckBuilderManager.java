package Controller;

import Model.Card;
import Model.CardDAO;
import Model.CardImageView;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.util.List;

public class SideDeckBuilderManager extends DeckSectionManager{
    private final HBox sideDeckHbox;
    private final int hboxMaxSize = 15;

    public SideDeckBuilderManager(HBox sideDeckHbox) {
        this.sideDeckHbox = sideDeckHbox;
        setOnDragAndDrop(sideDeckHbox);
    }

    @Override
    public void addCard(Node node) {
        if (sideDeckHbox.getChildren().size() < hboxMaxSize) {
            sideDeckHbox.getChildren().add(node);
        }
    }

    @Override
    public void removeCard(Node node) {
        sideDeckHbox.getChildren().remove(node);
    }

    @Override
    public void loadDeckSection(List<String> cardIds) {
        sideDeckHbox.getChildren().clear();
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
