package net.ddns.minersonline.launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.primaryStage = stage;
        Parent login = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/main.fxml")));
        Scene scene = new Scene(login, 400, 275);

        stage.setTitle("MinersOnline Launcher");
        stage.setScene(scene);
        stage.show();
    }
}
