package org.example;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ARM3 extends AntiRadiationMissile {// ARM3 is related to the first grafic{
    int i = 0;
    ImageView view3 ;
    boolean isMessageShowed = false;
    ARM3(double x, double y, ImageView view1, ImageView view2, ImageView view3, int height, double azimuth) {
        super(x,y,view1,view2,view3,height, azimuth,"ARM1");
        this.view3 = view1;
        speed = 4;//normal speed 4
        speedKmPerSecond.setValue(1000);
        this.height.setValue(height);
        line = new Line();
        line.setStrokeWidth(5);
        line.setStroke(Color.WHITE);
        line.getStrokeDashArray().addAll(20d,10d);
        Main.missileLayer.getChildren().add(line);
        arrow1 = new Line();
        arrow1.setStrokeWidth(4);
        arrow1.setStroke(Color.RED);
        arrow2 = new Line();
        arrow2.setStrokeWidth(4);
        arrow2.setStroke(Color.RED);
        cross = new Line();
        cross.setStroke(Color.RED);
        cross.setStrokeWidth(4);
        interceptPoint = new Line();
        interceptPoint.setStroke(Color.WHITE);
        interceptPoint.setStrokeWidth(5);
        Main.missileLayer.getChildren().addAll(arrow1,arrow2,cross,interceptPoint);

    }

    @Override
    boolean update(double dt, double targetX, double targetY, Boolean radarOn, Arc sector, Pane interceptorLayer, Pane missileLayer) {
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
        if(!isPlaneStrart) {
            this.interceptPoint.setVisible(false);
        }
        height.setValue(1500);
        diveAngle.setValue(0);
        if(distanceInKm.getValue() <= 90 && distanceInKm.getValue() != 0) {
            if(!isPlaneStrart) {
                this.targetType.set("ARM1");
                this.interceptPoint.setVisible(true);
                Plane.spawnMissileARM1(x,y);
                isPlaneStrart = true;
            }
            double a = (-35.0/2025)*Math.pow((distanceInKm.getValue() - 45),2) + 35;
            a*=1000;
            height.setValue(a);
            double slope = (-14.0/405) * (distanceInKm.getValue() - 45);
            double angle = Math.toDegrees(Math.atan(slope));
            diveAngle.setValue(angle);
            calculateLine();
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
        if (Buttons.tab1Yes) {
            if(distanceInKm.getValue() <= Tab76.range &&
                    height.getValue() <= Tab76.altitude &&
                    Math.abs(diveAngle.get())>=Tab76.minDiveAngle && Math.abs(diveAngle.get())<=Tab76.maxDiveAngle &&
                    targetCrossSection.get()>= Tab76.targetCrossSeqtion
                    && approachAngle.get()>= Tab76.approachAngle
                    && speedKmPerSecond.get()>= Tab76.minSpeed && speedKmPerSecond.get()<= Tab76.maxSpeed) {
                view.setVisible(false);
                view = view2;
//                if(!isMessageShowed){
//                    isMessageShowed = true;
//                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                    alert.setTitle("Попередження");
//                    alert.setHeaderText("Помічено ПРР!");
//                    alert.setContentText("Увімкніть CARM MODE");
//                    alert.show();
//                }
                view.setVisible(true);
            } else {
                view.setVisible(false);
                view = view3;
                view.setVisible(true);
            }
        } else {
            view.setVisible(false);
            view = view3;
            view.setVisible(true);
        }
        distanceInKm.set((int)( (distance.get()) / 4 ));
        if(Buttons.safeModeOn.get() == false && distance.get() <= 300 && isEngaged == false && FirstTable.rocketsCount > 0) {
            isEngaged = true;
            AntiRadiationMissile.launchRocket(interceptorLayer, sector, id);
        }
            double dx, dy;
            dx = (targetX - 15) - x;
            dy = (targetY - 20) - y;
            distance.set(Math.sqrt(dx*dx + dy*dy));

            //if(id == 1)
            //System.out.println("ID:" + id + " X:" + x + " Y:" + y + " Distance(dx*dx + dy*dy):" + distance.get() + " targetX:" + (targetX - 15) + " targetY:" + (targetY - 20));
            if (distance.get() < 10) {
                distanceInKm.set(0);height.setValue(0);
                if(!Buttons.isCarmModeOn) {
                    Alert alret = new Alert(Alert.AlertType.CONFIRMATION);
                    alret.setHeaderText("You didnt turn on CARM MODE");
                    alret.show();
                }
                return true;}; // ракета достигла цели
            if(interceptor == null) {

            }
            dx /= distance.get();
            dy /= distance.get();
            drawInterceptPoint(dx, dy);
            x += dx * speed * dt;
            y += dy * speed * dt;
            label.setTranslateX(x + 20); // 10 px справа от ракеты
            label.setTranslateY(y + 20);
            view.setTranslateX(x);
            view.setTranslateY(y);
            return false;
    }
    public void calculateLine() {
        line.setStartX(x + 15 );
        line.setStartY(y +30);
        line.setEndX(Main.sector.getCenterX());
        line.setEndY(Main.sector.getCenterY());;
        line.getStrokeDashArray().addAll(20d,10d);
        double endX = line.getEndX();
        double endY = line.getEndY();
        double angle = Math.atan2(endY - y, endX - x);
        double len = 20;
        double alpha = Math.toRadians(25);
        arrow1.setStartX(endX);
        arrow1.setStartY(endY);
        arrow1.setEndX(endX - len * Math.cos(angle - alpha));
        arrow1.setEndY(endY - len * Math.sin(angle - alpha));;
        arrow2.setStartX(endX);
        arrow2.setStartY(endY);
        arrow2.setEndX(endX - len * Math.cos(angle + alpha));
        arrow2.setEndY(endY - len * Math.sin(angle + alpha));
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
    }
    public static AntiRadiationMissile spawnMissileARM1(Pane missileLayer, Arc sector) {

        double angle = 60 + 65 * Math.random();//Начальный угол 60(start angle + 15) конечный угол 125(start angle + 80)
        //System.out.println(angle);
        double x = sector.getCenterX()
                + ((sector.getRadiusX()*(4.3/5)) ) * Math.cos(Math.toRadians(angle));

        double y = sector.getCenterY()
                - ((sector.getRadiusX()*(4.3/5)) )* Math.sin(Math.toRadians(angle));

        Image img = new Image(ClassLoader.getSystemResourceAsStream("aerodynamic.png"));
        Image img2 = new Image(ClassLoader.getSystemResourceAsStream("arm_defined.png"));

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
    public void drawInterceptPoint(double dx, double dy) {
        double time = distance.get()/InterceptorMissile.speed;
        double futureX = x + dx * speed * (time - 5);
        double futureY = y + dy * speed * (time - 5);
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