package org.example;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;

import java.util.Objects;

public class InterceptorMissile {
    double x, y;
    static double speed = 10;
    ImageView view;
    AntiRadiationMissile target;
    boolean isSafeMode = true;
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
        if (target == null) {
            target.interceptPoint.setVisible(false);
            return true;}

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

    public  static InterceptorMissile spawnInterceptor(
            Pane interceptorLayer,
            Arc sector,
            AntiRadiationMissile target) {

        double x = sector.getCenterX() - 15;
        double y = sector.getCenterY() - 20;

        Image img = new Image("file:E:/диплом/app/armSimulator/src/main/resources/rocket.png"
        );

        ImageView view = new ImageView(img);
        view.setFitWidth(32);
        view.setPreserveRatio(true);

        InterceptorMissile m =
                new InterceptorMissile(x, y, view, target);
        m.target.interceptor = m;
        interceptorLayer.getChildren().add(view);
        return m;
    }

}
