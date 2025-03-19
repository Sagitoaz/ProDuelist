package View;

import Controller.DeckDAO;
import Controller.DeckDisplayFactory;
import Controller.SceneManager;
import Model.Deck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class SharingView implements Initializable {
    @FXML private TextField enterDeckNameText;
    @FXML private ListView<HBox> sharedDeckList;

    private final DeckDAO deckDAO = new DeckDAO();
    private final DeckDisplayFactory deckDisplayFactory = new DeckDisplayFactory();
    private final SceneManager sceneManager = SceneManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupEnterDeckNameText();
    }

    @FXML
    void onClickedBackButton(ActionEvent event) throws IOException {
        Main.primaryStage.setScene(sceneManager.mainMenuScene());
        Main.primaryStage.centerOnScreen();
    }

    private void setupEnterDeckNameText() {
        enterDeckNameText.setOnAction(_ -> {
            try {
                searchDeck();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void searchDeck() throws SQLException {
        String query = enterDeckNameText.getText().trim();
        if (query.isEmpty()) return;
        List<Deck> decks = deckDAO.searchDeckByName(query, 30);
        sharedDeckList.getItems().clear();
        for (Deck deck : decks) {
            HBox deckBox = deckDisplayFactory.createDeckBox(deck);
            sharedDeckList.getItems().add(deckBox);
        }
    }
}
