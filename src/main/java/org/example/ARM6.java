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

public class ARM6 extends AntiRadiationMissile {// ARM3 is related to the first grafic{
    int i = 0;
    ARM6(double x, double y, ImageView view1, ImageView view2, ImageView view3, int height, double azimuth) {
        super(x,y,view1,view2, view3, height, azimuth,"ARM6");
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
        if (distanceInKm.getValue()>25) {
            height.setValue(700);
        }
        double dx = 0;
        double dy = 0;
        dx = (targetX - 15) - x;
        dy = (targetY - 20) - y;
        double length2 = Math.sqrt(dx * dx + dy * dy);

        double nx = 0;
        double ny = 0;
        if (length2 != 0) {
            nx = dx / length2;
            ny = dy / length2;
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
        if(distanceInKm.getValue() <= 55 && distanceInKm.getValue() != 0){
            firstLine(nx,ny);
        }
        if(distanceInKm.getValue() < 12 && distanceInKm.getValue()!=0) {
            secondLine();
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
        if (Buttons.tab1Yes) {
            if(distanceInKm.getValue() <= Tab76.range &&
                    maxHeight <= Tab76.altitude &&
                    Math.abs(diveAngle.get())>=Tab76.minDiveAngle && Math.abs(diveAngle.get())<=Tab76.maxDiveAngle &&
                    targetCrossSection.get()>= Tab76.targetCrossSeqtion
                    && approachAngle.get()>= Tab76.approachAngle
                    && speedKmPerSecond.get()>= Tab76.minSpeed && speedKmPerSecond.get()<= Tab76.maxSpeed) {
                view.setVisible(false);
                view = view2;
                view.setVisible(true);
            }
        }
        // distanceInKm.set((int)((600 * distance.get()) / 140));
        distanceInKm.set((int)( (distance.get()) / 4 ));
        if(Buttons.safeModeOn.get() == false && distance.get() <= 300 && isEngaged == false && FirstTable.rocketsCount > 0) {
            isEngaged = true;
            AntiRadiationMissile.launchRocket(interceptorLayer, sector, id);
        }
            dx = (targetX - 15) - x;
            dy = (targetY - 20) - y;
            distance.set(Math.sqrt(dx*dx + dy*dy));
            if (distance.get() < 10) {
                distanceInKm.set(0);height.setValue(0);return true;}; // ракета достигла цели

            dx /= distance.get();
            dy /= distance.get();
            drawInterceptPoint(dx,dy);
        x += dx * speed * dt;
        y += dy * speed * dt;
        label.setTranslateX(x + 20);
        label.setTranslateY(y + 20);
        view.setTranslateX(x);
        view.setTranslateY(y);
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
        ImageView view3 = new ImageView(new Image("E:/диплом/app/armSimulator/src/main/resources/aerodynamic.png"
        ));
        ImageView view = new ImageView(img);
        ImageView view2 = new ImageView(img2);

        view.setFitWidth(28);
        view.setPreserveRatio(true);
        view.setVisible(true);
        view2.setFitWidth(28);
        view2.setPreserveRatio(true);
        ARM6 m = new ARM6(x, y, view, view2, view3, 8000, angle);
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
    public void firstLine(double nx, double ny) {
        this.targetType.set("ARM6");
        line.setStartX(x + 15);
        line.setStartY(y + 25);
        line.setStartX(x + 15);
        line.setStartY(y + 25);
        double fixedLength = 80;
        double endx = x + 15 + nx * fixedLength;
        double endy = y + 25 + ny * fixedLength;
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
        if(!isPlaneStrart) {
            Plane.spawnMissileARM1(x,y);
            isPlaneStrart = true;
            interceptPoint.setVisible(true);
        }
    }
    public void secondLine() {
        double a = (-1/3.0)*Math.pow((distanceInKm.getValue() - 6),2) + 12;
        a*=1000;
        height.setValue(a);
        double slope = (-3.0/2) * (distanceInKm.getValue() - 4);
        double angle = Math.toDegrees(Math.atan(slope));
        diveAngle.setValue(angle);
        line.setStartX(x + 15);
        line.setStartY(y + 25);
        line.setEndX(Main.sector.getCenterX());
        line.setEndY(Main.sector.getCenterY());
        double endX = Main.sector.getCenterX();
        double endY = Main.sector.getCenterY();
        double angleArrow = Math.atan2(endY - y, endX - x);
        double len = 20;
        double alpha = Math.toRadians(25);
        arrow1.setStartX(endX);
        arrow1.setStartY(endY);
        arrow1.setEndX(endX - len * Math.cos(angleArrow - alpha));
        arrow1.setEndY(endY - len * Math.sin(angleArrow - alpha));;
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
    public void drawInterceptPoint(double dx, double dy) {
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
    }
}