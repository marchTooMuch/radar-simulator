package org.example;

import javafx.beans.property.BooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


class AntiRadiationMissile {
    public static int count = 1;
    int id;
    double x, y;
    double speed = 10;
    ImageView view;
    Text label;
//    double angle;    // угол в градусах
//    double fraction;
    double distance;
    double lastDx = 0;
    double lastDy = 0;
    boolean isEngaged = false;

    AntiRadiationMissile(double x, double y, ImageView view) {
        this.x = x;
        this.y = y;
        this.view = view;
        view.setTranslateX(x);
        view.setTranslateY(y);
        id = count++;
        label = new Text(String.valueOf(id));
        label.setFill(Color.RED);
        label.setFont(Font.font(14));
//        fraction = 1;

    }

    boolean update(double dt, double targetX, double targetY, BooleanProperty radarOn, Arc sector, Pane interceptorLayer) {
        distance = AntiRadiationMissile.getDistance(sector, x, y);
        if(Buttons.safeModeOn.get() == false && distance <= 300 && isEngaged == false && FirstTable.rocketsCount > 0) {
            isEngaged = true;
            AntiRadiationMissile.launchRocket(interceptorLayer, sector, id);
        }
        double dx, dy;
        if (radarOn.get()) {
            label.setVisible(true);// радар включён
            dx = (targetX - 15) - x;
            dy = (targetY - 20) - y;

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
            label.setVisible(false);
        }
//        fraction -= speed * dt / sector.getRadiusY();
//        if (fraction <= 0.5) return true;
//        x = sector.getCenterX() + sector.getRadiusX() * fraction * Math.cos(Math.toRadians(angle));
//        y = sector.getCenterY() - sector.getRadiusY() * fraction * Math.sin(Math.toRadians(angle));
        x += dx * speed * dt;
        y += dy * speed * dt;
        label.setTranslateX(x + 20); // 10 px справа от ракеты
        label.setTranslateY(y + 20);
        view.setTranslateX(x);
        view.setTranslateY(y);
        view.setRotate(Math.toDegrees(Math.atan2(dy, dx)));
        return false;
    }

    public static AntiRadiationMissile spawnMissile(Pane missileLayer, Arc sector) {

        double angle = sector.getStartAngle() + 10
                + Math.random() * (sector.getLength() - 10);

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
//        m.angle = angle;
        missileLayer.getChildren().add(view);
        //создаем текст номера ракеты
        Text label = m.label;
        label.setFill(Color.RED);
        label.setFont(Font.font(30));
        label.setTranslateX(m.x);
        label.setTranslateY(m.y);
        missileLayer.getChildren().add(label);
        return m;
    }
    public static double getDistance(Arc sector, double x, double y) {
        x = x - sector.getCenterX();
        y = y - sector.getCenterY();
        double distance = Math.sqrt(x*x + y*y);
        return distance;
    }
    public static void launchRocket(Pane interceptorLayer, Arc sector, int targetId) {
        System.out.println(targetId);
        if (!Buttons.radarOn.get()) return;          // радар выключен
        if (Buttons.missiles.isEmpty()) return;       // нет целей
        if (FirstTable.rocketsCount <= 0) return;        // нет ракет
        //int targetId = Integer.parseInt(Buttons.targetInput.getText());
        AntiRadiationMissile target = null;

        for (AntiRadiationMissile m : Buttons.missiles) {
            if (m.id == targetId) {
                target = m;
                break;
            }
        }
        Buttons.interceptors.add(InterceptorMissile.spawnInterceptor(interceptorLayer, sector, target));
        // уменьшаем количество ракет
        FirstTable.rocketsCount--;
        Labels.rocketsLabel.setText("Ракет в запасе: " + FirstTable.rocketsCount);
        // обновляем таблицу, если она есть
        FirstTable.radarStats.get(0).value = String.valueOf(FirstTable.rocketsCount);
        FirstTable.radarStats.set(0, FirstTable.radarStats.get(0)); // триггер для TableView
    }

}
