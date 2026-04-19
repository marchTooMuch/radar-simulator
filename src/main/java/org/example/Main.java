package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static org.example.Labels.rocketsLabel;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Tab76.range = -1;
        StackPane root = new StackPane();//
        Scene scene = new Scene(root, 1000, 700);
        Arc sector = Sector.create(scene);

        FirstTable.createTable1();
        rocketsLabel = Labels.createRocketsLabel();
        Pane missileLayer = new Pane();
        Pane interceptorLayer = new Pane();
        Button launchButton = Buttons.createLaunchButton(root);
        Button radarButton = Buttons.createRadarButton();
        Button safeModeButton = Buttons.createSafeModeButton(root);
        Button tab76Button = Buttons.createTab76Button(root);
        Button trackAmplificationData = Buttons.createTrackAmplificationButton(root);
        root.setStyle("-fx-background-color: black;");
        root.setAlignment(rocketsLabel, Pos.TOP_RIGHT);
        RadarLayer radarLayer = new RadarLayer(scene,sector);



        root.getChildren().add(radarLayer.radarLayer);
        root.getChildren().add(rocketsLabel);
        root.getChildren().addAll(missileLayer,interceptorLayer);
        root.setAlignment(launchButton, Pos.BOTTOM_RIGHT);
        root.setAlignment(radarButton, Pos.BOTTOM_LEFT);
        root.getChildren().addAll(radarButton,launchButton,safeModeButton,tab76Button, trackAmplificationData);
        Buttons.radarOnAddListener();
        AnimationTimer timer = Buttons.createTimer(sector, missileLayer, interceptorLayer);
        timer.start();
        Buttons.setOnActionlaunchRocket(launchButton,interceptorLayer,sector,rocketsLabel);
        Buttons.setOnActionRadarButton(radarButton);
        Buttons.createSafeModeArc(radarLayer.radarLayer, sector);
        Buttons.setOnActionSafeButton(safeModeButton,radarLayer.radarLayer);

        ObservableList<RadarStat> radarStats = FXCollections.observableArrayList();
        radarStats.add(new RadarStat("Ракет в запасе", "5"));
        for (int i = 0; i < FirstTable.setup.getKey(); i++) {
            Buttons.missiles.add(AntiRadiationMissile.spawnMissile(missileLayer, sector));
        }
        stage.setTitle("Панель ЗРК — сектор стрельбы");
        stage.setScene(scene);
        stage.show();
    }
}