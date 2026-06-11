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
    Plane(double x, double y, ImageView view1, ImageView view2, ImageView view3, int height, double azimuth) {
        super(x,y,view1,view2, view3,height, azimuth,"Plane");
        speed = 10;
        targetCrossSection.setValue(15);
        speedKmPerSecond.setValue(1500);
        this.angle = Math.toRadians(azimuth);
        this.targetAngle = angle;
    }

    @Override
    boolean update(double dt, double targetX, double targetY, Boolean radarOn, Arc sector, Pane interceptorLayer, Pane missileLayer) {
        if(!radarOn || Buttons.passiveSearchOn.get()){
            label.setVisible(false);
            view.setVisible(false);
            view2.setVisible(false);
        } else {
            label.setVisible(true);
            view.setVisible(true);
            view2.setVisible(true);
        }
        double dx = (targetX - 15) - x;
        double dy = (targetY - 20) - y;
        distance.set(Math.sqrt(dx*dx + dy*dy));
        distanceInKm.set((int)( (distance.get()) / 4 ));
        azimuth.setValue(Math.round(getAzimuth(x,y)));
        height.setValue(1500);
        double speed = 3;
        double turnSpeed = 0.001;
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
        ImageView view3 = new ImageView(new Image("E:/диплом/app/armSimulator/src/main/resources/aerodynamic.png"
        ));
        view.setFitWidth(28);
        view.setPreserveRatio(true);
        view2.setFitWidth(28);
        view2.setPreserveRatio(true);
        Plane m = new Plane(x, y, view, view2, view3, 25000, angle);
//        m.angle = angle;
        Main.missileLayer.getChildren().addAll(view,view2);
        view2.setVisible(false);
        //создаем текст номера ракеты
        Text label = m.label;
        label.setFill(Color.RED);
        label.setFont(Font.font(30));
        label.setTranslateX(m.x);
        label.setTranslateY(m.y);
        Main.missileLayer.getChildren().add(m.label);
        Buttons.missiles.add(m);
        return m;
    }
    private double normalizeAngle(double a) {
        while (a > Math.PI) a -= 2 * Math.PI;
        while (a < -Math.PI) a += 2 * Math.PI;
        return a;
    }
    public double getAzimuth(double x, double y) {
       double dx = x - Main.sector.getCenterX();
       double dy = Main.sector.getCenterY() - y;
       double angle = Math.toDegrees(Math.atan2(dy, dx));
       return angle;
    }
}
