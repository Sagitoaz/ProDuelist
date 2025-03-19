package Controller;

import Model.BanlistManager;
import Model.Card;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CardDisplayFactory {
    public CardDisplayFactory() {}

    public HBox createCardBox(Card card) {
//        ImageView imageView = new ImageView(new Image(card.getImageUrl()));
//        imageView.setFitWidth(35);
//        imageView.setFitHeight(50);
//        imageView.setPreserveRatio(true);
        StackPane cardPane = createCardImage(card);

        Label nameLabel = new Label(card.getName());
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10; -fx-max-width: 170; -fx-pref-width: 170;");
        Label detailsLabel = new Label(
                card.getType() + "/" + card.getRace() + " ★" + card.getLevel() +
                        "\n" + card.getAttack() + "/" + card.getDefense()
        );
        detailsLabel.setWrapText(true);
        detailsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10; -fx-max-width: 170; -fx-pref-width: 170;");

        VBox textContainer = new VBox(nameLabel, detailsLabel);
        HBox cardContainer = new HBox(cardPane, textContainer);
        cardContainer.setSpacing(10);

        cardContainer.setOnDragDetected(event -> {
            Dragboard db = cardContainer.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(card.getId()));
            db.setContent(content);
            Image dragImage = new Image(card.getImageUrl(), 50, 70, true, true);
            db.setDragView(dragImage);
            ImageView cardImage = (ImageView) cardPane.getChildren().getFirst();
            db.setDragView(cardImage.getImage());
            event.consume();
        });

        return cardContainer;

    }

    private StackPane createCardImage(Card card) {
        StackPane cardPane = new StackPane();
        ImageView imageView = new ImageView(new Image(card.getImageUrl()));
        imageView.setFitWidth(35);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);
        String banlistImage = BanlistManager.getBanStatus(String.valueOf(card.getId()));
        //System.out.println("banlistImage: " + banlistImage);
        cardPane.getChildren().add(imageView);
        if (banlistImage != null) {
            //..\..\Images\CardType\normal.png
            ImageView banlistOverlay = new ImageView(new Image(getClass().getResourceAsStream("/Images/TypeOfBanlist/" + banlistImage)));
            banlistOverlay.setFitWidth(15);
            banlistOverlay.setFitHeight(15);
            banlistOverlay.setTranslateX(15);
            banlistOverlay.setTranslateY(-20);
            cardPane.getChildren().add(banlistOverlay);
        }
        return cardPane;
    }
}
