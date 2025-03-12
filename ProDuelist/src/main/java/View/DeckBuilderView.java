package View;

import Controller.*;
import Model.Card;
import Model.CardDAO;
import Model.CardImageView;
import Model.Deck;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

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
    @FXML private ComboBox<String> banlistCombobox;
    @FXML private ListView<HBox> searchResultList;
    @FXML private TextField searchTextField;
    @FXML private TextArea cardDescriptionArea;
    @FXML private ImageView cardImageView;
    @FXML private Label cardNameLabel;
    @FXML private Label editingDeckLabel;
    @FXML private HBox extraDeckHbox;
    @FXML private HBox sideDeckHbox;

    private Deck currentDeck;
    CardDisplayFactory cardDisplayFactory = new CardDisplayFactory();
    DeckDAO deckDAO = new DeckDAO();
    CardDAO cardDAO = new CardDAO();
    private DeckSectionManager mainDeckManager;
    private DeckSectionManager extraDeckManager;
    private DeckSectionManager sideDeckManager;

    private static final int gridColums = 15;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupComboBox();
        setupSearchTextField();
        mainDeckManager = new MainDeckBuilderGridManager(mainDeckGrid, gridColums);
        extraDeckManager = new ExtraDeckBuilderManager(extraDeckHbox);
        sideDeckManager = new SideDeckBuilderManager(sideDeckHbox);
        mainDeckManager.setCardInfoComponents(cardNameLabel, cardImageView, cardDescriptionArea);
        extraDeckManager.setCardInfoComponents(cardNameLabel, cardImageView, cardDescriptionArea);
        sideDeckManager.setCardInfoComponents(cardNameLabel, cardImageView, cardDescriptionArea);
    }

    @FXML
    void onClickedBackButton() throws IOException {
        SceneManager sceneManager = new SceneManager();
        Main.primaryStage.setScene(sceneManager.mainMenuScene());
        Main.primaryStage.centerOnScreen();
    }

    @FXML
    void onClickedSaveButton() {
        List<String> newMainCardIds = updateDeckIds(mainDeckGrid);
        List<String> newExtraCardIds = updateDeckIds(extraDeckHbox);
        List<String> newSideCardIds = updateDeckIds(sideDeckHbox);
        if (currentDeck != null) {
            currentDeck.setMainCardIDs(new ArrayList<>(newMainCardIds));
            currentDeck.setExtraCardIDs(new ArrayList<>(newExtraCardIds));
            currentDeck.setSideCardIDs(new ArrayList<>(newSideCardIds));
        }
        deckDAO.updateDeck(currentDeck);
    }

    private void setupComboBox() {
        comboboxType.setItems(FXCollections.observableArrayList("Monster", "Spell", "Trap"));
        comboboxRace.setItems(FXCollections.observableArrayList(
                "Aqua", "Beast", "Beast-Warrior", "Creator God", "Cyberse", "Dinosaur", "Divine-Beast", "Dragon",
                "Fairy", "Fiend", "Fish", "Insect", "Illusion", "Machine", "Plant", "Psychic", "Pyro", "Reptile",
                "Rock", "Sea Serpent", "Spellcaster", "Thunder", "Warrior", "Winged Beast", "Wyrm", "Zombie"));
        comboboxMonsterType.setItems(FXCollections.observableArrayList("Normal", "Effect", "Fusion", "Synchro", "XYZ", "Link"));
        comboboxAttribute.setItems(FXCollections.observableArrayList("Light", "Dark", "Water", "Fire", "Earth", "Wind", "Divine"));
        comboboxLevel.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
        comboboxResult.setItems(FXCollections.observableArrayList("30", "60", "90", "120"));
        comboboxSort.setItems(FXCollections.observableArrayList(""));
        banlistCombobox.setItems(FXCollections.observableArrayList("TCG Banlist", "OCG Banlist", "Master Duel", "World ChampionShip"));
    }

    private void setupSearchTextField() {
        searchTextField.setOnAction(_ -> searchCards());
    }

    public void loadDeck(Deck deck) {
        this.currentDeck = deck;
        editingDeckLabel.setText("Editing Deck: " + currentDeck.getDeckName());
        DeckSectionManager.resetCardCountMap();
        mainDeckManager.loadDeckSection(deck.getMainCardIDs());
        extraDeckManager.loadDeckSection(deck.getExtraCardIDs());
        sideDeckManager.loadDeckSection(deck.getSideCardIDs());
    }

    private void searchCards() {
        String query = searchTextField.getText().trim();
        if (query.isEmpty()) return;
        List<Card> cards = cardDAO.searchCardsByName(query, 30);
        searchResultList.getItems().clear();

        for (Card card : cards) {
            HBox cardBox = cardDisplayFactory.createCardBox(card);
            ImageView imageView = (ImageView) cardBox.getChildren().getFirst();
            showCardInfor(imageView, new Image(card.getImageUrl()), card);
            searchResultList.getItems().add(cardBox);
        }

    }

    private void showCardInfor(ImageView imageView, Image image, Card card) {
        imageView.setOnMouseEntered(_ -> {
            cardNameLabel.setText(card.getName());
            cardImageView.setImage(image);
            DeckSectionManager.setCardDesc(card, cardDescriptionArea);
        });
    }

    private List<String> updateDeckIds(Parent parent) {
        List<String> newCardIds = new ArrayList<>();
        parent.getChildrenUnmodifiable().forEach(node -> {
            if (node instanceof ImageView) {
                String cardId = (String) node.getUserData();
                newCardIds.add(cardId);
            }
        });
        return newCardIds;
    }
}
