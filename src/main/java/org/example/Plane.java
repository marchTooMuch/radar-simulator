package org.example;
import javafx.beans.property.BooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Plane extends AntiRadiationMissile { // ARM1 is related to the second grafic{
    int i = 0;
    double angle = 0;
    double targetAngle;
    boolean reverse = false;
    Plane(double x, double y, ImageView view1, ImageView view2, int height, double azimuth) {
        super(x,y,view1,view2, height, azimuth);
        speed = 100;
        targetCrossSection.setValue(0.3);
        speedKmPerSecond.setValue(1500);
        this.angle = Math.toRadians(azimuth);
        this.targetAngle = angle;

    }

    @Override
    boolean update(double dt, double targetX, double targetY, BooleanProperty radarOn, Arc sector, Pane interceptorLayer, Pane missileLayer) {
        System.out.println("Hardcorrr!!!!");
        //        diveAngle.setValue(0);
//        if(distanceInKm.getValue() > 90) {
//            view.setVisible(false);
//            view3.setVisible(true);
//        } else {
//            view3.setVisible(false);
//            view.setVisible(true);
//        }
//        System.out.println("distance " + distanceInKm.getValue() + "heigth " + heightProperty().getValue());
//        System.out.println("-----------------------------------------------------------------------");
//        double a = (-0.0072*Math.pow((distanceInKm.getValue() - 50),2) + 18);
//        a*=1000;
//        height.setValue(a);
//        double slope = (-0.0144) * (distanceInKm.getValue() - 50);
//        double angle = Math.toDegrees(Math.atan(slope));
//        diveAngle.setValue(angle);
//        i++;
//        if(i>100000000) {
//            i = 0;
//        }
//        if(i%1000 == 0) {
//            int random = (int)(Math.random() * (200)) + 1450;
//            speedKmPerSecond.set(random);
//        }
//
//        if (height.getValue() > maxHeight) {
//            maxHeight = height.getValue();
//        }
//        if(distanceInKm.getValue() <= Tab76.range && maxHeight >= Tab76.altitude && Math.abs(diveAngle.get())>=Tab76.diveAngle && targetCrossSection.get()>= Tab76.targetCrossSeqtion && approachAngle.get()<= Tab76.approachAngle && speedKmPerSecond.get()>= Tab76.speed) {
//            view.setVisible(false);
//            view = view2;
//            view.setVisible(true);
//        }
//        // distanceInKm.set((int)((600 * distance.get()) / 140));
//        distanceInKm.set((int)( (distance.get()) / 4 ));
//        if(Buttons.safeModeOn.get() == false && distance.get() <= 300 && isEngaged == false && FirstTable.rocketsCount > 0) {
//            isEngaged = true;
//            AntiRadiationMissile.launchRocket(interceptorLayer, sector, id);
//        }
        double speed = 30;
        double turnSpeed = 0.02;

// 1. один раз включаем разворот
        if (!reverse) {
            reverse = true;
            targetAngle = angle + Math.PI;
        }

// 2. считаем разницу
        double diff = normalizeAngle(targetAngle - angle);

// 3. поворот (ВАЖНО: через diff, но цель НЕ меняется каждый кадр)
        angle += diff * turnSpeed;

// 4. движение
        x += Math.cos(angle) * speed * dt;
        y += Math.sin(angle) * speed * dt;
//        fraction -= speed * dt / sector.getRadiusY();
//        if (fraction <= 0.5) return true;
//        x = sector.getCenterX() + sector.getRadiusX() * fraction * Math.cos(Math.toRadians(angle));
//        y = sector.getCenterY() - sector.getRadiusY() * fraction * Math.sin(Math.toRadians(angle));
        label.setTranslateX(x + 20); // 10 px справа от ракеты
        label.setTranslateY(y + 20);
        view.setTranslateX(x);
        view.setTranslateY(y);
        System.out.println("X: " + x + "Y: " + y);
        return false;
    }


    public static AntiRadiationMissile spawnMissileARM1(double x, double y) {
        double angle = 60 + 65 * Math.random();//Начальный угол 60(start angle + 15) конечный угол 125(start angle + 80)
        //System.out.println(angle);
        Image img = new Image("E:/диплом/app/armSimulator/src/main/resources/aerodynamic.png"
        );
        Image img2 = new Image("E:/диплом/app/armSimulator/src/main/resources/arm_defined.png"
        );
        ImageView view = new ImageView(img);
        ImageView view2 = new ImageView(img2);
        view.setFitWidth(28);
        view.setPreserveRatio(true);
        view2.setFitWidth(28);
        view2.setPreserveRatio(true);
        Plane m = new Plane(x, y, view, view2,25000, angle);
//        m.angle = angle;
        Main.missileLayer.getChildren().addAll(view,view2);
        view2.setVisible(false);
        //создаем текст номера ракеты
        Text label = m.label;
        label.setFill(Color.RED);
        label.setFont(Font.font(30));
        label.setTranslateX(m.x);
        label.setTranslateY(m.y);
        Main.missileLayer.getChildren().add(label);
        Buttons.missiles.add(m);
        return m;
    }
    private double normalizeAngle(double a) {
        while (a > Math.PI) a -= 2 * Math.PI;
        while (a < -Math.PI) a += 2 * Math.PI;
        return a;
    }
}
