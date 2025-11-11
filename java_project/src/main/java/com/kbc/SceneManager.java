package com.kbc;

import com.kbc.auth.Authenticator;
import com.kbc.model.User;
import com.kbc.ui.DashboardView;
import com.kbc.ui.GameView;
import com.kbc.ui.LoginSignupView;
import com.kbc.ui.ResultView;
import com.kbc.ui.SplashView;
import com.kbc.ui.LeaderboardView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private final Authenticator authenticator;

    public SceneManager(Stage stage) {
        this.stage = stage;
        this.authenticator = new Authenticator();
        stage.setTitle("KBC - Kaun Banega Crorepati");
    }

    public void showSplash() {
        SplashView view = new SplashView(this);
        setScene(view.getScene());
    }

    public void showLoginSignup() {
        LoginSignupView view = new LoginSignupView(this, authenticator);
        setScene(view.getScene());
    }

    public void showDashboard(User user) {
        DashboardView view = new DashboardView(this, user);
        setScene(view.getScene());
    }

    public void startGame(User user) {
        GameView view = new GameView(this, user);
        setScene(view.getScene());
    }

    public void showResult(User user, int finalWinnings, int correctAnswers, boolean quit) {
        ResultView view = new ResultView(this, user, finalWinnings, correctAnswers, quit);
        setScene(view.getScene());
    }

    public void showLeaderboard(User user) {
        LeaderboardView view = new LeaderboardView(this, authenticator, user);
        setScene(view.getScene());
    }

    private void setScene(Scene scene) {
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
}


