package org.example;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.animation.AnimationTimer;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class Buttons {
    public static BooleanProperty radarOn = new SimpleBooleanProperty(true);
    public static BooleanProperty safeModeOn = new SimpleBooleanProperty(true);
    public static List<AntiRadiationMissile> missiles = new ArrayList<>();
    public static List<InterceptorMissile> interceptors = new ArrayList<>();
    public static TextField targetInput;
    public static Arc safeModeArc;
    public static TableView<AntiRadiationMissile> tableView;
    public static Button createSafeModeButton(StackPane root) {
        Button safeModeButton = new Button("SAFE MODE");
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
        radarButton.setStyle("""
        -fx-background-color: darkgreen;
        -fx-text-fill: lime;
        """);
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
                safeButton.setText("SAFE MODE");
                safeButton.setStyle("-fx-background-color: darkgreen; -fx-text-fill: lime;");
                radarLayer.getChildren().add(safeModeArc);
            } else {
                safeButton.setText("SAFE MODE");
                safeButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
                radarLayer.getChildren().remove(safeModeArc);
            }
            Buttons.safeModeOn.set(!Buttons.safeModeOn.get());
        });

    }

    public static void radarOnAddListener() {
        Buttons.radarOn.addListener((obs, oldVal, newVal) -> {

            // скрываем / показываем ПРР
            for (AntiRadiationMissile m : Buttons.missiles) {
                m.view.setVisible(newVal);
            }

            // скрываем / показываем перехватчики
            for (InterceptorMissile i : Buttons.interceptors) {
                i.view.setVisible(newVal);
            }
        });
    }

    public static void setOnActionRadarButton(Button radarButton) {
        radarButton.setOnAction(e -> {
            Buttons.radarOn.set(!Buttons.radarOn.get());

            if (Buttons.radarOn.get()) {
                radarButton.setText("RADIATE");
                radarButton.setStyle("-fx-background-color: darkgreen; -fx-text-fill: lime;");
            } else {
                radarButton.setText("RADIATE");
                radarButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
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
                        missileLayer.getChildren()
                                .removeAll(interceptor.target.view,interceptor.target.label);
                        Buttons.missiles.remove(interceptor.target);
                        interceptorLayer.getChildren().remove(interceptor.view);
                        Buttons.showExplosion(interceptorLayer, interceptor.x, interceptor.y);
                    }
                    return hit;
                });
            }
        };
    }

    public static void setOnActionlaunchRocket(Button launchButton, Pane interceptorLayer, Arc sector, Text rocketsLabel) {
        launchButton.setOnAction(e -> {
            if (!Buttons.radarOn.get()) return;          // радар выключен
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
            Buttons.interceptors.add(InterceptorMissile.spawnInterceptor(interceptorLayer, sector, target));

            // уменьшаем количество ракет
            FirstTable.rocketsCount--;
            rocketsLabel.setText("Ракет в запасе: " + FirstTable.rocketsCount);

            // обновляем таблицу, если она есть
            FirstTable.radarStats.get(0).value = String.valueOf(FirstTable.rocketsCount);
            FirstTable.radarStats.set(0, FirstTable.radarStats.get(0)); // триггер для TableView

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
//            Stage stage = new Stage();
//            VBox root = new VBox(Tab76.tab76);
//            Scene scene = new Scene(root,300,200);
//            stage.setScene(scene);
//            stage.setTitle("TAB 76");
//            stage.show();
            Stage stage = new Stage();
            TextField rangeField = new TextField();
            Label rangeLabel = new Label("Range");
            HBox rangeBox = new HBox(10, rangeLabel,rangeField);
            Button selectTab = new Button("SELECT TAB");
            selectTab.setOnAction(ok -> {
                Tab76.range = Integer.parseInt(rangeField.getText());
            });
            stage.setTitle("TAB 76");
            VBox root = new VBox(rangeBox,selectTab);
            stage.setScene(new Scene(root, 250, 200));
            stage.show();
            });
        rt.setAlignment(tab76, Pos.TOP_LEFT);
        tab76.setPrefHeight(35);
        tab76.setPrefWidth(140);
        return tab76;
    }

    public static Button createTrackAmplificationButton(StackPane rt) {
        Button trackAmplificationButton = new Button("Track Amplification");
        trackAmplificationButton.setOnAction(e -> {
            ObservableList<AntiRadiationMissile> list = FXCollections.observableArrayList(missiles);
            tableView = new TableView<>();
            TableColumn<AntiRadiationMissile, Integer> id = new TableColumn<>("id");
            TableColumn<AntiRadiationMissile, Integer> rangeInKm = new TableColumn<>("range km");
            TableColumn<AntiRadiationMissile, Integer> heightInKm = new TableColumn<>("height m");


            id.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Integer>("id"));
            rangeInKm.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Integer>("distanceInKm"));
            heightInKm.setCellValueFactory(new PropertyValueFactory<AntiRadiationMissile, Integer>("height"));

            tableView.getColumns().addAll(id, rangeInKm, heightInKm);
            tableView.setItems(list);
            Stage stage = new Stage();
            VBox root = new VBox(tableView);
            stage.setScene(new Scene(root, 400, 400));
            stage.show();
            for (AntiRadiationMissile m : list) {
                System.out.println(m.distance);
            }
        });
        rt.setAlignment(trackAmplificationButton, Pos.TOP_LEFT);
        StackPane.setMargin(trackAmplificationButton, new Insets(35,0,0,0));
        trackAmplificationButton.setPrefHeight(35);
        trackAmplificationButton.setPrefWidth(140);
        return trackAmplificationButton;
    }

}
