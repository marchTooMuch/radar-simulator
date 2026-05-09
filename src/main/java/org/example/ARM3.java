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

public class ARM3 extends AntiRadiationMissile {// ARM3 is related to the first grafic{
    int i = 0;
    ARM3(double x, double y, ImageView view1, ImageView view2, ImageView view3, int height, double azimuth) {
        super(x,y,view1,view2,height, azimuth);
        speed = 4;
        speedKmPerSecond.setValue(1000);
        targetCrossSection.setValue(0.1);
        this.height.setValue(height);
        line = new Line();
        line.setStartX(x);
        line.setStartY(y);
        line.setEndX(Main.sector.getCenterX());
        line.setEndY(Main.sector.getCenterY());
        line.setStrokeWidth(2);
        line.setStroke(Color.RED);
        line.getStrokeDashArray().addAll(5d,5d);
        Main.missileLayer.getChildren().add(line);
        double endX = line.getEndX();
        double endY = line.getEndY();
        double x1 = endX + 20;
        double y1 = endY - 30;
        double x2 = endX - 10;
        double y2 = endY - 30;
        double angle = Math.atan2(endY - y, endX - x);
        double len = 20;
        double alpha = Math.toRadians(25);
        arrow1 = new Line();
        arrow1.setStartX(endX);
        arrow1.setStartY(endY);
        arrow1.setEndX(endX - len * Math.cos(angle - alpha));
        arrow1.setEndY(endY - len * Math.sin(angle - alpha));;
        arrow1.setStrokeWidth(2);
        arrow1.setStroke(Color.RED);
        arrow2 = new Line();
        arrow2.setStartX(endX);
        arrow2.setStartY(endY);
        arrow2.setEndX(endX - len * Math.cos(angle + alpha));
        arrow2.setEndY(endY - len * Math.sin(angle + alpha));
        arrow2.setStrokeWidth(2);
        arrow2.setStroke(Color.RED);
        cross = new Line();
        cross.setStroke(Color.RED);
        cross.setStrokeWidth(2);
        double dx = endX - x;
        double dy = endY - y;
        double lenCross = 10;
        double length = Math.sqrt(dx * dx + dy * dy);
        double px = -dy / length;
        double py = dx / length;
        cross.setStartX(endX + px * lenCross);
        cross.setStartY(endY + py * lenCross);
        cross.setEndX(endX - px * lenCross);
        cross.setEndY(endY - py * lenCross);
        Main.missileLayer.getChildren().addAll(arrow1,arrow2,cross);
        plane = view3;
    }

    @Override
    boolean update(double dt, double targetX, double targetY, Boolean radarOn, Arc sector, Pane interceptorLayer, Pane missileLayer) {
        if(!radarOn){
            label.setVisible(false);
            view.setVisible(false);
            view2.setVisible(false);
        } else {
            label.setVisible(true);
            view.setVisible(true);
            view2.setVisible(true);
        }
        height.setValue(1500);
        diveAngle.setValue(0);
//        if(distanceInKm.getValue() > 90) {
//            view.setVisible(false);
//            view3.setVisible(true);
//        } else {
//            view3.setVisible(false);
//            view.setVisible(true);
//        }
//        System.out.println("distance " + distanceInKm.getValue() + "heigth " + heightProperty().getValue());
        if(distanceInKm.getValue() <= 90 && distanceInKm.getValue() != 0) {
            if(!isPlaneStrart) {
                System.out.println("distance:" + distanceInKm.getValue());
                Plane.spawnMissileARM1(x,y);
                isPlaneStrart = true;
            }
//            System.out.println("-----------------------------------------------------------------------");
            double a = (-35.0/2025)*Math.pow((distanceInKm.getValue() - 45),2) + 35;
            a*=1000;
            height.setValue(a);
            double slope = (-14.0/405) * (distanceInKm.getValue() - 45);
            double angle = Math.toDegrees(Math.atan(slope));
            diveAngle.setValue(angle);
        }
        i++;
        if(i>100000000) {
            i = 0;
        }
        if(i%1000 == 0) {
            int random = (int)(Math.random() * (1100 - 950 + 1)) + 950;
            speedKmPerSecond.set(random);
        }

        if (height.getValue() > maxHeight) {
            maxHeight = height.getValue();
        }
        if(distanceInKm.getValue() <= Tab76.range &&
                maxHeight >= Tab76.altitude &&
                Math.abs(diveAngle.get())>=Tab76.minDiveAngle && Math.abs(diveAngle.get())<=Tab76.maxDiveAngle &&
                targetCrossSection.get()>= Tab76.targetCrossSeqtion
                && approachAngle.get()<= Tab76.approachAngle
                && speedKmPerSecond.get()>= Tab76.minSpeed && speedKmPerSecond.get()<= Tab76.maxSpeed) {
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
        if (radarOn) {
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
        line.setStartX(x + 15);
        line.setStartY(y + 25);
        return false;
    }


    public static AntiRadiationMissile spawnMissileARM1(Pane missileLayer, Arc sector) {

        double angle = 60 + 65 * Math.random();//Начальный угол 60(start angle + 15) конечный угол 125(start angle + 80)
        //System.out.println(angle);
        double x = sector.getCenterX()
                + ((sector.getRadiusX()*(4.3/5)) ) * Math.cos(Math.toRadians(angle));

        double y = sector.getCenterY()
                - ((sector.getRadiusX()*(4.3/5)) )* Math.sin(Math.toRadians(angle));

        Image img = new Image("E:/диплом/app/armSimulator/src/main/resources/aerodynamic.png"
        );
        Image img2 = new Image("E:/диплом/app/armSimulator/src/main/resources/arm_defined.png"
        );
        Image img3 = new Image("E:/диплом/app/armSimulator/src/main/resources/aerodynamic.png"
        );

        ImageView view = new ImageView(img);
        ImageView view2 = new ImageView(img2);
        ImageView view3 = new ImageView(img2);

        view.setFitWidth(28);
        view.setPreserveRatio(true);
        view.setVisible(true);
        view2.setFitWidth(28);
        view2.setPreserveRatio(true);
        view3.setFitWidth(28);
        view3.setPreserveRatio(true);
        ARM3 m = new ARM3(x, y, view, view2,view3, 1500, angle);
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