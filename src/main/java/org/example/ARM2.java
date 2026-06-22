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

public class ARM2 extends AntiRadiationMissile {// ARM2 is related to the third grafic{


    int i = 0;
    ARM2(double x, double y, ImageView view1, ImageView view2, ImageView view3,int height, double azimuth) {
        super(x,y,view1,view2,view3,height, azimuth,"ARM3");
        this.view3 = view1;
        speed = 4;
        speedKmPerSecond.setValue(2000);
        targetCrossSection.setValue(AntiRadiationMissile.EPR);
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
        interceptPoint.setVisible(false);
        Main.missileLayer.getChildren().addAll(arrow1,arrow2,cross,interceptPoint);
    }

    @Override
    boolean update(double dt, double targetX, double targetY, Boolean radarOn, Arc sector, Pane interceptorLayer, Pane missileLayer) {
        double dx = 0;
        double dy = 0;
        dx = (targetX - 15) - x;
        dy = (targetY - 20) - y;
        distance.set(Math.sqrt(dx*dx + dy*dy));
        dx /= distance.get();
        dy /= distance.get();
        drawInterceptPoint(dx,dy);
        if (distance.get() < 10) {
            distanceInKm.set(0);height.setValue(0);return true;}; // ракета достигла цели

        if (distanceInKm.getValue()>70) {
            height.setValue(8000);
        }
        if(distanceInKm.getValue() <= 70 && distanceInKm.getValue() != 0) {
            this.targetType.set("ARM3");
            if(!isPlaneStrart) {
                interceptPoint.setVisible(true);
                Plane.spawnMissileARM1(x,y);
                isPlaneStrart = true;
            }
            double a = distanceInKm.getValue() * 0.1142857142857143 ;
            a*=1000;
            height.setValue(a);
            double slope = 0.1142857142857143;
            double angle = Math.toDegrees(Math.atan(slope));
            diveAngle.setValue(angle);
            if(distance.get()>=80){
                line.setStartX(x + 15);
                line.setStartY(y + 25);
                double fixedLength = 80;
                double endx = x + 15 + dx * fixedLength;
                double endy = y + 25 + dy * fixedLength;
                line.setEndX(endx);
                line.setEndY(endy);
                double endX = endx;
                double endY = endy;
                double angleArrow = Math.atan2(endY - y, endX - x);
                double len = 20;
                double alpha = Math.toRadians(25);
                arrow1.setStartX(endX);
                arrow1.setStartY(endY);
                arrow1.setEndX(endX - len * Math.cos(angleArrow - alpha));
                arrow1.setEndY(endY - len * Math.sin(angleArrow - alpha));;
                arrow1.setStrokeWidth(5);
                arrow2.setStartX(endX);
                arrow2.setStartY(endY);
                arrow2.setEndX(endX - len * Math.cos(angleArrow + alpha));
                arrow2.setEndY(endY - len * Math.sin(angleArrow + alpha));
                double dx2 = endX - x;
                double dy2 = endY - y;
                double lenCross = 10;
                double length = Math.sqrt(dx2 * dx2 + dy2 * dy2);
                double px = -dy2 / length;
                double py = dx2 / length;
                cross.setStartX(endX + px * lenCross);
                cross.setStartY(endY + py * lenCross);
                cross.setEndX(endX - px * lenCross);
                cross.setEndY(endY - py * lenCross);
            }
            line.setStartX(x + 15);
            line.setStartY(y + 25);
        }
        i++;
        if(i>100000000) {
            i = 0;
        }
        if(i%1000 == 0) {
            int random = (int)(Math.random() * (400)) + 1800;
            speedKmPerSecond.set(random);
        }

        if (height.getValue() > maxHeight) {
            maxHeight = height.getValue();
        }
        if (isPlaneStrart) {
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
                } else {
                    view.setVisible(false);
                    view = view3;
                }
            } else {
                view.setVisible(false);
                view = view3;
            }
        }        distanceInKm.set((int)( (distance.get()) / 4 ));
        x += dx * speed * dt;
        y += dy * speed * dt;
        label.setTranslateX(x + 20);
        label.setTranslateY(y + 20);
        view.setTranslateX(x);
        view.setTranslateY(y);
        if(isEngaged){
            hexagon.setVisible(true);
            udateFrameCoord();
        } else {
            hexagon.setVisible(false);
        }
        if(!radarOn || Buttons.passiveSearchOn.get()){
            view.setVisible(false);
            line.setVisible(false);
            cross.setVisible(false);
            arrow1.setVisible(false);
            arrow2.setVisible(false);
            label.setVisible(false);
            interceptPoint.setVisible(false);
            hexagon.setVisible(false);
        } else {
            view.setVisible(true);
            line.setVisible(true);
            cross.setVisible(true);
            arrow1.setVisible(true);
            arrow2.setVisible(true);
            label.setVisible(true);
            hexagon.setVisible(true);
            if(isPlaneStrart) {
                interceptPoint.setVisible(true);
            }
        }
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

        ImageView view = new ImageView(img);
        ImageView view2 = new ImageView(img2);
        ImageView view3 = new ImageView(new Image("E:/диплом/app/armSimulator/src/main/resources/aerodynamic.png"
        ));
        view.setFitWidth(28);
        view.setPreserveRatio(true);
        view.setVisible(true);
        view2.setFitWidth(28);
        view2.setPreserveRatio(true);
        ARM2 m = new ARM2(x, y, view, view2,view3,8000, angle);
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
        if (distance.get() >= 130) {
            double time = distance.get()/InterceptorMissile.speed;
            double futureX = x + dx * speed * (time - 3);
            double futureY = y + dy * speed * (time - 3);
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
        } else {
            double futureX = x + dx * 30;
            double futureY = y + dy * 30;
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

}