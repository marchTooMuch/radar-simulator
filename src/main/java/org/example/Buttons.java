package org.example;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.animation.AnimationTimer;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class Buttons {
    public static Boolean radarOn = true;
    public static Boolean isCarmModeOn = false;
    public static BooleanProperty safeModeOn = new SimpleBooleanProperty(true);
    public static BooleanProperty passiveSearchOn = new SimpleBooleanProperty(false);
    public static ObservableList<AntiRadiationMissile> missiles =  FXCollections.observableArrayList();
    //public static List<AntiRadiationMissile> missiles = new ArrayList<>();
    public static List<InterceptorMissile> interceptors = new ArrayList<>();
    public static TextField targetInput;
    public static Arc safeModeArc;
    public static TableView<AntiRadiationMissile> tableView;
    public static boolean tab1Yes = false;
    public static boolean tab1No = true;

    public static Button createSafeModeButton(StackPane root) {
        Button safeModeButton = new Button("SAFE STATE");
        safeModeButton.setStyle("""
        -fx-background-color: darkgreen;
        -fx-text-fill: lime;
        """);
        root.setAlignment(safeModeButton,Pos.BOTTOM_CENTER);
        root.setMargin(safeModeButton, new Insets(0,0,0,200));
        safeModeButton.setPrefWidth(100);
        safeModeButton.setPrefHeight(50);
        return safeModeButton;
    }


    public static Button createLaunchButton(StackPane root) {
        Button launchButton = new Button("ENGAGE");
        launchButton.setStyle("""
        -fx-background-color: darkgreen;
        -fx-text-fill: lime;
        -fx-font-size: 16px;
        """);
        launchButton.setPrefWidth(100);
        launchButton.setPrefHeight(100);
        targetInput = new TextField();
        HBox hbox = new HBox();
        hbox.getChildren().addAll(launchButton,targetInput);
        targetInput.setPromptText("ID");
        root.getChildren().add(targetInput);
        root.setAlignment(targetInput, Pos.BOTTOM_RIGHT);
        targetInput.setMaxWidth(200);
        return launchButton;
    }

    public static Button createRadarButton() {
        Button radarButton = new Button("RADIATE");
        radarButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
        radarButton.setPrefHeight(100);
        radarButton.setPrefWidth(100);
        return radarButton;
    }


    public static void createSafeModeArc(Pane radarLayer, Arc sector) {
        safeModeArc = new Arc();
        safeModeArc.setType(ArcType.OPEN);
        safeModeArc.setStartAngle(45);
        safeModeArc.setLength(90);
        safeModeArc.setFill(Color.TRANSPARENT);
        safeModeArc.setStroke(Color.RED);
        safeModeArc.setStrokeWidth(1);
        safeModeArc.centerXProperty().bind(sector.centerXProperty());
        safeModeArc.centerYProperty().bind(sector.centerYProperty());
        safeModeArc.radiusXProperty().bind(sector.radiusXProperty().multiply(0.47));
        safeModeArc.radiusYProperty().bind(sector.radiusYProperty().multiply(0.47));
    }


    public static void setOnActionSafeButton(Button safeButton, Pane radarLayer) {
        safeButton.setOnAction(e -> {
            if (Buttons.safeModeOn.get()) {
                safeButton.setText("SAFE STATE");
                safeButton.setStyle("-fx-background-color: darkgreen; -fx-text-fill: lime;");
                radarLayer.getChildren().add(safeModeArc);
            } else {
                safeButton.setText("SAFE STATE");
                safeButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
                radarLayer.getChildren().remove(safeModeArc);
            }
            Buttons.safeModeOn.set(!Buttons.safeModeOn.get());
        });
    }

    public static void setOnActionRadarButton(Button radarButton) {
        radarButton.setOnAction(e -> {
            radarOn = !radarOn;
            if(!radarOn) {
                radarButton.setText("RADIATE");
                radarButton.setStyle("-fx-background-color: darkgreen; -fx-text-fill: lime;");
                for(AntiRadiationMissile m: Buttons.missiles) {
                    if(m.view != null)
                    m.view.setVisible(false);
                    if(m.line != null)
                    m.line.setVisible(false);
                    if(m.cross != null)
                    m.cross.setVisible(false);
                    if(m.arrow1 != null)
                    m.arrow1.setVisible(false);
                    if(m.arrow2 != null)
                    m.arrow2.setVisible(false);
                    if(m.label != null)
                    m.label.setVisible(false);
                }
                for(Jammer j: Jammer.jammers) {
                    j.view.setVisible(false);
                    j.line.setVisible(false);
                    System.out.println("Size is " + Jammer.jammers.size());
                }
            } else {
                radarButton.setText("RADIATE");
                radarButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
                if (passiveSearchOn.getValue()) {
                    for(AntiRadiationMissile m: Buttons.missiles) {
                        if(m.view != null)
                            m.view.setVisible(false);
                        if(m.line != null)
                            m.line.setVisible(false);
                        if(m.cross != null)
                            m.cross.setVisible(false);
                        if(m.arrow1 != null)
                            m.arrow1.setVisible(false);
                        if(m.arrow2 != null)
                            m.arrow2.setVisible(false);
                        if(m.label != null)
                            m.label.setVisible(false);
                    }
                    for(Jammer j: Jammer.jammers) {
                        j.view.setVisible(true);
                        j.line.setVisible(true);
                    }
                } else {
                    for(AntiRadiationMissile m: Buttons.missiles) {
                        if(m.view != null)
                            m.view.setVisible(true);
                        if(m.line != null)
                            m.line.setVisible(true);
                        if(m.cross != null)
                            m.cross.setVisible(true);
                        if(m.arrow1 != null)
                            m.arrow1.setVisible(true);
                        if(m.arrow2 != null)
                            m.arrow2.setVisible(true);
                        if(m.label != null)
                            m.label.setVisible(true);
                    }
                    for(Jammer j: Jammer.jammers) {
                        j.view.setVisible(true);
                        j.line.setVisible(true);
                    }
                }
            }
        });
    }
    public static AnimationTimer createTimer(Arc sector, Pane missileLayer, Pane interceptorLayer) {
        return new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }

                double dt = (now - last) / 1e9;
                last = now;
                Buttons.missiles.removeIf(m ->
                        m.update(dt,
                                sector.getCenterX(),
                                sector.getCenterY(),
                                Buttons.radarOn, sector, interceptorLayer, missileLayer)
                );

                Buttons.interceptors.removeIf(interceptor -> {
                    boolean hit = interceptor.update(dt);
                    if (hit && interceptor.target != null) {
                        if(!interceptor.isSafeMode){
                            missileLayer.getChildren()
                                    .removeAll(interceptor.target.view,interceptor.target.label, interceptor.target.view2, interceptor.target.line, interceptor.target.arrow1, interceptor.target.arrow2, interceptor.target.line,interceptor.target.interceptPoint, interceptor.target.cross);
                            Buttons.missiles.remove(interceptor.target);
                            Buttons.showExplosion(interceptorLayer, interceptor.x, interceptor.y);
                        }
                        if (!(interceptor.target instanceof ARM2) && !(interceptor.target instanceof ARM4)&& !(interceptor.target instanceof ARM5)&&!(interceptor.target instanceof ARM6)) {
                            missileLayer.getChildren()
                                    .removeAll(interceptor.target.view,interceptor.target.label, interceptor.target.view2, interceptor.target.line, interceptor.target.arrow1, interceptor.target.arrow2, interceptor.target.line,interceptor.target.interceptPoint, interceptor.target.cross);
                            Buttons.missiles.remove(interceptor.target);
                            Buttons.showExplosion(interceptorLayer, interceptor.x, interceptor.y);
                            interceptor.target.interceptPoint.setVisible(false);
                        }
                        interceptorLayer.getChildren().remove(interceptor.view);
                    }
                    return hit;
                });
            }
        };
    }

    public static void setOnActionlaunchRocket(Button launchButton, Pane interceptorLayer, Arc sector, Text rocketsLabel) {
        launchButton.setOnAction(e -> {// радар выключен
            if (Buttons.missiles.isEmpty()) return;       // нет целей
            if (FirstTable.rocketsCount <= 0) return;        // нет ракет
            int targetId = Integer.parseInt(targetInput.getText());
            AntiRadiationMissile target = null;

            for (AntiRadiationMissile m : missiles) {
                if (m.id == targetId) {
                    target = m;
                    break;
                }
            }
            InterceptorMissile im = InterceptorMissile.spawnInterceptor(interceptorLayer, sector, target);
            im.isSafeMode = false;
            Buttons.interceptors.add(im);

            // уменьшаем количество ракет
            FirstTable.rocketsCount--;
            rocketsLabel.setText("Ракет в запасе: " + FirstTable.rocketsCount);

            // обновляем таблицу, если она есть
            FirstTable.radarStats.get(0).value = String.valueOf(FirstTable.rocketsCount);
            FirstTable.radarStats.set(0, FirstTable.radarStats.get(0)); // триггер для TableView
            targetInput.requestFocus();
        });
    }

    public static void showExplosion(Pane layer, double x, double y) {
        ImageView explosion = new ImageView(new Image("E:/диплом/app/armSimulator/src/main/resources/shoot_down.png"));
        explosion.setFitWidth(40);
        explosion.setPreserveRatio(true);

        // Центрируем картинку на месте ракеты
        explosion.setTranslateX(x - explosion.getFitWidth()/2);
        explosion.setTranslateY(y - explosion.getFitHeight()/2);

        layer.getChildren().add(explosion);

       //  Анимация мигания
        Timeline blink = new Timeline(
                new KeyFrame(Duration.seconds(0.2), e -> explosion.setVisible(false)),
                new KeyFrame(Duration.seconds(0.6), e -> explosion.setVisible(true))
        );
        blink.setCycleCount(4); // мигает 2 раза (полсекунды)
        blink.play();
        // Таймер на полное исчезновение через 2.5 секунды
        PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
        delay.setOnFinished(e -> layer.getChildren().remove(explosion));
        delay.play();
    }

    public static Button createTab76Button(StackPane rt) {
        Button tab76 = new Button("TAB 76");
        tab76.setOnAction(e -> {
            Stage stage = new Stage();
            TextField rangeField = new TextField(String.valueOf(Tab76.range));
            Label rangeLabel = new Label("Range     ");
            Button showGraphic = new Button("SHOW");
            showGraphic.setPrefHeight(20);
            showGraphic.setPrefWidth(100);
            HBox rangeBox = new HBox(30, rangeLabel,rangeField,showGraphic);
            showGraphic.setOnAction(actionEvent -> {
                Platform.runLater(() -> Graphics.drawGraphic());
            });
            TextField heightField = new TextField(String.valueOf(Tab76.altitude));
            Label heightLabel = new Label("Altitude  ");
            HBox heightBox = new HBox(30,heightLabel, heightField);


            TextField minSpeedField = new TextField(String.valueOf(Tab76.minSpeed));
            TextField maxSpeedField = new TextField(String.valueOf(Tab76.maxSpeed));
            Label speedLabel = new Label("Speed               ");
            Label minSpeedLabel = new Label("min");
            Label maxSpeedLabel = new Label("max");
            HBox speedBox = new HBox(40,speedLabel, minSpeedLabel, minSpeedField, maxSpeedLabel, maxSpeedField);

            TextField minDiveAngleField = new TextField(String.valueOf(Tab76.minDiveAngle));
            TextField maxDiveAngleField = new TextField(String.valueOf(Tab76.maxDiveAngle));
            Label diveAngleLabel = new Label("Dive angle              ");
            Label minDiveAngleLabel = new Label("min");
            Label maxDiveAngleLabel = new Label("max");
            Button showDiveAngle = new Button("SHOW");
            showDiveAngle.setPrefHeight(20);
            showDiveAngle.setPrefWidth(100);
            showDiveAngle.setOnAction(actionEvent -> {
                Stage stage2 = new Stage();
               new GraphicDiveAngle().start(stage2);
            });

            HBox diveAngleBox = new HBox(40,diveAngleLabel, minDiveAngleLabel, minDiveAngleField, maxDiveAngleLabel, maxDiveAngleField, showDiveAngle);

            TextField approachAngleField = new TextField(String.valueOf(Tab76.approachAngle));
            Label approachAngleLabel = new Label("Approach angle      ");
            Button showApproachAngle = new Button("SHOW");
            showApproachAngle.setPrefWidth(100);
            showApproachAngle.setPrefHeight(20);
            showApproachAngle.setOnAction(actionEvent -> {
                Stage stage2 = new Stage();
                new GraphicApproachAngle().start();
            });
            HBox approachAngleBox = new HBox(30, approachAngleLabel,approachAngleField,showApproachAngle);

            TextField targetCrossSectionField = new TextField(String.valueOf(Tab76.targetCrossSeqtion));
            Label targetCrossSectionLabel = new Label("Target cross section");
            HBox targetCrossSectionBox = new HBox(30, targetCrossSectionLabel,targetCrossSectionField);
            Button selectTab = new Button("SELECT TAB");
            selectTab.setOnAction(ok -> {
                Tab76.range = Double.parseDouble(rangeField.getText());
                Tab76.altitude = Double.parseDouble(heightField.getText());
                Tab76.minSpeed = Double.parseDouble(minSpeedField.getText());
                Tab76.maxSpeed = Double.parseDouble(maxSpeedField.getText());
                Tab76.minDiveAngle = Double.parseDouble(minDiveAngleField.getText());
                Tab76.maxDiveAngle = Double.parseDouble(maxDiveAngleField.getText());
                Tab76.approachAngle = Double.parseDouble(approachAngleField.getText());
                Tab76.targetCrossSeqtion = Double.parseDouble(targetCrossSectionField.getText());
            });

            CheckBox yes = new CheckBox();
            CheckBox no = new CheckBox();
            yes.setSelected(tab1Yes);
            no.setSelected(tab1No);
            yes.setOnAction(actionEvent -> {
                tab1Yes = true;
                tab1No = false;
                yes.setSelected(tab1Yes);
                no.setSelected(tab1No);
            });
            no.setOnAction(actionEvent -> {
                tab1Yes = false;
                tab1No = true;
                yes.setSelected(tab1Yes);
                no.setSelected(tab1No);
            });
            Label armClass = new Label("AUTHORISATION ARM CLASS");
            armClass.setFont(Font.font(18));
            Label yesLabel = new Label("YES");
            yesLabel.setFont(Font.font(18));
            Label noLabel = new Label("NO");
            noLabel.setFont(Font.font(18));
            HBox tab1 = new HBox(40,armClass, yesLabel, yes, noLabel, no);

            stage.setTitle("TAB 76");
            VBox root = new VBox(rangeBox, heightBox, speedBox, diveAngleBox, approachAngleBox, targetCrossSectionBox, selectTab,tab1);
            stage.setScene(new Scene(root, 650, 250));
            stage.show();
            });
        rt.setAlignment(tab76, Pos.TOP_LEFT);
        tab76.setPrefHeight(35);
        tab76.setPrefWidth(140);
        return tab76;
    }

    public static Button createTrackAmplificationButton(StackPane rt) {
        Button trackAmplificationButton = new Button("TRACK AMPLIFICATION DATA");
        trackAmplificationButton.setOnAction(e -> {
            tableView = new TableView<>();
            tableView.setItems(missiles);
            TableColumn<AntiRadiationMissile, Integer> id = new TableColumn<>("id");
            TableColumn<AntiRadiationMissile, Integer> rangeInKm = new TableColumn<>("Range: km");
            TableColumn<AntiRadiationMissile, Integer> heightInKm = new TableColumn<>("Height: m");
            TableColumn<AntiRadiationMissile, Double> azimuth = new TableColumn<>("Azimuth: dgr");
            TableColumn<AntiRadiationMissile, Double> speed = new TableColumn<>("Speed: m/s");
            TableColumn<AntiRadiationMissile, Double> diveAngle = new TableColumn<>("Dive angle: dgr");
            TableColumn<AntiRadiationMissile, Double> approachAngle = new TableColumn<>("Approach angle: dgr");
            TableColumn<AntiRadiationMissile, Double> targetCrossSection = new TableColumn<>("Target cross section: sqm");
            TableColumn<AntiRadiationMissile, Double> targetType = new TableColumn<>("Target type");


            id.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Integer>("id"));
            rangeInKm.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Integer>("distanceInKm"));
            heightInKm.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Integer>("height"));
            azimuth.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Double>("azimuth"));
            speed.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Double>("speedKmPerSecond"));
            diveAngle.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Double>("diveAngle"));
            approachAngle.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Double>("approachAngle"));
            targetCrossSection.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Double>("targetCrossSection"));
            targetType.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Double>("targetType"));

            tableView.getColumns().addAll(id, rangeInKm, heightInKm,azimuth,speed,diveAngle,approachAngle,targetCrossSection,targetType);
            Stage stage = new Stage();
            stage.setTitle("Track");
            VBox root = new VBox(tableView);
            stage.setScene(new Scene(root, 900, 400));
            stage.show();
        });
        rt.setAlignment(trackAmplificationButton, Pos.TOP_LEFT);
        StackPane.setMargin(trackAmplificationButton, new Insets(35,0,0,0));
        trackAmplificationButton.setPrefHeight(35);
        trackAmplificationButton.setPrefWidth(200);
        return trackAmplificationButton;
    }
    public static Button createPassiveSearchButton(StackPane root) {
        Button passiveSearch = new Button("PASSIVE SEARCH");
        passiveSearch.setOnAction(event -> {
            Buttons.passiveSearchOn.set(!Buttons.passiveSearchOn.get());
            if(!radarOn) {
                for(AntiRadiationMissile m: Buttons.missiles) {
                    if(m.view != null)
                        m.view.setVisible(false);
                    if(m.line != null)
                        m.line.setVisible(false);
                    if(m.cross != null)
                        m.cross.setVisible(false);
                    if(m.arrow1 != null)
                        m.arrow1.setVisible(false);
                    if(m.arrow2 != null)
                        m.arrow2.setVisible(false);
                    if(m.label != null)
                        m.label.setVisible(false);
                }
                for(Jammer j: Jammer.jammers) {
                    j.view.setVisible(false);
                    j.line.setVisible(false);
                    System.out.println("Size is " + Jammer.jammers.size());
                }
            } else {
                if (passiveSearchOn.getValue()) {
                    passiveSearch.setText("PASSIVE SEARCH");
                    passiveSearch.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                    for(AntiRadiationMissile m: Buttons.missiles) {
                        if(m.view != null)
                            m.view.setVisible(false);
                        if(m.line != null)
                            m.line.setVisible(false);
                        if(m.cross != null)
                            m.cross.setVisible(false);
                        if(m.arrow1 != null)
                            m.arrow1.setVisible(false);
                        if(m.arrow2 != null)
                            m.arrow2.setVisible(false);
                        if(m.label != null)
                            m.label.setVisible(false);
                    }
                    for(Jammer j: Jammer.jammers) {
                        j.view.setVisible(true);
                        j.line.setVisible(true);
                    }
                } else {
                    passiveSearch.setText("PASSIVE SEARCH");
                    passiveSearch.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
                    for(AntiRadiationMissile m: Buttons.missiles) {
                        if(m.view != null)
                            m.view.setVisible(true);
                        if(m.line != null)
                            m.line.setVisible(true);
                        if(m.cross != null)
                            m.cross.setVisible(true);
                        if(m.arrow1 != null)
                            m.arrow1.setVisible(true);
                        if(m.arrow2 != null)
                            m.arrow2.setVisible(true);
                        if(m.label != null)
                            m.label.setVisible(true);
                    }
                    for(Jammer j: Jammer.jammers) {
                        j.view.setVisible(true);
                        j.line.setVisible(true);
                    }
                }
            }

        });
        passiveSearch.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
        passiveSearch.setPrefWidth(200);
        passiveSearch.setPrefHeight(50);
        root.setAlignment(passiveSearch, Pos.BOTTOM_RIGHT);
        root.setMargin(passiveSearch, new Insets(0,0,120,0) );
        targetInput.setMaxWidth(200);
        return passiveSearch;
    }
    public static Button createCarmMode(StackPane root) {
        Button button = new Button();
        button.setPrefWidth(100);
        button.setPrefHeight(100);
        button.setText("CARM MODE");
        button.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
        button.setOnAction(actionEvent -> {
            isCarmModeOn = !isCarmModeOn;
            if (Buttons.isCarmModeOn) {
                button.setText("CARM MODE");
                button.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");

            } else {
                button.setText("CARM MODE");
                button.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
            }
        });
        root.setAlignment(button, Pos.BOTTOM_RIGHT);
        root.setMargin(button, new Insets(0,0,180,0) );
        return button;
    }

}
