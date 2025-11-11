package com.kbc.ui;

import com.kbc.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SplashView {
    private final Scene scene;

    public SplashView(SceneManager sceneManager) {
        Label title = new Label("\uD83C\uDF89 Welcome to KBC!");
        title.getStyleClass().add("title-logo");

        Button loginBtn = new Button("Login");
        Button signupBtn = new Button("Sign Up");
        Button exitBtn = new Button("Exit");

        loginBtn.setOnAction(e -> sceneManager.showLoginSignup());
        signupBtn.setOnAction(e -> sceneManager.showLoginSignup());
        exitBtn.setOnAction(e -> System.exit(0));

        HBox actions = new HBox(12, loginBtn, signupBtn, exitBtn);
        actions.setAlignment(Pos.CENTER);

        VBox root = new VBox(16, title, actions);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.getStyleClass().add("splash");

        this.scene = new Scene(root, 800, 500);
        this.scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    public Scene getScene() { return scene; }
}


