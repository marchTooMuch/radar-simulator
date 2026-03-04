package org.example;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class Buttons {
    public static BooleanProperty radarOn = new SimpleBooleanProperty(true);
    public static List<AntiRadiationMissile> missiles = new ArrayList<>();
    public static List<InterceptorMissile> interceptors = new ArrayList<>();
    public static Button createLaunchButton() {
        Button launchButton = new Button("Engage");
        launchButton.setStyle("""
        -fx-background-color: darkgreen;
        -fx-text-fill: lime;
        -fx-font-size: 16px;
        """);
        launchButton.setLayoutX(20);
        launchButton.setLayoutY(20);
        return launchButton;
    }

    public static Button createRadarButton() {
        Button radarButton = new Button("РАДАР: ВКЛ");
        radarButton.setStyle("""
        -fx-background-color: darkgreen;
        -fx-text-fill: lime;
        -fx-font-size: 14px;
        """);

        radarButton.setLayoutX(20);
        radarButton.setLayoutY(60);
        return radarButton;
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
    public static AnimationTimer createTimer(Arc sector, Pane missileLayer) {
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
                                Buttons.radarOn)
                );

                Buttons.interceptors.removeIf(interceptor -> {
                    boolean hit = interceptor.update(dt);

                    if (hit && interceptor.target != null) {
                        missileLayer.getChildren()
                                .remove(interceptor.target.view);
                        Buttons.missiles.remove(interceptor.target);
                    }

                    return hit;
                });

            }
        };
    }

    public static void setOnActionLaunchButton(Button launchButton, Pane interceptorLayer, Arc sector, Text rocketsLabel) {
        launchButton.setOnAction(e -> {
            if (!Buttons.radarOn.get()) return;          // радар выключен
            if (Buttons.missiles.isEmpty()) return;       // нет целей
            if (FirstTable.rocketsCount <= 0) return;        // нет ракет

            AntiRadiationMissile target = Buttons.missiles.get(0);
            Buttons.interceptors.add(InterceptorMissile.spawnInterceptor(interceptorLayer, sector, target));

            // уменьшаем количество ракет
            FirstTable.rocketsCount--;
            rocketsLabel.setText("Ракет в запасе: " + FirstTable.rocketsCount);

            // обновляем таблицу, если она есть
            FirstTable.radarStats.get(0).value = String.valueOf(FirstTable.rocketsCount);
            FirstTable.radarStats.set(0, FirstTable.radarStats.get(0)); // триггер для TableView
        });
    }

    public static void setOnActionRadarButton(Button radarButton) {
        radarButton.setOnAction(e -> {
            Buttons.radarOn.set(!Buttons.radarOn.get());

            if (Buttons.radarOn.get()) {
                radarButton.setText("РАДАР: ВКЛ");
                radarButton.setStyle("-fx-background-color: darkgreen; -fx-text-fill: lime;");
            } else {
                radarButton.setText("РАДАР: ВЫКЛ");
                radarButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
            }
        });
    }
}
