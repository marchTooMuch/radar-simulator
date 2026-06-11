package org.example;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Graphics {
    private static final int WIDTH = 333;
    private static final int HEIGHT = 233;

    private static final int LEFT_MARGIN = 27;
    private static final int RIGHT_MARGIN = 13;
    private static final int TOP_MARGIN = 13;
    private static final int BOTTOM_MARGIN = 27;

    // Максимумы осей
    private static final double MAX_DISTANCE = 140.0; // км
    private static final double MAX_HEIGHT = 40.0;
    private static final int GRAPH_WIDTH =
            WIDTH - LEFT_MARGIN - RIGHT_MARGIN;

    private static final int GRAPH_HEIGHT =
            HEIGHT - TOP_MARGIN - BOTTOM_MARGIN;// км


    public static void drawGraphic() {
        Stage stage = new Stage();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawAxes(gc);
        drawArc(gc,30,30);
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        stage.setTitle("График");
        stage.setScene(scene);
        stage.show();
    }
    public static void drawAxes(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setLineWidth(1);
        gc.strokeLine(LEFT_MARGIN,HEIGHT - BOTTOM_MARGIN, LEFT_MARGIN,TOP_MARGIN);
        gc.setFont(Font.font(10));
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(10));
        drawGrid(gc);
        drawXMarks(gc);
        drawYMarks(gc);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        // Ось X
        gc.strokeLine(
                LEFT_MARGIN,
                HEIGHT - BOTTOM_MARGIN,
                WIDTH - RIGHT_MARGIN,
                HEIGHT - BOTTOM_MARGIN
        );

        // Ось Y
        gc.strokeLine(
                LEFT_MARGIN,
                HEIGHT - BOTTOM_MARGIN,
                LEFT_MARGIN,
                TOP_MARGIN
        );
    }
    private static void drawGrid(GraphicsContext gc) {

        gc.setStroke(Color.LIGHTGRAY);

        for (int i = 0; i <= 14; i++) {
            double x = LEFT_MARGIN + i * (GRAPH_WIDTH / 14.0);
            gc.strokeLine(x, TOP_MARGIN, x, HEIGHT - BOTTOM_MARGIN);
        }

        for (int i = 0; i <= 8; i++) {
            double y = TOP_MARGIN + i * (GRAPH_HEIGHT / 8.0);
            gc.strokeLine(LEFT_MARGIN, y, WIDTH - RIGHT_MARGIN, y);
        }
    }

    private static  void drawXMarks(GraphicsContext gc) {

        for (int i = 0; i <= 14; i++) {

            double value = i * 10.0;
            double x = LEFT_MARGIN + i * (GRAPH_WIDTH / 14.0);

            gc.strokeLine(x, HEIGHT - BOTTOM_MARGIN, x, HEIGHT - BOTTOM_MARGIN + 4);

            gc.fillText(
                    String.format("%.0f", value),
                    x - 8,
                    HEIGHT - BOTTOM_MARGIN + 12
            );
        }
    }

    private static void drawYMarks(GraphicsContext gc) {
        for (int i = 0; i <= 8; i++) {

            double value = MAX_HEIGHT - i * 5.0;
            double y = TOP_MARGIN + i * (GRAPH_HEIGHT / 8.0);

            gc.strokeLine(LEFT_MARGIN - 4, y, LEFT_MARGIN, y);

            gc.fillText(
                    String.format("%.0f", value),
                    LEFT_MARGIN - 18,
                    y + 3
            );
            gc.setFont(new Font(20));
            gc.fillText("Range KM",220,230);
            gc.fillText("ALTITUDE KM",28,15);
            gc.setFont(new Font(10));
        }
    }
    static void drawArc(GraphicsContext gc, double offsetX, double offsetY) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.beginPath();
        double x1 = Tab76.range * 2.09 + 27;
        double y1 = 205;
        double x2;
        int offsett;
        x2 = 27 + (Tab76.range - 35) * 2.09;
        double y2 = 13;
        double x = (x1 + x2)/2 + 40;
        double y = (y1 + y2)/2;
        gc.moveTo(x1, y1);
        gc.quadraticCurveTo(x, y, x2, y2);
        gc.stroke();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        x1 = 27;
        x2 = 320;
        y1 = y2 = (13 + ((Math.abs(Tab76.altitude/1000 - 40)) * 4.575));
        System.out.println();
        gc.strokeLine(x1,y1,x2,y2);
    }
}
