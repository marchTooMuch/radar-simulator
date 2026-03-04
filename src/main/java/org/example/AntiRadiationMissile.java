package org.example;

import javafx.beans.property.BooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;

class AntiRadiationMissile {

    double x, y;
    double speed = 10;
    ImageView view;

    double lastDx = 0;
    double lastDy = 0;

    AntiRadiationMissile(double x, double y, ImageView view) {
        this.x = x;
        this.y = y;
        this.view = view;
        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    boolean update(double dt, double targetX, double targetY, BooleanProperty radarOn) {

        double dx, dy;

        if (radarOn.get()) {  // радар включён
            dx = targetX - x;
            dy = targetY - y;

            double dist = Math.sqrt(dx*dx + dy*dy);
            if (dist < 10) return true; // ракета достигла цели

            dx /= dist;
            dy /= dist;

            // сохраняем последний вектор для инерции
            lastDx = dx;
            lastDy = dy;
        } else { // радар выключен — летим по последнему вектору
            dx = lastDx;
            dy = lastDy;
        }

        x += dx * speed * dt;
        y += dy * speed * dt;

        view.setTranslateX(x);
        view.setTranslateY(y);
        view.setRotate(Math.toDegrees(Math.atan2(dy, dx)));

        return false;
    }

    public static AntiRadiationMissile spawnMissile(Pane missileLayer, Arc sector) {

        double angle = sector.getStartAngle()
                + Math.random() * sector.getLength();

        double x = sector.getCenterX()
                + sector.getRadiusX() * Math.cos(Math.toRadians(angle));

        double y = sector.getCenterY()
                - sector.getRadiusY() * Math.sin(Math.toRadians(angle));

        Image img = new Image("E:/диплом/app/armSimulator/src/main/resources/arm.png"
        );

        ImageView view = new ImageView(img);
        view.setFitWidth(28);
        view.setPreserveRatio(true);

        AntiRadiationMissile m = new AntiRadiationMissile(x, y, view);
        missileLayer.getChildren().add(view);
        return m;
    }
}
