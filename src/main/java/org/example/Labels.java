package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;

public class Labels {
    public static Text rocketsLabel = new Text();
    public static Text tab1Label;
    public static Text createRocketsLabel() {
        rocketsLabel.setVisible(false);
        rocketsLabel = new Text();
        rocketsLabel.setFont(Font.font(18));
        rocketsLabel.setFill(Color.LIME);
        rocketsLabel.setX(20);
        rocketsLabel.setY(160);
        rocketsLabel.setText("Rockets count: " + FirstTable.rocketsCount);
        return rocketsLabel;
    }
}
