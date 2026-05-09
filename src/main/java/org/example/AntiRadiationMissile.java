package org.example;

import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class AntiRadiationMissile {
    int maxHeight = 0 ;
    public static int count = 1;
    int id;
    double x, y;
    double speed = 1 ;
    ImageView view;
    ImageView view2;
    ImageView plane;
    Text label;
    IntegerProperty distanceInKm = new SimpleIntegerProperty();
//    double angle;    // угол в градусах
//    double fraction;
    DoubleProperty distance = new SimpleDoubleProperty();
    IntegerProperty height = new SimpleIntegerProperty();
    DoubleProperty azimuth = new SimpleDoubleProperty();
    DoubleProperty speedKmPerSecond = new SimpleDoubleProperty();
    DoubleProperty diveAngle = new SimpleDoubleProperty();
    DoubleProperty approachAngle = new SimpleDoubleProperty();
    DoubleProperty targetCrossSection = new SimpleDoubleProperty();
    double lastDx = 0;
    double lastDy = 0;
    boolean isEngaged = false;
    Line line;
    Line arrow1;
    Line arrow2;
    Line cross;


    AntiRadiationMissile(double x, double y, ImageView view1, ImageView view2, int height, double azimuth) {
        this.x = x;
        this.y = y;
        this.view = view1;
        this.view2 = view2;
        view.setTranslateX(x);
        view.setTranslateY(y);
        id = count++;
        label = new Text(String.valueOf(id));
        label.setFill(Color.RED);
        label.setFont(Font.font(14));
        this.azimuth.setValue(azimuth);
        approachAngle.setValue(0);
//       fraction = 1;
    }

    boolean update(double dt, double targetX, double targetY, BooleanProperty radarOn, Arc sector, Pane interceptorLayer, Pane missileLayer) {

        System.out.println("Hello from anti!");
        if(distanceInKm.getValue() <= Tab76.range ) {
            view.setVisible(false);
            view = view2;
            view.setVisible(true);
        }
       // distanceInKm.set((int)((600 * distance.get()) / 140));
        distanceInKm.set((int)( (distance.get()) / 4 ));
        if(Buttons.safeModeOn.get() == false && distance.get() <= 300 && isEngaged == false && FirstTable.rocketsCount > 0) {
            isEngaged = true;
            AntiRadiationMissile.launchRocket(interceptorLayer, sector, id);
        }
        double dx, dy;
        if (radarOn.get()) {
            label.setVisible(true);// радар включён
            dx = (targetX - 15) - x;
            dy = (targetY - 20) - y;
            distance.set(Math.sqrt(dx*dx + dy*dy));
            //if(id == 1)
            //System.out.println("ID:" + id + " X:" + x + " Y:" + y + " Distance(dx*dx + dy*dy):" + distance.get() + " targetX:" + (targetX - 15) + " targetY:" + (targetY - 20));
            if (distance.get() < 10) {
                distanceInKm.set(0);return true;}; // ракета достигла цели

            dx /= distance.get();
            dy /= distance.get();

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
                + (sector.getRadiusX() - 15) * Math.cos(Math.toRadians(angle));

        double y = sector.getCenterY()
                - (sector.getRadiusY() - 15)* Math.sin(Math.toRadians(angle) );

        Image img = new Image("E:/диплом/app/armSimulator/src/main/resources/arm.png"
        );
        Image img2 = new Image("E:/диплом/app/armSimulator/src/main/resources/arm_defined.png"
        );
        Image img3 = new Image("E:/диплом/app/armSimulator/src/main/resources/plane.png"
        );
        ImageView view = new ImageView(img);
        ImageView view2 = new ImageView(img2);
        ImageView view3 = new ImageView(img3);
        view.setFitWidth(28);
        view.setPreserveRatio(true);
        view2.setFitWidth(28);
        view2.setPreserveRatio(true);
        AntiRadiationMissile m = new AntiRadiationMissile(x, y, view, view2,25000, angle);
//        m.angle = angle;
        missileLayer.getChildren().addAll(view,view2);
        view2.setVisible(false);
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

    public Double getDistance() {
        return distance.get();
    }
    public Integer getDistanceInKm() {
        return distanceInKm.get();
    }
    public Integer getHeight() {
        return height.get();
    }
    public Double getAzimuth() {
        return azimuth.get();
    }
    public Double getSpeedKmPerSecond() {
        return speedKmPerSecond.get();
    }
    public Double getDiveAngle() {
        return diveAngle.get();
    }
    public Double getApproachAngle() {
        return approachAngle.get();
    }
    public Double getTargetCrossSection() {
        return targetCrossSection.get();
    }

    public void setDistance(double value) {
        distance.set(value);
    }
    public void setDistanceInKm(int value) {
        distanceInKm.set(value);
    }
    public void setHeight(int value) {
        height.set(value);
    }
    public void setAzimuth(double value) {
        azimuth.set(value);
    }
    public void setSpeedKmPerSecond(double value) {
        speedKmPerSecond.set(value);
    }
    public void setDiveAngle(double value) {
        diveAngle.set(value);
    }
    public void setApproachAngle(double value) {
        approachAngle.set(value);
    }
    public void setTargetCrossSection(double value) {
        targetCrossSection.set(value);
    }


    public DoubleProperty distanceProperty() {
        return distance;
    }
    public IntegerProperty distanceInKmProperty() {
        return distanceInKm;
    }
    public IntegerProperty heightProperty() {
        return height;
    }
    public DoubleProperty azimuthProperty() {
        return azimuth;
    }
    public DoubleProperty speedKmPerSecondProperty() {
        return speedKmPerSecond;
    }
    public DoubleProperty diveAngleProperty() {
        return diveAngle;
    }
    public DoubleProperty approachAngleProperty() {
        return approachAngle;
    }
    public DoubleProperty targetCrossSectionProperty() {
        return targetCrossSection;
    }

    public Integer getId() {
        return id;
    }

}
