package Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

public class CardImageView extends StackPane {
    private final Card card;
    private ImageView cardImage;
    private ImageView banlistOverlay;

    public CardImageView(Card card) {
        this.card = card;
        cardImage = new ImageView(new Image(card.getImageUrl()));
        cardImage.setFitWidth(50);
        cardImage.setFitHeight(70);
        cardImage.setPreserveRatio(true);
        this.getChildren().add(cardImage);
        String banlistImage = BanlistManager.getBanStatus(String.valueOf(card.getId()));
        if (banlistImage != null) {
            banlistOverlay = new ImageView(new Image(getClass().getResourceAsStream("/Images/TypeOfBanlist/" + banlistImage)));
            banlistOverlay.setFitWidth(20);
            banlistOverlay.setFitHeight(20);
            banlistOverlay.setTranslateX(20);
            banlistOverlay.setTranslateY(-30);
            this.getChildren().add(banlistOverlay);
        }
        setUserData(String.valueOf(card.getId()));
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        setOnDragDetected(event -> {
            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString((String) getUserData());
            db.setContent(content);
            db.setDragView(cardImage.getImage());
            event.consume();
        });
    }

    public Card getCard() {
        return card;
    }
}
