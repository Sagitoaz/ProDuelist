    package Controller;

    import View.DeckBuilderView;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;

    import java.io.IOException;

    public class SceneManager {
        private DeckBuilderView deckBuilderController;

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
    }
