package org.example;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Images {
    public static ImageView createGraphicImage() {

        Image image = new Image("E:/диплом/app/armSimulator/src/main/resources/trajectories.png");
        ImageView view = new ImageView(image);

//        view.setFitHeight(100);
//        view.setFitWidth(100);
         view.setPreserveRatio(true);
         view.setFitWidth(320);
        view.setTranslateX(-330);
        view.setTranslateY(160);
        Main.root.getChildren().add(view);
        return view;
    }
}
