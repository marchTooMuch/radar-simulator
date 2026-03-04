package org.example;

import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RadarLayer {
    private static final int SECTOR_ANGLE = 60;
    private static final int LINES_COUNT = 5;
    private static final int MAX_DISTANCE_KM = 140;

    Pane radarLayer;
    Arc sector;
    Pane root;

    RadarLayer(Scene scene, Arc sector) {
        radarLayer = new Pane();
        this.sector = sector;
        addLines();
    }

    private void addLines() {

        radarLayer.getChildren().add(sector);
        double startAngle = 90 - SECTOR_ANGLE / 2;

        for (int i = 1; i <= LINES_COUNT; i++) {

            double fraction = i / (double) LINES_COUNT;
            double distance = MAX_DISTANCE_KM * fraction;

            // ---- дуга расстояния
            Arc rangeArc = new Arc();
            rangeArc.setType(ArcType.OPEN);
            rangeArc.setStartAngle(startAngle);
            rangeArc.setLength(SECTOR_ANGLE);
            rangeArc.setFill(Color.TRANSPARENT);
            rangeArc.setStroke(Color.GREEN);
            rangeArc.setStrokeWidth(1);

            rangeArc.centerXProperty().bind(sector.centerXProperty());
            rangeArc.centerYProperty().bind(sector.centerYProperty());
            rangeArc.radiusXProperty().bind(sector.radiusXProperty().multiply(fraction));
            rangeArc.radiusYProperty().bind(sector.radiusYProperty().multiply(fraction));

            radarLayer.getChildren().add(rangeArc);

            // ---- подпись расстояния
            Text label = new Text(String.format("%.0f km", distance));
            label.setFont(Font.font(14));
            label.setFill(Color.GREEN);

            // точка начала дуги (ГЕОМЕТРИЧЕСКИ ВЕРНО)
            label.xProperty().bind(
                    Bindings.createDoubleBinding(
                            () -> sector.getCenterX()
                                    + rangeArc.getRadiusX()
                                    * Math.cos(Math.toRadians(startAngle))
                                    + 5, // отступ от линии
                            sector.centerXProperty(),
                            rangeArc.radiusXProperty()
                    )
            );

            label.yProperty().bind(
                    Bindings.createDoubleBinding(
                            () -> sector.getCenterY()
                                    - rangeArc.getRadiusY()
                                    * Math.sin(Math.toRadians(startAngle)),
                            sector.centerYProperty(),
                            rangeArc.radiusYProperty()
                    )
            );

            radarLayer.getChildren().add(label);
        }
    }

}
