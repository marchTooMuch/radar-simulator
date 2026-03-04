package org.example;

import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class Sector {
    private static final double SECTOR_ANGLE = 60;

    public static Arc create(Scene scene) {
        Arc sector = new Arc();
        sector = new Arc();
        sector.setType(ArcType.ROUND);
        sector.setStartAngle(90 - SECTOR_ANGLE / 2);
        sector.setLength(SECTOR_ANGLE);
        sector.setFill(Color.rgb(0, 255, 0, 0.25));
        sector.setStroke(Color.GREEN);
        sector.setStrokeWidth(2);

        sector.centerXProperty().bind(scene.widthProperty().divide(2));
        sector.centerYProperty().bind(scene.heightProperty().subtract(50));

        // Радиусы
        sector.radiusXProperty().bind(scene.widthProperty().multiply(0.48));
        sector.radiusYProperty().bind(scene.heightProperty().multiply(0.9));
        sector.opacityProperty().bind(
                Bindings.when(Buttons.radarOn)
                        .then(0.25)
                        .otherwise(0.05)
        );
        return sector;
    }

}
