package org.example;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jammer {
    ImageView view;
    public static List<Jammer> jammers = new ArrayList<>();

    Jammer(ImageView view) {
        this.view = view;
    }

    public static void createJammer() {
        double angle = 60 + 65 * Math.random();//Начальный угол 60(start angle + 15) конечный угол 125(start angle + 80)
        //System.out.println(angle);
        double d = new Random().nextDouble();
        double x = Main.sector.getCenterX()
                + ((Main.sector.getRadiusX()*(d)) ) * Math.cos(Math.toRadians(angle));

        double y = Main.sector.getCenterY()
                - ((Main.sector.getRadiusX()*(4.3/5)) )* Math.sin(Math.toRadians(angle));
        Image img = new Image("E:/диплом/app/armSimulator/src/main/resources/jammer.png");
        ImageView view = new ImageView(img);
        Jammer jam = new Jammer(view);
        jammers.add(jam);
        view.setFitWidth(28);
        view.setPreserveRatio(true);
        view.setVisible(true);
        view.setX(x);
        view.setY(y);
        Main.missileLayer.getChildren().add(view);
    }
}
