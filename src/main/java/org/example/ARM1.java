package org.example;

import javafx.beans.property.BooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ARM1 extends AntiRadiationMissile { // ARM1 is related to the second grafic{
    int i = 0;

    ARM1(double x, double y, ImageView view1, ImageView view2, int height, double azimuth) {
        super(x,y,view1,view2, height, azimuth);
        speed = 6;
        targetCrossSection.setValue(0.3);
        speedKmPerSecond.setValue(1500);
    }

    @Override
    boolean update(double dt, double targetX, double targetY, BooleanProperty radarOn, Arc sector, Pane interceptorLayer, Pane missileLayer) {
        diveAngle.setValue(0);
//        if(distanceInKm.getValue() > 90) {
//            view.setVisible(false);
//            view3.setVisible(true);
//        } else {
//            view3.setVisible(false);
//            view.setVisible(true);
//        }
        System.out.println("distance " + distanceInKm.getValue() + "heigth " + heightProperty().getValue());
            System.out.println("-----------------------------------------------------------------------");
            double a = (-0.0072*Math.pow((distanceInKm.getValue() - 50),2) + 18);
            a*=1000;
            height.setValue(a);
            double slope = (-0.0144) * (distanceInKm.getValue() - 50);
            double angle = Math.toDegrees(Math.atan(slope));
            diveAngle.setValue(angle);
        i++;
        if(i>100000000) {
            i = 0;
        }
        if(i%1000 == 0) {
            int random = (int)(Math.random() * (200)) + 1450;
            speedKmPerSecond.set(random);
        }

        if (height.getValue() > maxHeight) {
            maxHeight = height.getValue();
        }
        if(distanceInKm.getValue() <= Tab76.range && maxHeight >= Tab76.altitude && Math.abs(diveAngle.get())>=Tab76.diveAngle && targetCrossSection.get()>= Tab76.targetCrossSeqtion && approachAngle.get()<= Tab76.approachAngle && speedKmPerSecond.get()>= Tab76.speed) {
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
                distanceInKm.set(0);height.setValue(0);return true;}; // ракета достигла цели

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


    public static AntiRadiationMissile spawnMissileARM1(Pane missileLayer, Arc sector) {
        double angle = 60 + 65 * Math.random();//Начальный угол 60(start angle + 15) конечный угол 125(start angle + 80)
        //System.out.println(angle);
        double x = sector.getCenterX()
                + ((sector.getRadiusX()*(4.4/5)) ) * Math.cos(Math.toRadians(angle));

        double y = sector.getCenterY()
                - ((sector.getRadiusX()*(4.4/5)) )* Math.sin(Math.toRadians(angle));

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
        ARM1 m = new ARM1(x, y, view, view2,25000, angle);
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

}
