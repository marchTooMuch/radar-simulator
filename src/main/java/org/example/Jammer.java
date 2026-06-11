package org.example;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jammer {
    ImageView view;
    public static List<Jammer> jammers = new ArrayList<>();
    Line line;
    Jammer(ImageView view, Line line) {
        this.view = view;
        this.line = line;
    }

    public static void createJammer() {
        double angle = 60 + 65 * Math.random();//Начальный угол 60(start angle + 15) конечный угол 125(start angle + 80)
        //System.out.println(angle);
        double d = new Random().nextDouble();
        double x = Main.sector.getCenterX()
                + ((Main.sector.getRadiusX()) ) * Math.cos(Math.toRadians(angle));

        double y = Main.sector.getCenterY()
                - ((Main.sector.getRadiusY()) )* Math.sin(Math.toRadians(angle));
        Image img = new Image("E:/диплом/app/armSimulator/src/main/resources/jammer.png");
        ImageView view = new ImageView(img);
        Line line = new Line();
        Jammer jam = new Jammer(view, line);
        jammers.add(jam);
        view.setFitWidth(28);
        view.setPreserveRatio(true);
        if(Buttons.radarOn){
            jam.view.setVisible(true);
            jam.line.setVisible(true);
        } else {
            jam.view.setVisible(false);
            jam.line.setVisible(false);
        }
        view.setX(x);
        view.setY(y);
        line.setStartX(x + 15);
        line.setStartY(y + 25);
        line.setEndX(Main.sector.getCenterX());
        line.setEndY(Main.sector.getCenterY());
        line.setStrokeWidth(2);
        line.setStroke(Color.WHITE);
        Main.missileLayer.getChildren().addAll(view,line);
    }
}
