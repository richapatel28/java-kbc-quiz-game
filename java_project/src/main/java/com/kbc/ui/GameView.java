package com.kbc.ui;

import com.kbc.SceneManager;
import com.kbc.auth.Authenticator;
import com.kbc.game.GameController;
import com.kbc.model.Question;
import com.kbc.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import com.kbc.model.QuestionBank;

public class GameView {
    private final Scene scene;
    private final SceneManager sceneManager;
    private final GameController controller;
    private final User user;

    private final Label questionLabel = new Label();
    private final ToggleGroup optionsGroup = new ToggleGroup();
    private final RadioButton optA = new RadioButton();
    private final RadioButton optB = new RadioButton();
    private final RadioButton optC = new RadioButton();
    private final RadioButton optD = new RadioButton();
    private final Label status = new Label();
    private final Label prizeInfo = new Label();
    private final Label timerLabel = new Label();
    private Timeline timer;
    private int secondsLeft;
    private VBox ladderBox;

    public GameView(SceneManager sceneManager, User user) {
        this.sceneManager = sceneManager;
        this.user = user;
        this.controller = new GameController(user, new Authenticator());

        Label header = new Label("KBC - Game On!");
        header.getStyleClass().add("section-title");

        optA.setToggleGroup(optionsGroup);
        optB.setToggleGroup(optionsGroup);
        optC.setToggleGroup(optionsGroup);
        optD.setToggleGroup(optionsGroup);

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(12);
        grid.add(questionLabel, 0, 0, 2, 1);
        grid.addRow(1, optA, optB);
        grid.addRow(2, optC, optD);

        Button fifty = new Button("50-50");
        Button audience = new Button("Audience Poll");
        Button submit = new Button("Confirm");
        Button quit = new Button("Quit");
        HBox lifelines = new HBox(10, fifty, audience);
        HBox actions = new HBox(10, submit, quit);
        lifelines.setAlignment(Pos.CENTER_LEFT);
        actions.setAlignment(Pos.CENTER_RIGHT);

        fifty.setOnAction(e -> {
            List<String> toRemove = controller.use5050();
            for (String s : toRemove) {
                switch (s) {
                    case "A": optA.setDisable(true); break;
                    case "B": optB.setDisable(true); break;
                    case "C": optC.setDisable(true); break;
                    case "D": optD.setDisable(true); break;
                }
            }
            fifty.setDisable(true);
        });

        audience.setOnAction(e -> {
            int[] poll = controller.useAudiencePoll();
            showAudienceChart(poll);
            audience.setDisable(true);
        });

        submit.setOnAction(e -> {
            Toggle selected = optionsGroup.getSelectedToggle();
            if (selected == null) {
                status.setText("Please select an option.");
                status.getStyleClass().add("error-text");
                return;
            }
            String letter = selected == optA ? "A" : selected == optB ? "B" : selected == optC ? "C" : "D";
            boolean correct = controller.submitAnswer(letter);
            if (correct) {
                status.setText("Correct! Moving on...");
                loadNextQuestion();
            } else {
                controller.persistHighScoreIfNeeded();
                sceneManager.showResult(user, controller.getCurrentEarnings(), controller.getCorrectCount(), false);
            }
        });

        quit.setOnAction(e -> {
            controller.quitGame();
            controller.persistHighScoreIfNeeded();
            sceneManager.showResult(user, controller.getCurrentEarnings(), controller.getCorrectCount(), true);
        });

        timerLabel.getStyleClass().add("timer");
        VBox bottom = new VBox(8, new HBox(12, prizeInfo, timerLabel), new HBox(12, lifelines, actions), status);
        bottom.setPadding(new Insets(10, 0, 0, 0));

        BorderPane root = new BorderPane();
        root.setTop(header);
        BorderPane.setAlignment(header, Pos.CENTER);
        root.setCenter(grid);
        root.setRight(buildPrizeLadder());
        root.setBottom(bottom);
        BorderPane.setMargin(grid, new Insets(12));
        BorderPane.setMargin(bottom, new Insets(12));

        this.scene = new Scene(root, 900, 560);
        this.scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        loadNextQuestion();
    }

    private void loadNextQuestion() {
        Question q = controller.getCurrentQuestion();
        if (q == null) {
            controller.persistHighScoreIfNeeded();
            sceneManager.showResult(user, controller.getCurrentEarnings(), controller.getCorrectCount(), true);
            return;
        }
        questionLabel.setText("Q" + q.getLevel() + " (\u20B9" + q.getPrize() + ")\n" + q.getText());
        optA.setText("A) " + q.getOptionA());
        optB.setText("B) " + q.getOptionB());
        optC.setText("C) " + q.getOptionC());
        optD.setText("D) " + q.getOptionD());
        optionsGroup.selectToggle(null);
        optA.setDisable(false);
        optB.setDisable(false);
        optC.setDisable(false);
        optD.setDisable(false);
        status.setText("");
        prizeInfo.setText("Current Earnings: \u20B9" + controller.getCurrentEarnings());
        highlightLadder(q.getLevel());
        startTimer(30);
    }

    private void showAudienceChart(int[] poll) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 100, 10);
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Audience Poll (%)");
        xAxis.setLabel("Options");
        yAxis.setLabel("% Votes");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("A", poll[0]));
        series.getData().add(new XYChart.Data<>("B", poll[1]));
        series.getData().add(new XYChart.Data<>("C", poll[2]));
        series.getData().add(new XYChart.Data<>("D", poll[3]));
        chart.getData().add(series);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Audience Poll");
        dialog.getDialogPane().setContent(chart);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    public Scene getScene() { return scene; }

    private VBox buildPrizeLadder() {
        ladderBox = new VBox(4);
        ladderBox.getStyleClass().add("ladder");
        QuestionBank qb = new QuestionBank();
        // Highest prize at top like KBC
        java.util.List<com.kbc.model.Question> all = new java.util.ArrayList<>(qb.getQuestions());
        java.util.Collections.reverse(all);
        for (com.kbc.model.Question q : all) {
            Label row = new Label(String.format("%2d: \u20B9%d", q.getLevel(), q.getPrize()));
            row.getStyleClass().add("ladder-row");
            ladderBox.getChildren().add(row);
        }
        VBox wrapper = new VBox(ladderBox);
        wrapper.setPadding(new Insets(12));
        return wrapper;
    }

    private void highlightLadder(int level) {
        // children are reversed; find matching label text
        for (javafx.scene.Node node : ladderBox.getChildren()) {
            node.getStyleClass().remove("active");
            if (node instanceof Label lbl) {
                if (lbl.getText().startsWith(String.format("%2d:", level))) {
                    node.getStyleClass().add("active");
                }
            }
        }
    }

    private void startTimer(int seconds) {
        stopTimer();
        secondsLeft = seconds;
        timerLabel.setText("⏱ " + secondsLeft + "s");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsLeft--;
            timerLabel.setText("⏱ " + Math.max(0, secondsLeft) + "s");
            if (secondsLeft <= 0) {
                stopTimer();
                controller.persistHighScoreIfNeeded();
                sceneManager.showResult(user, controller.getCurrentEarnings(), controller.getCorrectCount(), false);
            }
        }));
        timer.setCycleCount(seconds);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }
}


