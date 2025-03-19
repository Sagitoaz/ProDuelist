package Controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

public class MusicController {
    private MediaPlayer mediaPlayer;
    private final String musicFilePath = "data/music.json";
    private String currentMusicTrackPath;

    public void playMusic(String filePath, Slider volumeSlider) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Media media = new Media(filePath);
        mediaPlayer = new MediaPlayer(media);
        this.currentMusicTrackPath = filePath;
        mediaPlayer.play();
        mediaPlayer.play();

        volumeSlider.setValue(50);
        mediaPlayer.setVolume(volumeSlider.getValue() / 100);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue() / 100);
        });

        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    public void saveMusicToJson(String filePath) {
        try (FileWriter writer = new FileWriter(musicFilePath)) {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("musicPath", filePath);
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMusicFromJson(Label musicLabel, Slider volumeSlider) {
        File file = new File(musicFilePath);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                String filePath = jsonObject.get("musicPath").getAsString();
                File musicFile = new File(URI.create(filePath));

                if (musicFile.exists()) {
                    musicLabel.setText(musicFile.getName());
                    playMusic(filePath, volumeSlider);
                } else {
                    musicLabel.setText("Chọn file nhạc...");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public String getCurrentMusicTrackPath() {
        return currentMusicTrackPath;
    }

    public void autoPlayMusic() {
        File file = new File(musicFilePath);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                String filePath = jsonObject.get("musicPath").getAsString();
                File musicFile = new File(URI.create(filePath));
                if (musicFile.exists()) {
                    Slider defaultSlider = new Slider();
                    defaultSlider.setValue(50);
                    playMusic(filePath, defaultSlider);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
