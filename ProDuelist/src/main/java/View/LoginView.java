package View;

import Controller.LoginControl;
import Controller.SceneManager;
import Controller.UserInformationManager;
import Model.UserInformation;
import Model.UserModel;
import Utils.UIHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginView implements Initializable {
    @FXML private PasswordField passwordField;
    @FXML private TextField usernameField;
    @FXML private Label errorLabel;
    @FXML private CheckBox stayLoginCheckbox;

    private final SceneManager sceneManager = SceneManager.getInstance();
    private final UIHelper uiHelper = new UIHelper();
    private final LoginControl loginControl = new LoginControl();
    private final UserModel userModel = new UserModel();
    private final UserInformationManager userInformationManager = new UserInformationManager();

    @FXML
    private void onClickRegisterButton() throws IOException {
        Main.primaryStage.setScene(sceneManager.registScene());
        Main.primaryStage.centerOnScreen();
    }

    @FXML
    void onClickedLoginButton() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String loginError = loginControl.validateLogin(username, password);

        uiHelper.showLabelMessage(errorLabel, loginError, 3.0);
        if (loginError == null){
            String userID = userModel.getUserID(username, password);
            UserInformation.setUserID(userID);
            UserInformation.setUserName(username);
            if (stayLoginCheckbox.isSelected()) {
                userInformationManager.saveUserInfor(userID, username);
            }
            Main.primaryStage.setScene(sceneManager.mainMenuScene());
            Main.primaryStage.centerOnScreen();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onClickedLoginButton();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onClickedLoginButton();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    void onClickedFacebookButton(ActionEvent event) {
        try {
            if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.facebook.com/clubproptit"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickedYoutubeButton(ActionEvent event) {
        try {
            if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.youtube.com/c/CLBL%E1%BA%ADpTr%C3%ACnhPTIT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickedForgotButton(ActionEvent event) {

    }

}
