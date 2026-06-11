package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static org.example.Labels.rocketsLabel;
import static org.example.Labels.tab1Label;

public class Main extends Application {
    static StackPane root = new StackPane();//
    static Scene scene = new Scene(root, 1000, 700);
    static Arc sector = Sector.create(scene);
    static Pane missileLayer = new Pane();

    @Override
    public void start(Stage stage) {

        Images.createGraphicImage();
        Polygon object = Object.getPolygon();
        root.setAlignment(object, Pos.BOTTOM_CENTER);
        root.setMargin(object,new Insets(0.0,0.0,70.0,15.0));
        rocketsLabel = Labels.createRocketsLabel();
        Pane interceptorLayer = new Pane();
        missileLayer.setMouseTransparent(true);
        interceptorLayer.setMouseTransparent(true);
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
        Button carmMode = Buttons.createCarmMode(root);
        Button epr = createEprRocketButton();

        root.setStyle("-fx-background-color: black;");
        root.setAlignment(rocketsLabel, Pos.TOP_RIGHT);
        RadarLayer radarLayer = new RadarLayer(scene,sector);
        root.getChildren().add(radarLayer.radarLayer);
        root.getChildren().add(rocketsLabel);

        root.getChildren().addAll(object, missileLayer,interceptorLayer);
        root.getChildren().addAll(radarButton,launchButton,safeModeButton,tab76Button,
                trackAmplificationData, createARM1, createARM3,
                createARM2,createARM4,createARM5,createARM6,
                passiveSearch, createJammer, carmMode, epr);
        root.setAlignment(launchButton, Pos.BOTTOM_RIGHT);
        root.setAlignment(radarButton, Pos.BOTTOM_LEFT);
       // Buttons.radarOnAddListener();
        AnimationTimer timer = Buttons.createTimer( sector, missileLayer, interceptorLayer);
        timer.start();
        Buttons.setOnActionlaunchRocket(launchButton,interceptorLayer,sector,rocketsLabel);
        Buttons.setOnActionRadarButton(radarButton);
        Buttons.createSafeModeArc(radarLayer.radarLayer, sector);
        Buttons.setOnActionSafeButton(safeModeButton,radarLayer.radarLayer);

        scene.setOnMouseClicked(e -> {
            Buttons.targetInput.requestFocus();
        });
        stage.setTitle("Панель ЗРК");
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
    public static Button createEprRocketButton() {
        Label label = new Label("Добавити ракету та змінити ЕПР");
        label.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: red;" +
                        "-fx-font-weight: bold;"
        );
        root.setAlignment(label,Pos.TOP_RIGHT);
        StackPane.setMargin(label,new Insets(70, 0,0,0));
        root.getChildren().add(label);
        Button button = new Button();
        button.setPrefHeight(20);
        button.setPrefWidth(100);
        root.setAlignment(button, Pos.TOP_RIGHT);
        root.setMargin(button, new Insets(100,0,0,0));

        button.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setTitle("");
            VBox root = new VBox(10);
            Scene scene = new Scene(root,400,200);
            stage.setScene(scene);
            stage.show();
            HBox hBox1 = new HBox(10);
            Label labelAddButton = new Label("Add rocket");
            labelAddButton.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: red;" +
                            "-fx-font-weight: bold;"
            );
            Button addRocket = new Button("+");
            addRocket.setPrefWidth(50);
            addRocket.setPrefHeight(50);
            hBox1.getChildren().addAll(labelAddButton, addRocket);
            root.getChildren().addAll(hBox1);
            addRocket.setOnAction(event -> {
                FirstTable.rocketsCount++;
                rocketsLabel.setText("Rockets count " + FirstTable.rocketsCount);
            });
            HBox hBox = new HBox(10);
            Label EprLabel = new Label("Enter cross section");
            EprLabel.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: red;" +
                            "-fx-font-weight: bold;"
            );
            TextField epr = new TextField();
            Button enter = new Button("CHANGE");
            hBox.getChildren().addAll(EprLabel,epr,enter);
            enter.setOnAction(event -> {
                AntiRadiationMissile.EPR = Double.parseDouble(epr.getText());
                System.out.println(AntiRadiationMissile.EPR);
            });
            root.getChildren().addAll(hBox);
        });
        return button;
    }
}