package org.example;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Labels {
    public static Text createRocketsLabel() {
        Text rocketsLabel = new Text();
        rocketsLabel.setFont(Font.font(18));
        rocketsLabel.setFill(Color.LIME);
        rocketsLabel.setX(20);
        rocketsLabel.setY(160);
        rocketsLabel.setText("Ракет в запасе: " + FirstTable.rocketsCount);

        return rocketsLabel;
    }
}
