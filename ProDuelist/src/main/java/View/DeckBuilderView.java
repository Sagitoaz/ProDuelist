package View;

import Controller.*;
import Model.*;
import Utils.UIHelper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DeckBuilderView implements Initializable {
    @FXML private GridPane mainDeckGrid;
    @FXML private ComboBox<String> comboboxType;
    @FXML private ComboBox<String> comboboxRace;
    @FXML private ComboBox<String> comboboxMonsterType;
    @FXML private ComboBox<String> comboboxAttribute;
    @FXML private ComboBox<Integer> comboboxLevel;
    @FXML private ComboBox<Integer> comboboxResult;
    @FXML private ComboBox<String> banlistCombobox;
    @FXML private ListView<HBox> searchResultList;
    @FXML private TextField searchTextField;
    @FXML private TextField textfieldATK;
    @FXML private TextField textfieldDEF;
    @FXML private TextArea cardDescriptionArea;
    @FXML private ImageView cardImageView;
    @FXML private Label cardNameLabel;
    @FXML private Label extraDeckLabel;
    @FXML private Label sideDeckLabel;
    @FXML private Label unshareLabel;
    @FXML private Label editingDeckLabel;
    @FXML private Label mainDeckLabel;
    @FXML private HBox extraDeckHbox;
    @FXML private HBox sideDeckHbox;

    private Deck currentDeck;
    CardDisplayFactory cardDisplayFactory = new CardDisplayFactory();
    DeckDAO deckDAO = new DeckDAO();
    CardDAO cardDAO = new CardDAO();
    private DeckSectionManager mainDeckManager;
    private DeckSectionManager extraDeckManager;
    private DeckSectionManager sideDeckManager;
    private UIHelper uiHelper = new UIHelper();

    private static final int gridColums = 15;

    private boolean canEdit = true;
    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BanlistManager.loadBanlist("Null");
        setNodeOnAction();
        setUpComboboxBanlist();
        setUpComboboxType();
        setUpComboboxResult();
        setupSearchTextField();
        comboboxType.setOnAction(_ -> {
            setupComboBox();
            searchCards();
        });
        mainDeckManager = new MainDeckBuilderGridManager(mainDeckGrid, gridColums);
        extraDeckManager = new ExtraDeckBuilderManager(extraDeckHbox);
        sideDeckManager = new SideDeckBuilderManager(sideDeckHbox);
        mainDeckManager.setCardInfoComponents(cardNameLabel, cardImageView, cardDescriptionArea);
        extraDeckManager.setCardInfoComponents(cardNameLabel, cardImageView, cardDescriptionArea);
        sideDeckManager.setCardInfoComponents(cardNameLabel, cardImageView, cardDescriptionArea);
        mainDeckAddListener();
        extraDeckAddListener();
        sideDeckAddListener();
    }

    @FXML
    void onClickedBackButton() throws IOException {
        SceneManager sceneManager = SceneManager.getInstance();
        if (!canEdit) {
            Main.primaryStage.setScene(sceneManager.SharingDeckView());
        } else {
            Main.primaryStage.setScene(sceneManager.mainMenuScene());
        }
        Main.primaryStage.centerOnScreen();
    }

    @FXML
    void onClickedSaveButton() {
        if (!canEdit) {
            String userID = UserInformation.getUserID();
            if (userID == null) {
                return;
            }
            String deckID = UUID.randomUUID().toString();
            String visibility = "private";

            boolean createSuccess = deckDAO.addDeckToSupabase(deckID, userID, currentDeck.getDeckName(), visibility);
            if (createSuccess) {
                currentDeck.setDeckId(deckID);
                deckDAO.updateDeck(currentDeck);
                System.out.println("Thêm deck thành công!!!");
            }
        } else {
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
    }

    @FXML
    void onClickedClearButton() {
        mainDeckGrid.getChildren().clear();
        extraDeckHbox.getChildren().clear();
        sideDeckHbox.getChildren().clear();
    }

    @FXML
    void onClickedSortButton() {
        if (currentDeck == null) return;

        sortDeck(currentDeck.getMainCardIDs());
        sortDeck(currentDeck.getExtraCardIDs());
        sortDeck(currentDeck.getExtraCardIDs());

        updateDeckDisplay();
    }

    @FXML
    void onClickedShuffleButton() {
        if (currentDeck == null) return;

        shuffleDeck(currentDeck.getMainCardIDs());
        shuffleDeck(currentDeck.getExtraCardIDs());
        shuffleDeck(currentDeck.getExtraCardIDs());

        updateDeckDisplay();
    }

    @FXML
    void onClickedUnshareButton() {
        currentDeck.setVisibility("private");
        deckDAO.updateDeckVisibility(currentDeck);
        uiHelper.showLabelMessage(unshareLabel, "Your deck has become private", 3.0);
    }

    private void sortDeck(List<String> deck) {
        Collections.sort(deck);
    }

    private void shuffleDeck(List<String> deck) {
        Collections.shuffle(deck);
    }

    private void updateDeckDisplay() {
        mainDeckGrid.getChildren().clear();
        extraDeckHbox.getChildren().clear();
        sideDeckHbox.getChildren().clear();

        mainDeckManager.loadDeckSection(currentDeck.getMainCardIDs());
        extraDeckManager.loadDeckSection(currentDeck.getExtraCardIDs());
        sideDeckManager.loadDeckSection(currentDeck.getSideCardIDs());
    }

    private void mainDeckAddListener() {
        mainDeckGrid.getChildren().addListener((ListChangeListener<Node>) change -> {
            mainDeckLabel.setText("Main Deck [" + mainDeckGrid.getChildren().size() + "]");
        });
    }

    private void extraDeckAddListener() {
        extraDeckHbox.getChildren().addListener((ListChangeListener<Node>) _ -> {
            extraDeckLabel.setText("Extra Deck [" + extraDeckHbox.getChildren().size() + "]");
        });
    }

    private void sideDeckAddListener() {
        sideDeckHbox.getChildren().addListener((ListChangeListener<Node>) _ -> {
            sideDeckLabel.setText("Side Deck [" + sideDeckHbox.getChildren().size() + "]");
        });
    }

    private void setNodeOnAction() {
        comboboxMonsterType.setOnAction(_ -> searchCards());
        comboboxRace.setOnAction(_ -> searchCards());
        comboboxResult.setOnAction(_ -> searchCards());
        comboboxAttribute.setOnAction(_ -> searchCards());
        comboboxLevel.setOnAction(_ -> searchCards());
        textfieldATK.setOnAction(_ -> searchCards());
        textfieldDEF.setOnAction(_ -> searchCards());
    }

    private void setUpComboboxBanlist() {
        banlistCombobox.setItems(FXCollections.observableArrayList("TCG", "OCG", "Master Duel", "WCS", "Null"));
        banlistCombobox.setValue("Null");
        banlistCombobox.setOnAction(_ -> updateBanlist());
    }

    private void setUpComboboxType() {
        comboboxType.setItems(FXCollections.observableArrayList("Monster", "Spell", "Trap", "Null"));
    }

    private void setUpComboboxResult() {
        comboboxResult.setItems(FXCollections.observableArrayList(30, 60, 90, 120));
        comboboxResult.setValue(30);
    }

    private void setupComboBox() {
        String selectedType = comboboxType.getValue();
        if (selectedType == null) return;
        if (selectedType.equals("Monster")) {
            changeVisibleIfIsMonsterOrNot(true);
            comboboxRace.setItems(FXCollections.observableArrayList(
                    "Aqua", "Beast", "Beast-Warrior", "Creator God", "Cyberse", "Dinosaur", "Divine-Beast", "Dragon",
                    "Fairy", "Fiend", "Fish", "Insect", "Illusion", "Machine", "Plant", "Psychic", "Pyro", "Reptile",
                    "Rock", "Sea Serpent", "Spellcaster", "Thunder", "Warrior", "Winged Beast", "Wyrm", "Zombie", null));
            comboboxMonsterType.setItems(FXCollections.observableArrayList("Normal", "Effect", "Fusion", "Synchro", "XYZ", "Link", "Pendulum", null));
            comboboxAttribute.setItems(FXCollections.observableArrayList("Light", "Dark", "Water", "Fire", "Earth", "Wind", "Divine", null));
            comboboxLevel.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, null));
        } else if (selectedType.equals("Spell")) {
            comboboxRace.setItems(FXCollections.observableArrayList("Continuous", "Equip", "Field", "Normal", "Quick-Play", "Ritual", null));
            changeVisibleIfIsMonsterOrNot(false);
        } else if (selectedType.equals("Trap")) {
            comboboxRace.setItems(FXCollections.observableArrayList("Continuous", "Counter", "Normal", "Normal", null));
            changeVisibleIfIsMonsterOrNot(false);
        } else if (selectedType.equals("Null")) {
            comboboxRace.setVisible(false);
            changeVisibleIfIsMonsterOrNot(false);
        }
    }

    private void changeVisibleIfIsMonsterOrNot(boolean isMonster) {
        comboboxMonsterType.setVisible(isMonster);
        comboboxAttribute.setVisible(isMonster);
        comboboxLevel.setVisible(isMonster);
        textfieldATK.setVisible(isMonster);
        textfieldDEF.setVisible(isMonster);
    }

    private void setupSearchTextField() {
        searchTextField.setOnAction(_ -> searchCards());
    }

    public void loadDeck(Deck deck, boolean editable) {
        this.currentDeck = deck;
        editingDeckLabel.setText("Editing Deck: " + currentDeck.getDeckName());
        DeckSectionManager.resetCardCountMap();

        mainDeckManager.setEditable(!editable);
        extraDeckManager.setEditable(!editable);
        sideDeckManager.setEditable(!editable);

        mainDeckManager.loadDeckSection(deck.getMainCardIDs());
        extraDeckManager.loadDeckSection(deck.getExtraCardIDs());
        sideDeckManager.loadDeckSection(deck.getSideCardIDs());
    }

    private void searchCards() {
        List<Card> cards;
        String name = searchTextField.getText().trim();
        int limitCard = comboboxResult.getValue();

        if (name.isEmpty()) return;
        searchResultList.getItems().clear();
        String selectedType = comboboxType.getValue();


        if (selectedType == null || selectedType.equals("Null")) {
            cards = cardDAO.searchCardsByName(name, limitCard);
        } else {
            String selectedRace = comboboxRace.getValue();
            String selectedAttribute = comboboxAttribute.isVisible() ? comboboxAttribute.getValue() : null;
            String selectedMonsterType = comboboxMonsterType.isVisible() ? comboboxMonsterType.getValue() : null;
            Integer selectedLevel = comboboxLevel.isVisible() && comboboxLevel.getValue() != null ? comboboxLevel.getValue() : null;
            Integer atkValue = textfieldATK.isVisible() && !textfieldATK.getText().isEmpty() ? Integer.parseInt(textfieldATK.getText()) : null;
            Integer defValue = textfieldDEF.isVisible() && !textfieldDEF.getText().isEmpty() ? Integer.parseInt(textfieldDEF.getText()) : null;

            cards = cardDAO.searchCardsByFilter(name, selectedType, selectedRace, selectedAttribute, selectedMonsterType, selectedLevel, atkValue, defValue, limitCard);
        }

        for (Card card : cards) {
            HBox cardBox = cardDisplayFactory.createCardBox(card);
            StackPane cardPane = (StackPane) cardBox.getChildren().getFirst();
            ImageView imageView = (ImageView) cardPane.getChildren().getFirst();
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
            if (node instanceof StackPane stackPane) {
                String cardId = (String) stackPane.getUserData();
                if (cardId != null) {
                    newCardIds.add(cardId);
                }
            }
        });
        return newCardIds;
    }

    private void updateBanlist() {
        String banlistSelected = banlistCombobox.getValue();
        BanlistManager.loadBanlist(banlistSelected);
        if (currentDeck != null) {
            mainDeckManager.loadDeckSection(currentDeck.getMainCardIDs());
            extraDeckManager.loadDeckSection(currentDeck.getExtraCardIDs());
            sideDeckManager.loadDeckSection(currentDeck.getSideCardIDs());
        }
    }
}
