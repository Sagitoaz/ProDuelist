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
        if (node instanceof CardImageView cardImageView) {
            if (sideDeckHbox.getChildren().size() < hboxMaxSize) {
                int cardId = cardImageView.getCard().getId();
                sideDeckHbox.getChildren().add(node);
                DeckSectionManager.increaseCardCount(cardId);
                //System.out.println("Có " + getCardCount(cardImageView.getCard().getId()) + " lá bài tên " + cardImageView.getCard().getName());
            }
        }
    }

    @Override
    public void removeCard(Node node) {
        sideDeckHbox.getChildren().remove(node);
        if (node instanceof CardImageView cardImageView) {
            DeckSectionManager.decreaseCardCount(cardImageView.getCard().getId());
            //System.out.println("Có " + getCardCount(cardImageView.getCard().getId()) + " lá bài tên " + cardImageView.getCard().getName());
        }
    }

    @Override
    public void loadDeckSection(List<String> cardIds) {
        sideDeckHbox.getChildren().clear();
        for (String cardID : cardIds) {
            Card card = CardDAO.getCardById(cardID);
            if (card != null) {
                CardImageView cardImageView = new CardImageView(card);
                //setOnDragAndDrop(cardImageView);
                attachDragDoneHandler(cardImageView);
                addCard(cardImageView);
                cardImageView.setOnMouseEntered(_ -> showCardInfo(card));
            }
        }
    }

    @Override
    protected void rearrangDeck() {

    }
}
