package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class FirstTable {
    public static int rocketsCount;
    public static ObservableList<RadarStat> radarStats = FXCollections.observableArrayList();
    public static Pair<Integer, Integer> setup;


    public static void createTable1() {
        rocketsCount = setup.getValue();        // наши ракеты
        radarStats.add(new RadarStat("Ракет в запасе", String.valueOf(rocketsCount)));
    }


}
