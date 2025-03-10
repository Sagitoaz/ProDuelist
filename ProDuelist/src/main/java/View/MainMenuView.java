package View;

import Controller.DeckListControl;
import Model.Deck;
import Model.DeckDataManager;
import Model.UserInformation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuView implements Initializable {

    @FXML private ListView<Deck> decksListView;
    @FXML private TextField createDeckPane;

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
    void onClickedCreateDeckButton(ActionEvent event) {

    }
}
