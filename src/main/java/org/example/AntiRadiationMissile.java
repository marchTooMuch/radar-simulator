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
}
