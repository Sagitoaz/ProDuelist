package View;

import Controller.DeckDAO;
import Controller.DeckListControl;
import Controller.SceneManager;
import Model.Deck;
import Model.DeckDataManager;
import Model.UserInformation;
import Utils.UIHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class MainMenuView implements Initializable {

    @FXML private ListView<Deck> decksListView;
    @FXML private AnchorPane createDeckBoard;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button createDeckButton;
    @FXML private Button searchDeckButton;
    @FXML private TextField searchMyDeckText;
    @FXML private TextField enterDeckName;
    @FXML private Label labelDone;
    @FXML private Label labelError;

    private final UIHelper uiHelper = new UIHelper();
    private final DeckDAO deckDAO = new DeckDAO();
    private final SceneManager sceneManager = SceneManager.getInstance();
    private final DeckDataManager deckDataManager = new DeckDataManager();
    private ObservableList<Deck> visibleDecks = FXCollections.observableArrayList();

    public void loadUserDeck(String userID) {
        List<Deck> userDecks = deckDataManager.getDecksByUserId(userID);
        visibleDecks.clear();
        visibleDecks.addAll(userDecks);
        decksListView.setItems(visibleDecks);
        new DeckListControl(decksListView, this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String userID = UserInformation.getUserID();
        if (userID != null) {
            loadUserDeck(userID);
        }
        searchMyDeckText.setOnAction(_ -> searchMyDeck(userID));
    }

    @FXML
    void onClickedCreateButton(ActionEvent event) {
        createDeckBoard.setVisible(true);
    }

    @FXML
    void onClickedCancelButton(ActionEvent event) {
        enterDeckName.clear();
        createDeckBoard.setVisible(false);
    }

    @FXML
    void onClickedConfirmButton(ActionEvent event) {
        String deckName = enterDeckName.getText().trim();
        if (deckName.isEmpty()) {
            return;
        }

        String userID = UserInformation.getUserID();
        if (userID == null) {
            return;
        }

        String deckID = UUID.randomUUID().toString();
        String visibility = "private";

        boolean createSuccess = deckDAO.addDeckToSupabase(deckID, userID, deckName, visibility);
        if (createSuccess) {
            enterDeckName.clear();
            createDeckBoard.setVisible(false);
            loadUserDeck(userID);
        }
    }

    public void showSharingError(String deckName) {
        uiHelper.showLabelMessage(labelError, deckName + " isn't eligible for sharing", 3.0);
    }

    public void showSharingDone(String deckName) {
        uiHelper.showLabelMessage(labelDone, deckName + " Has Been Shared", 3.0);
    }

    @FXML
    void onClickedSearchDeckButton(ActionEvent event) throws IOException {
        Main.primaryStage.setScene(sceneManager.SharingDeckView());
        Main.primaryStage.centerOnScreen();
    }

    @FXML
    void onClickedAccountSettingButton(ActionEvent event) throws IOException {
        Main.primaryStage.setScene(sceneManager.AccountSettingView());
        Main.primaryStage.centerOnScreen();
    }

    private void searchMyDeck(String userID) {
        String deckName = searchMyDeckText.getText().trim().toLowerCase();
        List<Deck> userDecks = deckDataManager.getDecksByUserId(userID);
        List<Deck> resultSearchingDeck = new ArrayList<>();
        for (Deck deck : userDecks) {
            if (deck.getDeckName().trim().toLowerCase().contains(deckName)) {
                resultSearchingDeck.add(deck);
            }
        }
        visibleDecks.clear();
        decksListView.getItems().clear();
        visibleDecks.addAll(resultSearchingDeck);
        decksListView.setItems(visibleDecks);
        new DeckListControl(decksListView, this);
    }

}
