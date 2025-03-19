package Controller;

import Model.Deck;
import Model.UserInformation;
import View.DeckBuilderView;
import View.Main;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.sql.SQLException;

public class DeckDisplayFactory {
    private final DeckDAO deckDAO = new DeckDAO();
    private final SceneManager sceneManager = SceneManager.getInstance();

    public DeckDisplayFactory() {}

    public HBox createDeckBox(Deck deck) throws SQLException {
        Label deckNameLabel = new Label("DECK: " + deck.getDeckName());
        deckNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold;");

        Label deckAuthor = new Label("AUTHOR: " + deckDAO.getDeckAuthor(deck.getUserId()));
        deckAuthor.setStyle("-fx-text-fill: white; -fx-font-size: 12;");

        double avgRating = deckDAO.getDeckRating(deck.getDeckId());
        Label ratingLabel = new Label(String.format("Rating: %.1f ⭐", avgRating));
        ratingLabel.setStyle("-fx-text-fill: yellow; -fx-font-size: 18; -fx-font-weight: bold;");

        ComboBox<Integer> ratingBox = new ComboBox<>();
        ratingBox.getItems().addAll(1, 2, 3, 4, 5);
        ratingBox.setPromptText("Rate");
        ratingBox.setStyle("""
            -fx-font-size: 14;
            -fx-background-color: #222;
            -fx-border-color: cyan;
            -fx-border-radius: 5;
            -fx-text-fill: white;
        """);

        Button rateButton = new Button("Submit");
        rateButton.setStyle("""
            -fx-background-color: #ffcc00;
            -fx-text-fill: black;
            -fx-font-size: 14;
            -fx-font-weight: bold;
            -fx-border-radius: 5;
            -fx-padding: 5 10;
            -fx-cursor: hand;
        """);

        rateButton.setOnMouseEntered(e -> rateButton.setStyle("""
            -fx-background-color: #ff9900;
            -fx-font-size: 16;
            -fx-padding: 6 12;
        """));

        rateButton.setOnMouseExited(e -> rateButton.setStyle("""
            -fx-background-color: #ffcc00;
            -fx-font-size: 14;
            -fx-padding: 5 10;
        """));

        rateButton.setOnAction(_ -> {
            Integer selectedRating = ratingBox.getValue();
            if (selectedRating != null) {
                try {
                    deckDAO.rateDeck(deck.getDeckId(), UserInformation.getUserID(), selectedRating);
                    ratingLabel.setText(String.format("Rating: %.1f ⭐", deckDAO.getDeckRating(deck.getDeckId())));
                    rateButton.setDisable(true);
                } catch (SQLException e) {
                    System.out.println("Lỗi khi đánh giá: " + e.getMessage());
                }
            }
        });

        Image eyeImage = new Image(getClass().getResourceAsStream("/Images/SharingView/EyeImage.png"));
        ImageView eyeIcon = new ImageView(eyeImage);
        eyeIcon.setFitWidth(50);
        eyeIcon.setFitHeight(50);

        Button viewButton = new Button("", eyeIcon);
        viewButton.setStyle("""
            -fx-background-color: transparent;
            -fx-padding: 5;
            -fx-cursor: hand;
        """);

        ColorAdjust normalEffect = new ColorAdjust();
        normalEffect.setBrightness(0);

        ColorAdjust brightEffect = new ColorAdjust();
        brightEffect.setBrightness(0.5);

        viewButton.setOnMouseEntered(e -> eyeIcon.setEffect(brightEffect));
        viewButton.setOnMouseExited(e -> eyeIcon.setEffect(normalEffect));

        viewButton.setOnAction(_ -> {
            try {
                viewDeck(deck);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        VBox deckInfoBox = new VBox(deckNameLabel, deckAuthor);
        deckInfoBox.setSpacing(2);

        HBox ratingBoxContainer = new HBox(ratingBox, rateButton);
        ratingBoxContainer.setSpacing(5);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox deckBox = new HBox(deckInfoBox, viewButton, spacer, ratingLabel, ratingBoxContainer);
        deckBox.setSpacing(10);
        deckBox.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-border-color: cyan; -fx-border-width: 2; -fx-border-radius: 5;");
        return deckBox;
    }

    private void viewDeck(Deck deck) throws IOException {
        System.out.println("Xem Deck " + deck.getDeckName());
        Scene deckBuilderScene = sceneManager.deckBuilderView();

        DeckBuilderView deckBuilderController = sceneManager.getDeckBuilderView();
        deckBuilderController.setCanEdit(false);
        deckBuilderController.loadDeck(deck, false);

        Main.primaryStage.setScene(deckBuilderScene);
        Main.primaryStage.centerOnScreen();
    }
}
