package com.kbc.ui;

import com.kbc.SceneManager;
import com.kbc.auth.Authenticator;
import com.kbc.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class LeaderboardView {
    private final Scene scene;

    public static class Row {
        private final String username;
        private final Integer highscore;
        public Row(String username, Integer highscore) { this.username = username; this.highscore = highscore; }
        public String getUsername() { return username; }
        public Integer getHighscore() { return highscore; }
    }

    public LeaderboardView(SceneManager sceneManager, Authenticator authenticator, User user) {
        Text title = new Text("🏆 Leaderboard");
        title.getStyleClass().add("section-title");

        TableView<Row> table = new TableView<>();
        TableColumn<Row, String> colUser = new TableColumn<>("Username");
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<Row, Integer> colScore = new TableColumn<>("High Score");
        colScore.setCellValueFactory(new PropertyValueFactory<>("highscore"));
        table.getColumns().addAll(colUser, colScore);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        ObservableList<Row> rows = FXCollections.observableArrayList(
                authenticator.getTopUsers(10).stream()
                        .map(u -> new Row(u.username(), u.highscore()))
                        .toList()
        );
        table.setItems(rows);

        Button back = new Button("Back");
        back.setOnAction(e -> sceneManager.showDashboard(user));
        HBox actions = new HBox(back);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(8, 0, 0, 0));

        BorderPane root = new BorderPane();
        root.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setCenter(table);
        root.setBottom(actions);
        BorderPane.setMargin(table, new Insets(12));
        BorderPane.setMargin(title, new Insets(12));
        BorderPane.setMargin(actions, new Insets(12));

        this.scene = new Scene(root, 700, 500);
        this.scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    public Scene getScene() { return scene; }
}


