package Utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class UIHelper {
    public void showLabelMessage(Label label, String message, double second) {
        label.setText(message);
        label.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(second), event -> {
            label.setVisible(false);
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
