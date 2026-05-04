package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;


public class Object {
    public static Polygon getPolygon() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(250.0, 1000.0,
                                        280.0, 1020.0,
                                        310.0, 960.0,
                                        250.0, 950.0);
        polygon.setStroke(Color.WHITE);
        polygon.setFill(Color.TRANSPARENT);
        polygon.setStrokeWidth(3);
        return polygon;
    }
}
