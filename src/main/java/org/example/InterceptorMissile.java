package org.example;

import javafx.scene.image.ImageView;

public class InterceptorMissile {
    double x, y;
    double speed = 20;
    ImageView view;
    AntiRadiationMissile target;

    double lastDx = 0;
    double lastDy = 0;


    InterceptorMissile(double x, double y,
                       ImageView view,
                       AntiRadiationMissile target) {
        this.x = x;
        this.y = y;
        this.view = view;
        this.target = target;

        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    boolean update(double dt) {
        if (target == null) return true;

        double dx = target.x - x;
        double dy = target.y - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < 12) {
            return true; // перехват
        }

        dx /= dist;
        dy /= dist;

        x += dx * speed * dt;
        y += dy * speed * dt;

        view.setTranslateX(x);
        view.setTranslateY(y);
        view.setRotate(Math.toDegrees(Math.atan2(dy, dx)));

        return false;
    }
}
