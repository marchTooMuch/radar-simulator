package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class FirstTable {
    public static int rocketsCount;
    public static ObservableList<RadarStat> radarStats = FXCollections.observableArrayList();
    public static Pair<Integer, Integer> setup;


    public static void createTable1() {
        TextInputDialog dialog = new TextInputDialog("5");
        dialog.setTitle("Настройка ракет");
        dialog.setHeaderText("Введите количество ракет на вооружении:");
        dialog.setContentText("Количество ракет:");
        setup = showAttackSetupDialog();
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

    }

    private static Pair<Integer, Integer> showAttackSetupDialog() {
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
}
