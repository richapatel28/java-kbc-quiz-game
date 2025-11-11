package com.kbc.ui;

import com.kbc.SceneManager;
import com.kbc.auth.Authenticator;
import com.kbc.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LoginSignupView {
    private final Scene scene;

    public LoginSignupView(SceneManager sceneManager, Authenticator authenticator) {
        TabPane tabs = new TabPane();

        // Login Tab
        GridPane loginGrid = new GridPane();
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(20));
        TextField loginUser = new TextField();
        PasswordField loginPass = new PasswordField();
        Label loginMsg = new Label();
        Button loginBtn = new Button("Login");
        loginGrid.addRow(0, new Label("Username"), loginUser);
        loginGrid.addRow(1, new Label("Password"), loginPass);
        loginGrid.addRow(2, loginBtn, loginMsg);
        loginBtn.setOnAction(e -> {
            User user = authenticator.login(loginUser.getText(), loginPass.getText());
            if (user != null) {
                sceneManager.showDashboard(user);
            } else {
                loginMsg.setText("Invalid credentials");
                loginMsg.getStyleClass().add("error-text");
            }
        });
        Tab loginTab = new Tab("Login", loginGrid);
        loginTab.setClosable(false);

        // Signup Tab
        GridPane signupGrid = new GridPane();
        signupGrid.setHgap(10);
        signupGrid.setVgap(10);
        signupGrid.setPadding(new Insets(20));
        TextField suUser = new TextField();
        PasswordField suPass = new PasswordField();
        PasswordField suPass2 = new PasswordField();
        Label suMsg = new Label();
        Button suBtn = new Button("Sign Up");
        signupGrid.addRow(0, new Label("Username"), suUser);
        signupGrid.addRow(1, new Label("Password"), suPass);
        signupGrid.addRow(2, new Label("Confirm Password"), suPass2);
        signupGrid.addRow(3, suBtn, suMsg);
        suBtn.setOnAction(e -> {
            if (!suPass.getText().equals(suPass2.getText())) {
                suMsg.setText("Passwords do not match");
                suMsg.getStyleClass().add("error-text");
                return;
            }
            boolean ok = authenticator.signup(suUser.getText(), suPass.getText());
            if (ok) {
                suMsg.setText("Signup successful. Please login.");
                suMsg.getStyleClass().remove("error-text");
            } else {
                suMsg.setText("Signup failed (username exists?)");
                suMsg.getStyleClass().add("error-text");
            }
        });
        Tab signupTab = new Tab("Sign Up", signupGrid);
        signupTab.setClosable(false);

        tabs.getTabs().addAll(loginTab, signupTab);

        Label header = new Label("Login / Sign Up");
        header.getStyleClass().add("section-title");
        VBox root = new VBox(10, header, tabs);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        this.scene = new Scene(root, 800, 500);
        this.scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    public Scene getScene() { return scene; }
}


