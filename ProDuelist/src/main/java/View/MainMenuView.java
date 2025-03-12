package View;

import Controller.DeckDAO;
import Controller.DeckListControl;
import Model.DatabaseConnection;
import Model.Deck;
import Model.DeckDataManager;
import Model.UserInformation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class MainMenuView implements Initializable {

    @FXML private ListView<Deck> decksListView;
    @FXML private AnchorPane createDeckBoard;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button createDeckButton;
    @FXML private TextField enterDeckName;

    private DeckDAO deckDAO = new DeckDAO();

    public void loadUserDeck(String userID) {
        DeckDataManager deckDataManager = new DeckDataManager();
        List<Deck> userDecks = deckDataManager.getDecksByUserId(userID);

        ObservableList<Deck> visibleDecks = FXCollections.observableArrayList();
        visibleDecks.addAll(userDecks);

        decksListView.setItems(visibleDecks);
        new DeckListControl(decksListView);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String userID = UserInformation.getUserID();
        if (userID != null) {
            loadUserDeck(userID);
        }
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

}
