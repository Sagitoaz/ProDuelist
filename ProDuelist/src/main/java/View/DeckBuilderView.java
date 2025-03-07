package View;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
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


    CardDAO cardDAO = new CardDAO();
    private static final int gridColums = 15;
    private static final int gridRows = 4;

    private final List<Integer> monsterRows = List.of(2, 3, 4, 5, 6);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupComboBox(comboboxType, comboboxRace, comboboxMonsterType, comboboxAttribute, comboboxLevel, comboboxResult);
        comboboxSort.setItems(FXCollections.observableArrayList(""));
        searchTextField.setOnAction(_ -> searchCards());
    }

    @FXML
    void onClickedBackButton(ActionEvent event) throws IOException {
        SceneManager sceneManager = new SceneManager();
        Main.primaryStage.setScene(sceneManager.mainMenuScene());
    }


    public static void setupComboBox(ComboBox<String> comboboxType, ComboBox<String> comboboxRace, ComboBox<String> comboboxMonsterType, ComboBox<String> comboboxAttribute, ComboBox<String> comboboxLevel, ComboBox<String> comboboxResult) {
        comboboxType.setItems(FXCollections.observableArrayList("Monster", "Spell", "Trap"));
        comboboxRace.setItems(FXCollections.observableArrayList("Dragon", "Warrior", "Spellcaster", "Fiend", "Fairy"));
        comboboxMonsterType.setItems(FXCollections.observableArrayList("Normal", "Effect", "Fusion", "Synchro", "XYZ", "Link"));
        comboboxAttribute.setItems(FXCollections.observableArrayList("Light", "Dark", "Water", "Fire", "Earth", "Wind", "Divine"));
        comboboxLevel.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
        comboboxResult.setItems(FXCollections.observableArrayList("30", "60", "90", "120"));
    }

    public void loadDeck(Deck deck) {
        mainDeckGrid.getChildren().clear();

        List<String> cardIds = deck.getCardIDs();

        for (int i = 0; i < cardIds.size(); i++) {
            int row = i / gridColums;
            int col = i % gridColums;
            if (row >= gridRows) break;

            Card card = CardDAO.getCardById(cardIds.get(i));
            if (card != null) {
                Image image = new Image(card.getImageUrl());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(50);
                imageView.setFitHeight(70);
                imageView.setPreserveRatio(true);
                imageView.setOnMouseEntered(_ -> {
                    cardNameLabel.setText(card.getName());
                    cardImageView.setImage(image);
                    String cardDesc = "[" + card.getType() + "] " + card.getRace() + "/" + card.getAttribute() + "\n";
                    if (card.getLevel() != 0) {
                        cardDesc += "[" + card.getLevel() + "★] " + card.getAttack() + "/" + card.getDefense() + "\n";
                    }
                    cardDesc += card.getDesc();
                    cardDescriptionArea.setText(cardDesc);
                });
                mainDeckGrid.add(imageView, col, row);
            }
        }
    }

    private void searchCards() {
        String query = searchTextField.getText().trim();
        if (query.isEmpty()) return;

        List<Card> cards = cardDAO.searchCardsByName(query, 30);
        searchResultList.getItems().clear();

        for (Card card : cards) {
            HBox cardBox = createCardDisplay(card);
            searchResultList.getItems().add(cardBox);
        }
    }

    private HBox createCardDisplay(Card card) {
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

        return cardContainer;
    }
}
