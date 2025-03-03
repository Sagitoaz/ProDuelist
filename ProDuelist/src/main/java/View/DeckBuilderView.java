package View;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DeckBuilderView implements Initializable {
    @FXML private GridPane gridPane;
    @FXML private ComboBox<String> comboboxType;
    @FXML private ComboBox<String> comboboxRace;
    @FXML private ComboBox<String> comboboxMonsterType;
    @FXML private ComboBox<String> comboboxAttribute;
    @FXML private ComboBox<String> comboboxLevel;
    @FXML private ComboBox<String> comboboxResult;
    @FXML private ComboBox<String> comboboxSort;

    private final List<Integer> monsterRows = List.of(2, 3, 4, 5, 6);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboboxType.setItems(FXCollections.observableArrayList("Monster", "Spell", "Trap"));
        comboboxRace.setItems(FXCollections.observableArrayList("Dragon", "Warrior", "Spellcaster", "Fiend", "Fairy"));
        comboboxMonsterType.setItems(FXCollections.observableArrayList("Normal", "Effect", "Fusion", "Synchro", "XYZ", "Link"));
        comboboxAttribute.setItems(FXCollections.observableArrayList("Light", "Dark", "Water", "Fire", "Earth", "Wind", "Divine"));
        comboboxLevel.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
        comboboxResult.setItems(FXCollections.observableArrayList("30", "60", "90", "120"));

    }

}
