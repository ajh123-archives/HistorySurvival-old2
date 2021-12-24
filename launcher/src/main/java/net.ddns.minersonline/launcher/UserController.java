package net.ddns.minersonline.launcher;

import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.ddns.minersonline.shared.AuthClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class UserController implements Initializable {

    @FXML private SplitMenuButton account;
    @FXML private Button menu;
    @FXML private MenuItem logout;
    @FXML private Button play;
    @FXML private ListView<String> navList;
    @FXML private ListView<String> verList;
    @FXML private AnchorPane navListParent;
    @FXML private Tab newsTab;
    @FXML private Tab versionsTab;
    @FXML private WebView web;
    private LauncherMeta meta = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        account.setText(Main.user.user.username);
        web.setContextMenuEnabled(false);

        try {
            meta = Main.readJsonFromUrl("http://minersonline.ddns.net/gamesdownload/version_manifest_v2.json", LauncherMeta.class);
        } catch (IOException e) {
            Main.LOGGER.trace(e);
        }
        //navList.setItems(FXCollections.observableArrayList("Red","Yellow","Blue"));
        navList.setItems(observableArrayList("All", "HistorySurvival"));
        prepareSlideMenuAnimation();
        logout.setOnAction((ActionEvent evt)->{
            try {
                AuthClient.signOut(Main.user.user.username, Main.user.user.password);
                Parent login = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/main.fxml")));
                Scene scene = new Scene(login, 400, 275);

                Main.primaryStage.setScene(scene);
                Main.primaryStage.show();
            } catch (Exception e) {
                Main.LOGGER.trace(e);
            }
        });

        if (meta != null){
            ObservableList<String> list = observableArrayList();
            for (Game game: meta.games){
                for (Version version: game.versions){
                    list.add(game.name+"\n"+version.id);
                }
            }

            verList.setItems(list);
        }

        web.getEngine().load("http://minersonline.ddns.net/games/All/news/");
        LauncherMeta finalMeta = meta;
        navList.setOnMouseClicked(event -> {
            String game = navList.getSelectionModel().getSelectedItem();
            newsTab.setText(game+" News");
            if(Objects.equals(game, "All")){
                if (finalMeta != null){
                    ObservableList<String> list = observableArrayList();
                    for (Game metaGame: finalMeta.games){
                        for (Version version : metaGame.versions) {
                            list.add(metaGame.name+"\n"+version.id);
                        }
                    }

                    verList.setItems(list);
                }
                versionsTab.setText(game+" Games/Versions");
            } else {
                if (finalMeta != null){
                    ObservableList<String> list = observableArrayList();
                    for (Game metaGame: finalMeta.games){
                        if (Objects.equals(metaGame.name, game)) {
                            for (Version version : metaGame.versions) {
                                list.add(metaGame.name+"\n"+version.id);
                            }
                        }
                    }

                    verList.setItems(list);
                }
                versionsTab.setText(game + " Versions");
            }
            web.getEngine().load("http://minersonline.ddns.net/games/"+game+"/news/");
        });


        play.setOnAction((ActionEvent evt)->{
            Alert alert = new Alert(Alert.AlertType.ERROR, "No game or version selected in side bar", ButtonType.CLOSE);
            alert.showAndWait();
        });
        verList.setOnMouseClicked(event -> {
            String game = verList.getSelectionModel().getSelectedItem();
            if(meta != null && game != null){
                for (Game metaGame: meta.games){
                    String[] name_ver = game.split("\\s+");
                    if(Objects.equals(metaGame.name, name_ver[0])){
                        for(Version ver: metaGame.versions){
                            if(Objects.equals(ver.id, name_ver[1])){
                                play.setOnAction((ActionEvent evt)->{
                                    String local_url = null;
                                    //System.out.println(System.getProperty("os.arch"));
                                    if(System.getProperty("os.name").contains("Mac")){
                                        local_url = ver.url_nativs_macos;
                                    } else if (System.getProperty("os.name").contains("Win")){
                                        local_url = ver.url_nativs_windows;
                                    }
                                    try {
                                        assert local_url != null;
                                        String local_file = Main.readFromUrl(local_url, "versions");

                                        String jvm_location;
                                        if (System.getProperty("os.name").startsWith("Win")) {
                                            jvm_location = System.getProperties().getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe";
                                        } else {
                                            jvm_location = System.getProperties().getProperty("java.home") + File.separator + "bin" + File.separator + "java";
                                        }

                                        String[] client = {jvm_location, "-jar", local_file};
                                        Process proc = new ProcessBuilder(client)
                                                .directory(new File(Main.dataDir).getAbsoluteFile())
                                                .start();

                                        InputStream out = proc.getInputStream();
                                        Stage stage = new Stage();
                                        Parent log = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/log.fxml")));
                                        Scene scene = new Scene(log, 640, 480);
                                        scene.setUserData(out);
                                        stage.setTitle("Game output");
                                        stage.setScene(scene);
                                        stage.show();
                                    } catch (IOException e) {
                                        Main.LOGGER.trace(e);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    private void prepareSlideMenuAnimation() {
        TranslateTransition openNav=new TranslateTransition(new Duration(350), navListParent);
        openNav.setToX(0);
        TranslateTransition closeNav=new TranslateTransition(new Duration(350), navListParent);
        menu.setOnAction((ActionEvent evt)->{
            if(navListParent.getTranslateX()!=0){
                openNav.play();
            }else{
                closeNav.setToX(-(navListParent.getWidth()));
                closeNav.play();
            }
        });
    }
}