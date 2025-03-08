package Controller;

import Model.Card;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CardDisplayFactory {
    public CardDisplayFactory() {}

    public HBox createCardBox(Card card) {
        ImageView imageView = new ImageView(new Image(card.getImageUrl()));
        imageView.setFitWidth(35);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(card.getName());
        Label detailsLabel = new Label(
                card.getType() + "/" + card.getRace() + " ★" + card.getLevel() +
                        "\n" + card.getAttack() + "/" + card.getDefense()
        );

        VBox textContainer = new VBox(nameLabel, detailsLabel);
        HBox cardContainer = new HBox(imageView, textContainer);
        cardContainer.setSpacing(10);

        cardContainer.setOnDragDetected(event -> {
            Dragboard db = cardContainer.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(card.getId()));
            db.setContent(content);
            Image dragImage = new Image(card.getImageUrl(), 50, 70, true, true);
            db.setDragView(dragImage);
            db.setDragView(imageView.getImage());
            event.consume();
        });

        return cardContainer;

    }
}
