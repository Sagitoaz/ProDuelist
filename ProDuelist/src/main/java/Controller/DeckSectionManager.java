package Controller;

import Model.Card;
import Model.CardDAO;
import Model.CardImageView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.util.List;

public abstract class DeckSectionManager {
    private Label cardNameLabel;
    private ImageView cardImageView;
    private TextArea cardDescriptionArea;

    public abstract void addCard(Node node);
    public abstract void removeCard(Node node);
    public abstract void loadDeckSection(List<String> cardIds);
    protected abstract void rearrangDeck();

    public void setCardInfoComponents(Label cardNameLabel, ImageView cardImageView, TextArea cardDescriptionArea) {
        this.cardNameLabel = cardNameLabel;
        this.cardImageView = cardImageView;
        this.cardDescriptionArea = cardDescriptionArea;
    }

    protected void setOnDragAndDrop(Node node) {
        node.setOnDragOver(event -> {
            if (event.getGestureSource() != node && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        node.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                if (event.getGestureSource() instanceof ImageView imageView && node instanceof Parent parent) {
                    if (parent.getChildrenUnmodifiable().contains(imageView)) {
                        imageView.getProperties().put("droppedInside", true);
                        success = true;
                    }
                } else {
                    String cardId = dragboard.getString();
                    Card card = CardDAO.getCardById(cardId);
                    if (card != null) {
                        CardImageView cardImageView = new CardImageView(card);
                        attachDragDoneHandler(cardImageView);
                        cardImageView.setOnMouseEntered(_ -> showCardInfo(card));
                        if ((this instanceof MainDeckBuilderGridManager && !card.checkExtraMonster()) ||
                                (this instanceof ExtraDeckBuilderManager && card.checkExtraMonster()) ||
                                (this instanceof SideDeckBuilderManager)) {
                            addCard(cardImageView);
                            success = true;
                        }
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void attachDragDoneHandler(CardImageView cardImageView) {
        Boolean isDroppedInside = (Boolean) cardImageView.getProperties().get("droppedInside");
        cardImageView.setOnDragDone(event -> {
            if ((isDroppedInside == null || !isDroppedInside) && !event.isDropCompleted()) {
                removeCard(cardImageView);
            }
        });
    }

    protected void showCardInfo(Card card) {
        if (cardNameLabel != null && cardImageView != null && cardDescriptionArea != null) {
            cardNameLabel.setText(card.getName());
            cardImageView.setImage(new Image(card.getImageUrl()));

            String cardDesc = "[" + card.getType() + "] " + card.getRace() + "/" + card.getAttribute() + "\n";
            if (card.getAttribute() != null) {
                cardDesc += "[" + card.getLevel() + "★] " + card.getAttack() + "/" + card.getDefense() + "\n";
            }
            cardDesc += card.getDesc();
            cardDescriptionArea.setText(cardDesc);
        }
    }
}
