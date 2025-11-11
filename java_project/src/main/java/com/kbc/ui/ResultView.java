package com.kbc.ui;

import com.kbc.SceneManager;
import com.kbc.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ResultView {
    private final Scene scene;

    public ResultView(SceneManager sceneManager, User user, int finalWinnings, int correctAnswers, boolean quit) {
        String titleText = quit ? "You Quit!" : (correctAnswers > 0 ? "Congratulations!" : "Better luck next time!");
        Label title = new Label(titleText);
        title.getStyleClass().add("section-title");
        Label details = new Label("Final Winnings: \u20B9" + finalWinnings + "\nCorrect Answers: " + correctAnswers);

        Button playAgain = new Button("Play Again");
        Button goDashboard = new Button("Go to Dashboard");
        Button exit = new Button("Exit");

        playAgain.setOnAction(e -> sceneManager.startGame(user));
        goDashboard.setOnAction(e -> sceneManager.showDashboard(user));
        exit.setOnAction(e -> System.exit(0));

        VBox root = new VBox(12, title, details, playAgain, goDashboard, exit);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.TOP_CENTER);
        this.scene = new Scene(root, 800, 500);
        this.scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    public Scene getScene() { return scene; }
}


