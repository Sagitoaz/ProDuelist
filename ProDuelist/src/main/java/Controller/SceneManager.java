package Controller;

import View.AccountSettingView;
import View.DeckBuilderView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneManager {
    private static SceneManager instance;
    private DeckBuilderView deckBuilderController;
    private final MusicController musicController = new MusicController();

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public Scene loginScene() throws IOException {
        Parent loginView = FXMLLoader.load(getClass().getResource("/FXML/LoginView/LoginView.fxml"));
        return new Scene(loginView);
    }

    public Scene registScene() throws IOException {
        Parent registerView = FXMLLoader.load(getClass().getResource("/FXML/RegisterView/RegisterView.fxml"));
        return new Scene(registerView);
    }

    public Scene mainMenuScene() throws IOException {
        Parent mainMenuScene = FXMLLoader.load(getClass().getResource("/FXML/MainMenuView/MainMenuView.fxml"));
        return new Scene(mainMenuScene);
    }

    public Scene deckBuilderView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DeckBuilderView/DeckBuilderView.fxml"));
        Parent deckBuilderRoot = loader.load();

        deckBuilderController = loader.getController();

        return new Scene(deckBuilderRoot);
    }

    public DeckBuilderView getDeckBuilderView() {
        return deckBuilderController;
    }

    public Scene SharingDeckView() throws IOException {
        Parent mainMenuScene = FXMLLoader.load(getClass().getResource("/FXML/SharingView/SharingView.fxml"));
        return new Scene(mainMenuScene);
    }

    public Scene AccountSettingView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AccountSettingView/AccountSettingView.fxml"));
        Parent accSettingScene = loader.load();

        AccountSettingView controller = loader.getController();
        controller.setMusicController(musicController);
        return new Scene(accSettingScene);
    }

    public MusicController getMusicController() {
        return musicController;
    }
}
