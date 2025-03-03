package View;

import Controller.LoginControl;
import Controller.SceneManager;
import Model.UserModel;
import Utils.UIHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginView {
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

    private final SceneManager sceneManager = new SceneManager();
    private final UIHelper uiHelper = new UIHelper();
    private final LoginControl loginControl = new LoginControl();
    @FXML
    private void onClickRegisterButton() throws IOException {
        Main.primaryStage.setScene(sceneManager.registScene());
    }

    @FXML
    void onClickedLoginButton() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String loginError = loginControl.validateLogin(username, password);
        uiHelper.showLabelMessage(errorLabel, loginError, 3.0);
        if (loginError == null){
            Main.primaryStage.setScene(sceneManager.mainMenuScene());
        }
    }
}
