package net.ddns.minersonline.launcher;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class LogController implements Initializable {
    @FXML private ListView<String> log;
    private InputStream outputStream;

    private void getLogs(){
        Object stream = log.getScene().getUserData();
        if (stream != null){
            outputStream = (InputStream) stream;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.getItems().clear();
        Timeline logger = new Timeline(
                new KeyFrame(Duration.seconds(0.001), event -> {
                    if(outputStream != null){
                        ObservableList<String> items = log.getItems();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(outputStream));
                        try {
                            if(reader.ready()) {
                                items.add(reader.readLine());
                            }
                        } catch (IOException e) {
                            Main.LOGGER.trace(e);
                        }
                    } else {
                        getLogs();
                    }
                }));
        logger.setCycleCount(Timeline.INDEFINITE);
        logger.play();
    }
}
