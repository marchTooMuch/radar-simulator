package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1000, 700);
        Arc sector = Sector.create(scene);
        root.getChildren().add(new RadarLayer(scene,sector).radarLayer);
        FirstTable.createTable1();
        Text rocketsLabel = Labels.createRocketsLabel();
        root.getChildren().add(rocketsLabel);
        Pane missileLayer = new Pane();
        Pane interceptorLayer = new Pane();
        root.getChildren().addAll(missileLayer,interceptorLayer);
        Button launchButton = Buttons.createLaunchButton();
        Button radarButton = Buttons.createRadarButton();
        root.getChildren().add(radarButton);
        root.getChildren().add(launchButton);
        Buttons.radarOnAddListener();
        AnimationTimer timer = Buttons.createTimer(sector, missileLayer);
        timer.start();
        Buttons.setOnActionLaunchButton(launchButton,interceptorLayer,sector,rocketsLabel);
        Buttons.setOnActionRadarButton(radarButton);
        ObservableList<RadarStat> radarStats = FXCollections.observableArrayList();
        radarStats.add(new RadarStat("Ракет в запасе", "5"));
        for (int i = 0; i < FirstTable.setup.getKey(); i++) {
            Buttons.missiles.add(AntiRadiationMissile.spawnMissile(missileLayer, sector));
        }
        stage.setTitle("Панель ЗРК — сектор стрельбы");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}