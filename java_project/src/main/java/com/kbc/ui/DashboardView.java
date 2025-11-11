package com.kbc.ui;

import com.kbc.SceneManager;
import com.kbc.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardView {
    private final Scene scene;

    public DashboardView(SceneManager sceneManager, User user) {
        Label welcome = new Label("Welcome, " + user.getUsername() + "!");
        welcome.getStyleClass().add("section-title");
        Label rules = new Label("Answer MCQs to climb the prize ladder. Use lifelines wisely!\n" +
                "Quit anytime to keep your current winnings.");
        rules.getStyleClass().add("muted");

        Button start = new Button("Start New Game");
        Button viewRules = new Button("View Rules");
        Button leaderboard = new Button("Leaderboard");
        Button logout = new Button("Logout");

        start.setOnAction(e -> sceneManager.startGame(user));
        viewRules.setOnAction(e -> rules.setText("1) 4 options per question\n2) 50-50 removes two wrong options\n3) Audience Poll shows a bar chart\n4) Wrong answer ends the game\n5) Quit to keep your money."));
        leaderboard.setOnAction(e -> sceneManager.showLeaderboard(user));
        logout.setOnAction(e -> sceneManager.showLoginSignup());

        VBox root = new VBox(14, welcome, rules, start, viewRules, leaderboard, logout);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(24));
        this.scene = new Scene(root, 800, 500);
        this.scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    public Scene getScene() { return scene; }
}


