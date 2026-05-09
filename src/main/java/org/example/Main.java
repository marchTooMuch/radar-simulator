package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static org.example.Labels.rocketsLabel;

public class Main extends Application {
    static StackPane root = new StackPane();//
    static Scene scene = new Scene(root, 1000, 700);
    static Arc sector = Sector.create(scene);
    static Pane missileLayer = new Pane();

    @Override
    public void start(Stage stage) {
        Images.createGraphicImage();
        Tab76.range = -1;
        Polygon object = Object.getPolygon();
        root.setAlignment(object, Pos.BOTTOM_CENTER);
        root.setMargin(object,new Insets(0.0,0.0,70.0,15.0));
        FirstTable.createTable1();
        rocketsLabel = Labels.createRocketsLabel();
        Pane interceptorLayer = new Pane();
        Button launchButton = Buttons.createLaunchButton(root);
        Button radarButton = Buttons.createRadarButton();
        Button safeModeButton = Buttons.createSafeModeButton(root);
        Button tab76Button = Buttons.createTab76Button(root);
        Button trackAmplificationData = Buttons.createTrackAmplificationButton(root);
        Button createARM1 = createARM1Button(root, missileLayer, sector);
        Button createARM3 = createARM3Button(root, missileLayer, sector);
        Button createARM2 = createARM2Button(root, missileLayer, sector);
        Button createARM4 = createARM4Button(root, missileLayer, sector);
        Button createARM5 = createARM5Button(root, missileLayer, sector);
        Button createARM6 = createARM6Button(root, missileLayer, sector);
        Button createJammer = createJammer(root, missileLayer, sector);
        Button passiveSearch = Buttons.createPassiveSearchButton(root);

        root.setStyle("-fx-background-color: black;");
        root.setAlignment(rocketsLabel, Pos.TOP_RIGHT);
        RadarLayer radarLayer = new RadarLayer(scene,sector);
        root.getChildren().add(radarLayer.radarLayer);
        root.getChildren().add(rocketsLabel);

        root.getChildren().addAll(object, missileLayer,interceptorLayer);
        root.getChildren().addAll(radarButton,launchButton,safeModeButton,tab76Button,
                trackAmplificationData, createARM1, createARM3, createARM2,createARM4,createARM5,createARM6,passiveSearch, createJammer);
        root.setAlignment(launchButton, Pos.BOTTOM_RIGHT);
        root.setAlignment(radarButton, Pos.BOTTOM_LEFT);
       // Buttons.radarOnAddListener();
        AnimationTimer timer = Buttons.createTimer( sector, missileLayer, interceptorLayer);
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

    public static Button createARM1Button(StackPane sp, Pane misseleLayer, Arc sector) {
        Button button = new Button("Create ARM1");
        sp.setAlignment(button, Pos.TOP_LEFT);
        sp.setMargin(button, new Insets(70,0,0,0));
        button.setPrefHeight(40);
        button.setPrefHeight(40);
        button.setOnAction(e -> {
            Buttons.missiles.add(ARM3.spawnMissileARM1(misseleLayer, sector));
        });
        return button;
    }
    public static Button createARM3Button(StackPane sp, Pane misseleLayer, Arc sector) {
        Button button = new Button("Create ARM2");
        sp.setAlignment(button, Pos.TOP_LEFT);
        sp.setMargin(button, new Insets(110,0,0,0));
        button.setPrefHeight(40);
        button.setPrefHeight(40);
        button.setOnAction(e -> {
            Buttons.missiles.add(ARM1.spawnMissileARM1(misseleLayer, sector));
        });
        return button;
    }
    public static Button createARM2Button(StackPane sp, Pane misseleLayer, Arc sector) {
        Button button = new Button("Create ARM3");
        sp.setAlignment(button, Pos.TOP_LEFT);
        sp.setMargin(button, new Insets(150,0,0,0));
        button.setPrefHeight(40);
        button.setPrefHeight(40);
        button.setOnAction(e -> {
            Buttons.missiles.add(ARM2.spawnMissileARM1(misseleLayer, sector));
        });
        return button;
    }
    public static Button createARM4Button(StackPane sp, Pane misseleLayer, Arc sector) {
        Button button = new Button("Create ARM4");
        sp.setAlignment(button, Pos.TOP_LEFT);
        sp.setMargin(button, new Insets(190,0,0,0));
        button.setPrefHeight(40);
        button.setPrefHeight(40);
        button.setOnAction(e -> {
            Buttons.missiles.add(ARM4.spawnMissileARM1(misseleLayer, sector));
        });
        return button;
    }
    public static Button createARM5Button(StackPane sp, Pane misseleLayer, Arc sector) {
        Button button = new Button("Create ARM5");
        sp.setAlignment(button, Pos.TOP_LEFT);
        sp.setMargin(button, new Insets(230,0,0,0));
        button.setPrefHeight(40);
        button.setPrefHeight(40);
        button.setOnAction(e -> {
            Buttons.missiles.add(ARM5.spawnMissileARM1(misseleLayer, sector));
        });
        return button;
    }
    public static Button createARM6Button(StackPane sp, Pane misseleLayer, Arc sector) {
        Button button = new Button("Create ARM6");
        sp.setAlignment(button, Pos.TOP_LEFT);
        sp.setMargin(button, new Insets(270,0,0,0));
        button.setPrefHeight(40);
        button.setPrefHeight(40);
        button.setOnAction(e -> {
            Buttons.missiles.add(ARM6.spawnMissileARM1(misseleLayer, sector));
        });
        return button;
    }
    public static Button createJammer(StackPane sp, Pane misseleLayer, Arc sector) {
        Button button = new Button("Create Jammer");
        sp.setAlignment(button, Pos.TOP_LEFT);
        sp.setMargin(button, new Insets(310,0,0,0));
        button.setPrefHeight(40);
        button.setPrefHeight(40);
        button.setOnAction(e -> {
            Jammer.createJammer();
        });
        return button;
    }
}