package org.example.produelist;

import View.DeckBuilderView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private GridPane gridPane;
    @FXML public ComboBox<String> comboboxType;
    @FXML public ComboBox<String> comboboxRace;
    @FXML public ComboBox<String> comboboxMonsterType;
    @FXML public ComboBox<String> comboboxAttribute;
    @FXML public ComboBox<String> comboboxLevel;
    @FXML public ComboBox<String> comboboxResult;
    @FXML public ComboBox<String> comboboxSort;

    private final List<Integer> monsterRows = List.of(2, 3, 4, 5, 6);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DeckBuilderView.setupComboBox(comboboxType, comboboxRace, comboboxMonsterType, comboboxAttribute, comboboxLevel, comboboxResult);

//        comboboxType.setOnAction(event -> updateGridVisibility());
//        comboboxType.valueProperty().addListener((observable, oldValue, newValue) -> updateGridVisibility());
//        comboboxType.setValue("Monster");
//        updateGridVisibility();
    }

    private void updateGridVisibility() {
        boolean isMonster = "Monster".equals(comboboxType.getValue());

        for (int rowIndex : monsterRows) {
            RowConstraints row = gridPane.getRowConstraints().get(rowIndex);
            row.setPrefHeight(isMonster ? 30 : 0); // 30px = hiển thị, 0 = ẩn hoàn toàn
            row.setMinHeight(isMonster ? 10 : 0);
            row.setMaxHeight(isMonster ? Double.MAX_VALUE : 0);
        }

        if (isMonster) {
            comboboxSort.setItems(FXCollections.observableArrayList("Default", "Alphabetical Ascending", "Alphabetical Descending", "ATK Ascending", "ATK Descending", "DEF Ascending", "DEF Descending"));
        } else {
            comboboxSort.setItems(FXCollections.observableArrayList("Default", "Alphabetical Ascending", "Alphabetical Descending"));

        }
    }
}
