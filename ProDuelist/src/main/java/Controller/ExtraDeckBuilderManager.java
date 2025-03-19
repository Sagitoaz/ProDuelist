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
        if (node instanceof CardImageView cardImageView) {
            if (extraDeckHbox.getChildren().size() < hboxMaxSize) {
                int cardId = cardImageView.getCard().getId();
                extraDeckHbox.getChildren().add(node);
                DeckSectionManager.increaseCardCount(cardId);
                //System.out.println("Có " + getCardCount(cardImageView.getCard().getId()) + " lá bài tên " + cardImageView.getCard().getName());
            }
        }
    }

    @Override
    public void removeCard(Node node) {
        extraDeckHbox.getChildren().remove(node);
        if (node instanceof CardImageView cardImageView) {
            DeckSectionManager.decreaseCardCount(cardImageView.getCard().getId());
            //System.out.println("Có " + getCardCount(cardImageView.getCard().getId()) + " lá bài tên " + cardImageView.getCard().getName());
        }
    }

    @Override
    public void loadDeckSection(List<String> cardIds) {
        extraDeckHbox.getChildren().clear();
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
