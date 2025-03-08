package View;

import Controller.CardDisplayFactory;
import Controller.DeckDAO;
import Controller.SceneManager;
import Model.Card;
import Model.CardDAO;
import Model.Deck;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DeckBuilderView implements Initializable {
    @FXML private GridPane mainDeckGrid;
    @FXML private ComboBox<String> comboboxType;
    @FXML private ComboBox<String> comboboxRace;
    @FXML private ComboBox<String> comboboxMonsterType;
    @FXML private ComboBox<String> comboboxAttribute;
    @FXML private ComboBox<String> comboboxLevel;
    @FXML private ComboBox<String> comboboxResult;
    @FXML private ComboBox<String> comboboxSort;
    @FXML private ListView<HBox> searchResultList;
    @FXML private TextField searchTextField;
    @FXML private TextArea cardDescriptionArea;
    @FXML private ImageView cardImageView;
    @FXML private Label cardNameLabel;
    @FXML private Button backButton;
    @FXML private Button saveButton;

    private Deck currentDeck;
    CardDisplayFactory cardDisplayFactory = new CardDisplayFactory();
    DeckDAO deckDAO = new DeckDAO();
    CardDAO cardDAO = new CardDAO();
    private static final int gridColums = 15;
    private static final int gridRows = 4;

    private final List<Integer> monsterRows = List.of(2, 3, 4, 5, 6);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupComboBox(comboboxType, comboboxRace, comboboxMonsterType, comboboxAttribute, comboboxLevel, comboboxResult, comboboxSort);
        setupSearchTextField();
        setupMainDeckGridDragDrop();
    }

    @FXML
    void onClickedBackButton(ActionEvent event) throws IOException {
        SceneManager sceneManager = new SceneManager();
        Main.primaryStage.setScene(sceneManager.mainMenuScene());
    }

    @FXML
    void onClickedSaveButton(ActionEvent event) {
        List<String> newCardIds = new ArrayList<>();
        mainDeckGrid.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                String cardId = (String) node.getUserData();
                newCardIds.add(cardId);
            }
        });

        if (currentDeck != null) {
            currentDeck.getCardIDs().clear();
            currentDeck.getCardIDs().addAll(newCardIds);

            deckDAO.updateDeck(currentDeck);

            System.out.println("Deck đã được cập nhật card_ids thành công!");
        }
    }


    private void setupComboBox(ComboBox<String> comboboxType, ComboBox<String> comboboxRace, ComboBox<String> comboboxMonsterType, ComboBox<String> comboboxAttribute, ComboBox<String> comboboxLevel, ComboBox<String> comboboxResult, ComboBox<String> comboboxSort) {
        comboboxType.setItems(FXCollections.observableArrayList("Monster", "Spell", "Trap"));
        comboboxRace.setItems(FXCollections.observableArrayList("Dragon", "Warrior", "Spellcaster", "Fiend", "Fairy"));
        comboboxMonsterType.setItems(FXCollections.observableArrayList("Normal", "Effect", "Fusion", "Synchro", "XYZ", "Link"));
        comboboxAttribute.setItems(FXCollections.observableArrayList("Light", "Dark", "Water", "Fire", "Earth", "Wind", "Divine"));
        comboboxLevel.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
        comboboxResult.setItems(FXCollections.observableArrayList("30", "60", "90", "120"));
        comboboxSort.setItems(FXCollections.observableArrayList(""));
    }

    private void setupSearchTextField() {
        searchTextField.setOnAction(_ -> searchCards());
    }

    private void setupMainDeckGridDragDrop() {
        mainDeckGrid.setOnDragOver(event -> {
            if (event.getGestureSource() != mainDeckGrid && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        mainDeckGrid.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String cardId = db.getString();
                Card card = CardDAO.getCardById(cardId);
                if (card != null) {
                    int count = mainDeckGrid.getChildren().size();
                    int row = count / gridColums;
                    int col = count % gridColums;

                    createCardImage(card, col, row);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void loadDeck(Deck deck) {
        this.currentDeck = deck;
        mainDeckGrid.getChildren().clear();

        List<String> cardIds = deck.getCardIDs();

        for (int i = 0; i < cardIds.size(); i++) {
            int row = i / gridColums;
            int col = i % gridColums;
            if (row >= gridRows) break;

            Card card = CardDAO.getCardById(cardIds.get(i));
            if (card != null) {
                createCardImage(card, col, row);
            }
        }
    }

    private void searchCards() {
        String query = searchTextField.getText().trim();
        if (query.isEmpty()) return;

        List<Card> cards = cardDAO.searchCardsByName(query, 30);
        searchResultList.getItems().clear();

        for (Card card : cards) {
            HBox cardBox = cardDisplayFactory.createCardBox(card);

            ImageView imageView = (ImageView) cardBox.getChildren().getFirst();
            Image image = new Image(card.getImageUrl());
            showCardInfor(imageView, image, card);

            searchResultList.getItems().add(cardBox);
        }

    }

    private void showCardInfor(ImageView imageView, Image image, Card card) {
        imageView.setOnMouseEntered(_ -> {
            cardNameLabel.setText(card.getName());
            cardImageView.setImage(image);
            String cardDesc = "[" + card.getType() + "] " + card.getRace() + "/" + card.getAttribute() + "\n";
            if (card.getAttribute() != null) {
                cardDesc += "[" + card.getLevel() + "★] " + card.getAttack() + "/" + card.getDefense() + "\n";
            }
            cardDesc += card.getDesc();
            cardDescriptionArea.setText(cardDesc);
        });
    }

    private void createCardImage(Card card, int col, int row) {
        Image image = new Image(card.getImageUrl());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(70);
        imageView.setPreserveRatio(true);
        showCardInfor(imageView, image, card);

        imageView.setUserData(String.valueOf(card.getId()));

        mainDeckGrid.add(imageView, col, row);
    }
}
