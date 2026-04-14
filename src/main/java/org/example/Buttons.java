package org.example;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.animation.AnimationTimer;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
                                Buttons.radarOn, sector, interceptorLayer)
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
}
