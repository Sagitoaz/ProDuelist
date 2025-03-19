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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DeckSectionManager {
    private static Map<Integer, Integer> cardCountMap = new HashMap<>();

    private boolean editable = true;
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    private Label cardNameLabel;
    private ImageView cardImageView;
    private TextArea cardDescriptionArea;

    public abstract void addCard(Node node);
    public abstract void removeCard(Node node);
    public abstract void loadDeckSection(List<String> cardIds);
    protected abstract void rearrangDeck();

    public static void resetCardCountMap() {
        cardCountMap.clear();
    }

    protected static int getCardCount(Integer cardID) {
        return cardCountMap.getOrDefault(cardID, 0);
    }

    protected static void increaseCardCount(Integer cardID) {
        cardCountMap.put(cardID, getCardCount(cardID) + 1);
    }

    protected static void decreaseCardCount(Integer cardID) {
        int cnt = getCardCount(cardID);
        if (cnt > 0) {
            cardCountMap.put(cardID, cnt - 1);
        }
    }

    public void setCardInfoComponents(Label cardNameLabel, ImageView cardImageView, TextArea cardDescriptionArea) {
        this.cardNameLabel = cardNameLabel;
        this.cardImageView = cardImageView;
        this.cardDescriptionArea = cardDescriptionArea;
    }

    protected void setOnDragAndDrop(Node node) {
        if (!editable) return;

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
                    if (imageView.getParent() == parent) {
                        imageView.getProperties().put("droppedInside", true);
                        success = true;
                    }
                } else {
                    String cardId = dragboard.getString();
                    Card card = CardDAO.getCardById(cardId);
                    if (card != null) {
                        if (getCardCount(card.getId()) < 3) {
                            CardImageView cardImageView = new CardImageView(card);
                            cardImageView.getProperties().put("droppedInside", true);
                            attachDragDoneHandler(cardImageView);
                            cardImageView.setOnMouseEntered(_ -> showCardInfo(card));
                            if ((this instanceof MainDeckBuilderGridManager && !card.checkExtraMonster()) ||
                                    (this instanceof ExtraDeckBuilderManager && card.checkExtraMonster()) ||
                                    (this instanceof SideDeckBuilderManager)) {
                                addCard(cardImageView);
                                success = true;
                            }
                        } else {
                            System.out.println("Đạt giới hạn 3 lá " + card.getName());
                        }
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    protected void attachDragDoneHandler(CardImageView cardImageView) {
        cardImageView.setOnDragDone(event -> {
            Boolean isDroppedInside = (Boolean) cardImageView.getProperties().get("droppedInside");
            if (isDroppedInside == null  || !isDroppedInside) {
                System.out.println("DropComplete: " + event.isDropCompleted());
                if (!event.isDropCompleted()) {
                    System.out.println("Drag Done: " + cardImageView.getCard().getName() + ", droppedInside: " + isDroppedInside);
                    removeCard(cardImageView);
                }
            }
            cardImageView.getProperties().remove("droppedInside");
        });
    }

    protected void showCardInfo(Card card) {
        if (cardNameLabel != null && cardImageView != null && cardDescriptionArea != null) {
            cardNameLabel.setText(card.getName());
            cardImageView.setImage(new Image(card.getImageUrl()));
            setCardDesc(card, cardDescriptionArea);
        }
    }

    public static void setCardDesc(Card card, TextArea cardDescriptionArea) {
        String cardDesc = card.getName() + "\n";
        cardDesc += "[" + card.getType() + "] " + card.getRace() + "/" + card.getAttribute() + "\n";
        if (card.getAttribute() != null) {
            cardDesc += "[" + card.getLevel() + "★] " + card.getAttack() + "/" + card.getDefense() + "\n";
        }
        cardDesc += card.getDesc();
        cardDescriptionArea.setText(cardDesc);
    }
}
