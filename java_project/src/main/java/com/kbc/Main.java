package com.kbc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.showSplash();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


