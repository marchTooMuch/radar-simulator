package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GraphicDiveAngle {
    static double angle1 = 30;
    static double angle2 = 50;

    private final double minDiveAngle = Tab76.minDiveAngle;
    private final double maxDiveAngle = Tab76.maxDiveAngle;

    public void start(Stage stage) {

        Pane root = new Pane();

        double width = 260;
        double height = 200;

        // Начало координат
        double centerX = 20;
        double centerY = height - 20;

        double axisLength = 180;
        double lineLength = 150;

        // Ось X
        Line xAxis = new Line(
                centerX,
                centerY,
                centerX + axisLength,
                centerY
        );

        // Ось Y
        Line yAxis = new Line(
                centerX,
                centerY,
                centerX,
                centerY - axisLength
        );

        xAxis.setStrokeWidth(2);
        yAxis.setStrokeWidth(2);

        // MIN DIVE ANGLE
        Line minLine = createAngleLine(
                centerX,
                centerY,
                minDiveAngle,
                lineLength
        );

        minLine.setStroke(Color.RED);
        minLine.setStrokeWidth(3);

        // MAX DIVE ANGLE
        Line maxLine = createAngleLine(
                centerX,
                centerY,
                maxDiveAngle,
                lineLength
        );

        maxLine.setStroke(Color.BLUE);
        maxLine.setStrokeWidth(3);

        // Дуга угла MIN
        Arc minArc = createAngleArc(
                centerX,
                centerY,
                35,
                minDiveAngle
        );

        minArc.setStroke(Color.RED);

        // Дуга угла MAX
        Arc maxArc = createAngleArc(
                centerX,
                centerY,
                55,
                maxDiveAngle
        );

        maxArc.setStroke(Color.BLUE);

        // Подпись MIN
        Text minText = new Text(
                centerX + 95,
                centerY - 35,
                "min dive angle"
        );

        minText.setFill(Color.RED);
        minText.setFont(Font.font(12));

        // Подпись MAX
        Text maxText = new Text(
                centerX + 20,
                centerY - 130,
                "max dive angle"
        );

        maxText.setFill(Color.BLUE);
        maxText.setFont(Font.font(12));

        root.getChildren().addAll(
                xAxis,
                yAxis,

                minArc,
                maxArc,

                minLine,
                maxLine,

                minText,
                maxText
        );

        Scene scene = new Scene(root, width, height);

        stage.setTitle("Dive Angles");
        stage.setScene(scene);
        stage.show();
    }

    private Line createAngleLine(
            double startX,
            double startY,
            double angleDegrees,
            double length
    ) {

        double radians = Math.toRadians(angleDegrees);

        double endX = startX + length * Math.cos(radians);

        double endY = startY - length * Math.sin(radians);

        return new Line(startX, startY, endX, endY);
    }

    private Arc createAngleArc(
            double centerX,
            double centerY,
            double radius,
            double angle
    ) {

        Arc arc = new Arc();

        arc.setCenterX(centerX);
        arc.setCenterY(centerY);

        arc.setRadiusX(radius);
        arc.setRadiusY(radius);

        // Начало от оси X
        arc.setStartAngle(0);

        // JavaFX рисует против часовой
        arc.setLength(angle);

        arc.setFill(Color.TRANSPARENT);
        arc.setStrokeWidth(2);

        arc.setType(ArcType.OPEN);

        return arc;
    }

}

