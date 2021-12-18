package net.ddns.minersonline.launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent login = FXMLLoader.load(this.getClass().getResource("/main.fxml"));
        Scene scene = new Scene(login, 400, 275);

        stage.setTitle("MinersOnline Launcher");
        stage.setScene(scene);
        stage.show();
    }
}
