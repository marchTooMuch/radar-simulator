package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class Main extends Application {

    private static final int LINES_COUNT = 5;
    private static final double SECTOR_ANGLE = 60;      // градусов
    private static final double MAX_DISTANCE_KM = 140;
    private int rocketsCount; // текущее количество ракет
    private Text rocketsLabel; // надпись на экране
    private ObservableList<RadarStat> radarStats = FXCollections.observableArrayList();

    private BooleanProperty radarOn = new SimpleBooleanProperty(true);

    @Override
    public void start(Stage stage) {

        Pane root = new Pane();

        TextInputDialog dialog = new TextInputDialog("5");
        dialog.setTitle("Настройка ракет");
        dialog.setHeaderText("Введите количество ракет на вооружении:");
        dialog.setContentText("Количество ракет:");
        Pair<Integer, Integer> setup = showAttackSetupDialog();
        rocketsCount = setup.getKey();        // наши ракеты
        int enemyRocketsCount = setup.getValue();

        dialog.showAndWait().ifPresent(input -> {
            try {
                rocketsCount = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                rocketsCount = 5; // значение по умолчанию
            }

            // создаем начальную запись в таблице
            radarStats.add(new RadarStat("Ракет в запасе", String.valueOf(rocketsCount)));
        });
        rocketsLabel = new Text();
        rocketsLabel.setFont(Font.font(18));
        rocketsLabel.setFill(Color.LIME);
        rocketsLabel.setX(20);
        rocketsLabel.setY(160);
        rocketsLabel.setText("Ракет в запасе: " + rocketsCount);

        root.getChildren().add(rocketsLabel);

        Pane radarLayer = new Pane();
        Pane missileLayer = new Pane();
        Pane interceptorLayer = new Pane();
        root.getChildren().addAll(radarLayer,missileLayer,interceptorLayer);

        Button launchButton = new Button("Engage");
        launchButton.setStyle("""
        -fx-background-color: darkgreen;
        -fx-text-fill: lime;
        -fx-font-size: 16px;
        """);

        launchButton.setLayoutX(20);
        launchButton.setLayoutY(20);

        Button radarButton = new Button("РАДАР: ВКЛ");
        radarButton.setStyle("""
        -fx-background-color: darkgreen;
        -fx-text-fill: lime;
        -fx-font-size: 14px;
        """);

        radarButton.setLayoutX(20);
        radarButton.setLayoutY(60);

        root.getChildren().add(radarButton);
        root.getChildren().add(launchButton);


        Button statsButton = new Button("Характеристики радара");
        statsButton.setLayoutX(20);
        statsButton.setLayoutY(100);
        root.getChildren().add(statsButton);


        /* =======================
           Основной сектор
           ======================= */
        Arc sector = new Arc();
        sector.setType(ArcType.ROUND);
        sector.setStartAngle(90 - SECTOR_ANGLE / 2);
        sector.setLength(SECTOR_ANGLE);
        sector.setFill(Color.rgb(0, 255, 0, 0.25));
        sector.setStroke(Color.GREEN);
        sector.setStrokeWidth(2);

        radarLayer.getChildren().add(sector);

        Scene scene = new Scene(root, 1000, 700);

        // Центр сектора внизу
        sector.centerXProperty().bind(scene.widthProperty().divide(2));
        sector.centerYProperty().bind(scene.heightProperty().subtract(50));

        // Радиусы
        sector.radiusXProperty().bind(scene.widthProperty().multiply(0.48));
        sector.radiusYProperty().bind(scene.heightProperty().multiply(0.9));
        /* =======================
           Линии + подписи
           ======================= */
        double startAngle = 90 - SECTOR_ANGLE / 2;

        for (int i = 1; i <= LINES_COUNT; i++) {

            double fraction = i / (double) LINES_COUNT;
            double distance = MAX_DISTANCE_KM * fraction;

            // ---- дуга расстояния
            Arc rangeArc = new Arc();
            rangeArc.setType(ArcType.OPEN);
            rangeArc.setStartAngle(startAngle);
            rangeArc.setLength(SECTOR_ANGLE);
            rangeArc.setFill(Color.TRANSPARENT);
            rangeArc.setStroke(Color.GREEN);
            rangeArc.setStrokeWidth(1);

            rangeArc.centerXProperty().bind(sector.centerXProperty());
            rangeArc.centerYProperty().bind(sector.centerYProperty());
            rangeArc.radiusXProperty().bind(sector.radiusXProperty().multiply(fraction));
            rangeArc.radiusYProperty().bind(sector.radiusYProperty().multiply(fraction));

            radarLayer.getChildren().add(rangeArc);

            // ---- подпись расстояния
            Text label = new Text(String.format("%.0f km", distance));
            label.setFont(Font.font(14));
            label.setFill(Color.GREEN);

            // точка начала дуги (ГЕОМЕТРИЧЕСКИ ВЕРНО)
            label.xProperty().bind(
                    Bindings.createDoubleBinding(
                            () -> sector.getCenterX()
                                    + rangeArc.getRadiusX()
                                    * Math.cos(Math.toRadians(startAngle))
                                    + 5, // отступ от линии
                            sector.centerXProperty(),
                            rangeArc.radiusXProperty()
                    )
            );

            label.yProperty().bind(
                    Bindings.createDoubleBinding(
                            () -> sector.getCenterY()
                                    - rangeArc.getRadiusY()
                                    * Math.sin(Math.toRadians(startAngle)),
                            sector.centerYProperty(),
                            rangeArc.radiusYProperty()
                    )
            );

            radarLayer.getChildren().add(label);
        }


        List<AntiRadiationMissile> missiles = new ArrayList<>();
        List<InterceptorMissile> interceptors = new ArrayList<>();

        radarOn.addListener((obs, oldVal, newVal) -> {

            // скрываем / показываем ПРР
            for (AntiRadiationMissile m : missiles) {
                m.view.setVisible(newVal);
            }

            // скрываем / показываем перехватчики
            for (InterceptorMissile i : interceptors) {
                i.view.setVisible(newVal);
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }

                double dt = (now - last) / 1e9;
                last = now;

                missiles.removeIf(m ->
                        m.update(dt,
                                sector.getCenterX(),
                                sector.getCenterY(),
                                radarOn)
                );

                interceptors.removeIf(interceptor -> {
                    boolean hit = interceptor.update(dt);

                    if (hit && interceptor.target != null) {
                        missileLayer.getChildren()
                                .remove(interceptor.target.view);
                        missiles.remove(interceptor.target);
                    }

                    return hit;
                });

            }
        };

        timer.start();

        // тестовый запуск ракеты
        missiles.add(spawnMissile(missileLayer, sector));

        launchButton.setOnAction(e -> {
            if (!radarOn.get()) return;          // радар выключен
            if (missiles.isEmpty()) return;       // нет целей
            if (rocketsCount <= 0) return;        // нет ракет

            AntiRadiationMissile target = missiles.get(0);
            interceptors.add(spawnInterceptor(interceptorLayer, sector, target));

            // уменьшаем количество ракет
            rocketsCount--;
            rocketsLabel.setText("Ракет в запасе: " + rocketsCount);

            // обновляем таблицу, если она есть
            radarStats.get(0).value = String.valueOf(rocketsCount);
            radarStats.set(0, radarStats.get(0)); // триггер для TableView
        });

        radarButton.setOnAction(e -> {
            radarOn.set(!radarOn.get());

            if (radarOn.get()) {
                radarButton.setText("РАДАР: ВКЛ");
                radarButton.setStyle("-fx-background-color: darkgreen; -fx-text-fill: lime;");
            } else {
                radarButton.setText("РАДАР: ВЫКЛ");
                radarButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
            }
        });

        sector.opacityProperty().bind(
                Bindings.when(radarOn)
                        .then(0.25)
                        .otherwise(0.05)
        );

        ObservableList<RadarStat> radarStats = FXCollections.observableArrayList();
        radarStats.add(new RadarStat("Ракет в запасе", "5"));
        statsButton.setOnAction(e -> showRadarStatsWindow(radarStats));

        for (int i = 0; i < enemyRocketsCount; i++) {
            missiles.add(spawnMissile(missileLayer, sector));
        }
        rocketsLabel.setText("Ракет в запасе: " + rocketsCount);

        stage.setTitle("Панель ЗРК — сектор стрельбы");
        stage.setScene(scene);
        stage.show();


    }

    private AntiRadiationMissile spawnMissile(Pane missileLayer, Arc sector) {

        double angle = sector.getStartAngle()
                + Math.random() * sector.getLength();

        double x = sector.getCenterX()
                + sector.getRadiusX() * Math.cos(Math.toRadians(angle));

        double y = sector.getCenterY()
                - sector.getRadiusY() * Math.sin(Math.toRadians(angle));

        Image img = new Image(
                getClass().getResourceAsStream("/arm.png")
        );

        ImageView view = new ImageView(img);
        view.setFitWidth(28);
        view.setPreserveRatio(true);

        AntiRadiationMissile m = new AntiRadiationMissile(x, y, view);
        missileLayer.getChildren().add(view);
        return m;
    }

    private InterceptorMissile spawnInterceptor(
            Pane interceptorLayer,
            Arc sector,
            AntiRadiationMissile target) {

        double x = sector.getCenterX();
        double y = sector.getCenterY();

        Image img = new Image(
                Objects.requireNonNull(
                        getClass().getResource("/rocket.png")
                ).toExternalForm()
        );

        ImageView view = new ImageView(img);
        view.setFitWidth(24);
        view.setPreserveRatio(true);

        InterceptorMissile m =
                new InterceptorMissile(x, y, view, target);

        interceptorLayer.getChildren().add(view);
        return m;
    }

    private void showRadarStatsWindow(ObservableList<RadarStat> radarStats) {
        // новый Stage (окно)
        Stage statsStage = new Stage();
        statsStage.setTitle("Характеристики радара");

        // TableView
        TableView<RadarStat> radarTable = new TableView<>();
        radarTable.setPrefWidth(300);
        radarTable.setPrefHeight(200);

        // колонки
        TableColumn<RadarStat, String> nameCol = new TableColumn<>("Характеристика");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(180);

        TableColumn<RadarStat, String> valueCol = new TableColumn<>("Значение");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.setPrefWidth(100);

        radarTable.getColumns().addAll(nameCol, valueCol);
        radarTable.setItems(radarStats);



        // сцена
        Scene scene = new Scene(radarTable);
        statsStage.setScene(scene);
        statsStage.show();
    }

    private Pair<Integer, Integer> showAttackSetupDialog() {
        // создаем диалог
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Настройка атаки");
        dialog.setHeaderText("Введите количество ракет");

        // кнопки ОК / Отмена
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // GridPane для ввода
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField ourRocketsField = new TextField("5");
        TextField enemyRocketsField = new TextField("5");

        grid.add(new javafx.scene.control.Label("Своих ракет:"), 0, 0);
        grid.add(ourRocketsField, 1, 0);
        grid.add(new javafx.scene.control.Label("Вражеских ракет:"), 0, 1);
        grid.add(enemyRocketsField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // возвращаем значения при нажатии OK
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    int ourRockets = Integer.parseInt(ourRocketsField.getText());
                    int enemyRockets = Integer.parseInt(enemyRocketsField.getText());
                    return new Pair<>(ourRockets, enemyRockets);
                } catch (NumberFormatException e) {
                    return new Pair<>(5, 5); // значения по умолчанию
                }
            }
            return null;
        });

        // показываем диалог и ждем ввода
        Pair<Integer, Integer> result = dialog.showAndWait().orElse(new Pair<>(5,5));
        return result;
    }

    public static void main(String[] args) {
        launch(args);
    }
}