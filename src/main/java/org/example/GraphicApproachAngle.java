package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GraphicApproachAngle {

    // Угол приближения ракеты
    private final double approachAngle = Tab76.approachAngle;

    public void start() {
        Stage stage = new Stage();
        Pane root = new Pane();

        double width = 500;
        double height = 500;

        // Центр радара
        double centerX = width / 2;
        double centerY = height - 40;

        // Радиус сектора
        double radarRadius = 320;

        // Сектор радара
        Arc radarSector = new Arc(
                centerX,
                centerY,
                radarRadius,
                radarRadius,
                30,
                120
        );

        radarSector.setType(ArcType.ROUND);
        radarSector.setFill(Color.rgb(0, 255, 0, 0.10));
        radarSector.setStroke(Color.LIMEGREEN);
        radarSector.setStrokeWidth(2);

        // Центральная ось радара
        Line centerLine = new Line(
                centerX,
                centerY,
                centerX,
                centerY - radarRadius
        );

        centerLine.setStroke(Color.DARKGREEN);

        /*
            РАКЕТА ВСЕГДА ПО ЦЕНТРУ
        */
        double rocketX = centerX;
        double rocketY = centerY - 180;

        // Овал ракеты
        Ellipse rocket = new Ellipse(
                rocketX,
                rocketY,
                45,
                14
        );

        rocket.setFill(Color.LIMEGREEN);
        rocket.setStroke(Color.GREENYELLOW);

        /*
            Поворот ракеты:

            0° approach  -> смотрит вниз на радар
            180°         -> смотрит вверх от радара

            Поэтому:
        */
        rocket.setRotate(90 - approachAngle);

        root.getChildren().addAll(
                radarSector,
                centerLine,
                rocket
        );

        Scene scene = new Scene(root, width, height, Color.BLACK);

        stage.setTitle("Radar Approach Angle");
        stage.setScene(scene);
        stage.show();
    }

}
