package Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class CardImageView extends ImageView {
    private final Card card;

    public CardImageView(Card card) {
        this.card = card;
        setImage(new Image(card.getImageUrl()));
        setFitWidth(50);
        setFitHeight(70);
        setPreserveRatio(true);
        setUserData(String.valueOf(card.getId()));
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        setOnDragDetected(event -> {
            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString((String) getUserData());
            db.setContent(content);
            db.setDragView(getImage());
            event.consume();
        });
    }
}
