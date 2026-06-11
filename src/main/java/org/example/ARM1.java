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

public class ARM1 extends AntiRadiationMissile { // ARM1 is related to the second grafic{
    int i = 0;
    ImageView view3= new ImageView();
    ARM1(double x, double y, ImageView view1, ImageView view2, ImageView view3, int height, double azimuth) {
        super(x,y,view1,view2, view3,height, azimuth,"ARM2");
        speed = 4;
        speedKmPerSecond.setValue(1500);
        line = new Line();
        line.setStartX(x);
        line.setStartY(y);
        line.setEndX(Main.sector.getCenterX());
        line.setEndY(Main.sector.getCenterY());
        line.setStrokeWidth(5);
        line.setStroke(Color.WHITE);
        line.getStrokeDashArray().addAll(20d,10d);
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
        arrow1.setStrokeWidth(4);
        arrow1.setStroke(Color.RED);
        arrow2 = new Line();
        arrow2.setStartX(endX);
        arrow2.setStartY(endY);
        arrow2.setEndX(endX - len * Math.cos(angle + alpha));
        arrow2.setEndY(endY - len * Math.sin(angle + alpha));
        arrow2.setStrokeWidth(4);
        arrow2.setStroke(Color.RED);
        cross = new Line();
        cross.setStroke(Color.RED);
        cross.setStrokeWidth(5);
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
        interceptPoint = new Line();
        interceptPoint.setStroke(Color.WHITE);
        interceptPoint.setStrokeWidth(5);
        Main.missileLayer.getChildren().addAll(arrow1,arrow2,cross,interceptPoint);
        plane = view3;
    }

    @Override
    boolean update(double dt, double targetX, double targetY, Boolean radarOn, Arc sector, Pane interceptorLayer, Pane missileLayer) {
        diveAngle.setValue(0);
        if(!isPlaneStrart) {
            this.targetType.set("ARM2");
            Plane.spawnMissileARM1(x,y);
            isPlaneStrart = true;
        }
        if(!radarOn || Buttons.passiveSearchOn.get()){
            view.setVisible(false);
            line.setVisible(false);
            cross.setVisible(false);
            arrow1.setVisible(false);
            arrow2.setVisible(false);
            label.setVisible(false);
            interceptPoint.setVisible(false);
        } else {
            view.setVisible(true);
            line.setVisible(true);
            cross.setVisible(true);
            arrow1.setVisible(true);
            arrow2.setVisible(true);
            label.setVisible(true);
            if(isPlaneStrart) {
                interceptPoint.setVisible(true);
            }
        }
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
        if (Buttons.tab1Yes) {
            if(distanceInKm.getValue() <= Tab76.range &&
                    maxHeight <= Tab76.altitude &&
                    Math.abs(diveAngle.get())>=Tab76.minDiveAngle && Math.abs(diveAngle.get())<=Tab76.maxDiveAngle &&
                    targetCrossSection.get()>= Tab76.targetCrossSeqtion
                    && approachAngle.get()>= Tab76.approachAngle
                    && speedKmPerSecond.get()>= Tab76.minSpeed && speedKmPerSecond.get()<= Tab76.maxSpeed) {
                view.setVisible(false);
                view3 = view;
                view = view2;
                view.setVisible(true);
            }
        } else {
            view.setVisible(false);
        }
        distanceInKm.set((int)( (distance.get()) / 4 ));
        if(Buttons.safeModeOn.get() == false && distance.get() <= 300 && isEngaged == false && FirstTable.rocketsCount > 0) {
            isEngaged = true;
            AntiRadiationMissile.launchRocket(interceptorLayer, sector, id);
        }
        double dx =0, dy =0;
            dx = (targetX - 15) - x;
            dy = (targetY - 20) - y;
            distance.set(Math.sqrt(dx*dx + dy*dy));;
            if (distance.get() < 10) {
                distanceInKm.set(0);height.setValue(0);return true;}; // ракета достигла цели
            dx /= distance.get();
            dy /= distance.get();
        drawInterceptPoint(dx,dy);
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
        //System.out.println(angle);angle = 45 + 90 * Math.random();
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
        ImageView view3 = new ImageView(new Image("E:/диплом/app/armSimulator/src/main/resources/aerodynamic.png"
        ));
        view.setFitWidth(28);
        view.setPreserveRatio(true);
        view2.setFitWidth(28);
        view2.setPreserveRatio(true);
        ARM1 m = new ARM1(x, y, view, view2, view3,25000, angle);
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
    public void drawInterceptPoint(double dx, double dy) {
        double time = distance.get()/InterceptorMissile.speed;
        double futureX = x + dx * speed * (time - 8);
        double futureY = y + dy * speed * (time - 8);
        double px = -dy ;
        double py = dx;
        int lineLength = 10;
        int ang;
        if(azimuth.get()<=90) {
            ang = 20;
        } else if(azimuth.get()<=70){
            ang = 40;
        } else if(azimuth.get()>=110){
            ang = 5;
        } else{
            ang = 10;
        }

        if(interceptor == null) {
            interceptPoint.setStartX(
                    futureX  + px * lineLength + ang
            );

            interceptPoint.setStartY(
                    futureY + py * lineLength
            );

            interceptPoint.setEndX(
                    futureX  - px * lineLength + ang
            );

            interceptPoint.setEndY(
                    futureY  - py * lineLength
            );
        }
    }
}
