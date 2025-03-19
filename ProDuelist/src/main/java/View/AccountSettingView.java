package View;

import Controller.MusicController;
import Controller.SceneManager;
import Controller.UserInformationManager;
import Model.UserInformation;
import Model.UserModel;
import Utils.UIHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountSettingView implements Initializable {
    private final SceneManager sceneManager = SceneManager.getInstance();
    private final UIHelper uiHelper = new UIHelper();
    private final UserModel userModel = new UserModel();
    private MusicController musicController;
    private final UserInformationManager userInformationManager = new UserInformationManager();

    @FXML private Label changePassLabel;
    @FXML private Button chooseMusicButton;
    @FXML private Label errorLabel;
    @FXML private Label helloLabel;
    @FXML private Label musicLabel;
    @FXML private Slider volumeSlider;
    @FXML private TextField confirmPassText;
    @FXML private TextField newPassText;
    @FXML private TextField currentPassText;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        helloLabel.setText("Hello " + UserInformation.getUserName());
        updateMusicUI();
    }

    @FXML
    void onClickedLogoutButton(ActionEvent event) throws IOException {
        Main.primaryStage.setScene(sceneManager.loginScene());
        Main.primaryStage.centerOnScreen();
        userInformationManager.deleteUserInfo();
    }

    @FXML
    void onClickedBackButton(ActionEvent event) throws IOException {
        Main.primaryStage.setScene(sceneManager.mainMenuScene());
        Main.primaryStage.centerOnScreen();
    }

    @FXML
    void onClickedChangePassButton(ActionEvent event) {
        String currentPass = currentPassText.getText();
        String newPass = newPassText.getText();
        String confirmPass = confirmPassText.getText();
        if (currentPass == null || newPass == null || confirmPass == null) {
            uiHelper.showLabelMessage(errorLabel, "Please fill in all fields!!!", 3.0);
            return;
        }
        if (!userModel.checkPassword(UserInformation.getUserID(), currentPass)) {
            uiHelper.showLabelMessage(errorLabel, "Incorrect Password!!!", 3.0);
            return;
        }
        if (!newPass.equals(confirmPass)) {
            uiHelper.showLabelMessage(errorLabel, "Passwords do not match!!!", 3.0);
            return;
        }
        userModel.updatePassword(UserInformation.getUserID(), newPass);
        uiHelper.showLabelMessage(errorLabel, "Password has been changed!!!", 3.0);
        confirmPassText.clear();
        newPassText.clear();
        currentPassText.clear();
    }

    @FXML
    void onClickedChooseMusicButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"));
        fileChooser.setTitle("Select audio file");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String filePath = selectedFile.toURI().toString();
            musicLabel.setText(selectedFile.getName());

            musicController.playMusic(filePath, volumeSlider);
            musicController.saveMusicToJson(filePath);
        }
    }

    public void setMusicController(MusicController musicController) {
        this.musicController = musicController;
        updateMusicUI();
    }

    private void updateMusicUI() {
        if (musicController != null && musicController.getMediaPlayer() != null) {
            String currentPath = musicController.getCurrentMusicTrackPath();
            if (currentPath != null) {
                File file = new File(java.net.URI.create(currentPath));
                musicLabel.setText(file.getName());
            }
            volumeSlider.setValue(musicController.getMediaPlayer().getVolume() * 100);
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                musicController.getMediaPlayer().setVolume(newVal.doubleValue() / 100);
            });
        }
    }

}
