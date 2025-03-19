package Controller;

import Model.Deck;
import Utils.UIHelper;
import View.DeckBuilderView;
import View.Main;
import View.MainMenuView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import java.io.IOException;

public class DeckListControl {
    private ListView<Deck> deckListView;
    private final SceneManager sceneManager = SceneManager.getInstance();
    private final DeckDAO deckDAO = new DeckDAO();
    private final UIHelper uiHelper = new UIHelper();
    private MainMenuView mainMenuView;

    public DeckListControl(ListView<Deck> deckListView, MainMenuView mainMenuView) {
        this.deckListView = deckListView;
        this.mainMenuView = mainMenuView;
        setUpDeckList();
    }

    private void setUpDeckList() {
        deckListView.setCellFactory(_ -> new ListCell<Deck>() {
            private final HBox buttonHbox = new HBox();
            private final Button editButton = new Button("✏");
            private final Button shareButton = new Button("🔗");
            private final Button deleteButton = new Button("🗑");

            {
                editButton.setOnAction(event -> {
                    try {
                        editDeck(getItem());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                shareButton.setOnAction(event -> shareDeck(getItem()));
                deleteButton.setOnAction(event -> deleteDeck(getItem()));

                buttonHbox.getChildren().addAll(editButton, shareButton, deleteButton);
                buttonHbox.setSpacing(10);

            }

            @Override
            protected void updateItem(Deck deck, boolean empty) {
                super.updateItem(deck, empty);
                if (empty || deck == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null);

                    Label deckLabel = new Label(deck.getDeckName());
                    deckLabel.textFillProperty().bind(this.textFillProperty());
                    deckLabel.getStyleClass().add("list-cell");

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    HBox container = new HBox(deckLabel, spacer, buttonHbox);
                    container.setSpacing(10);
                    container.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(container);
                }
            }
        });
    }

    private void editDeck(Deck deck) throws IOException {
        System.out.println("Chỉnh sửa deck: " + deck.getDeckName());
        Scene deckBuilderScene = sceneManager.deckBuilderView();

        DeckBuilderView deckBuilderController = sceneManager.getDeckBuilderView();

        deckBuilderController.setCanEdit(true);
        deckBuilderController.loadDeck(deck, true);

        Main.primaryStage.setScene(deckBuilderScene);
        Main.primaryStage.centerOnScreen();
    }

    private void shareDeck(Deck deck) {
        System.out.println("Chia sẻ deck: " + deck.getDeckName());
        if (deck.getMainCardIDs().size() < 40) {
            mainMenuView.showSharingError(deck.getDeckName());
            deck.setVisibility("private");
        } else {
            mainMenuView.showSharingDone(deck.getDeckName());
            deck.setVisibility("public");
        }
        deckDAO.updateDeckVisibility(deck);
    }

    private void deleteDeck(Deck deck) {
        System.out.println("Xóa deck: " + deck.getDeckName());
        deckListView.getItems().remove(deck);
        deckDAO.deleteDeckFromSupabase(deck.getDeckId());
    }
}
