package net.ddns.minersonline.launcher;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import net.ddns.minersonline.shared.AuthClient;
import net.ddns.minersonline.shared.AuthException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class UserController implements Initializable {

    @FXML private Button menu;
    @FXML private Button logout;
    @FXML private ListView navList;
    @FXML private ListView verList;
    @FXML private AnchorPane navListParent;
    @FXML private Tab newsTab;
    @FXML private Tab versionsTab;
    @FXML private WebView web;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LauncherMeta meta = null;
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
                Parent login = FXMLLoader.load(this.getClass().getResource("/main.fxml"));
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
                    list.add(game.name+" "+version.id);
                }
            }

            verList.setItems(list);
        }

        web.getEngine().load("http://minersonline.ddns.net/games/All/news/");
        LauncherMeta finalMeta = meta;
        navList.setOnMouseClicked(event -> {
            String game = (String) navList.getSelectionModel().getSelectedItem();
            newsTab.setText(game+" News");
            if(Objects.equals(game, "All")){
                if (finalMeta != null){
                    ObservableList<String> list = observableArrayList();
                    for (Game metaGame: finalMeta.games){
                        for (Version version : metaGame.versions) {
                            list.add(metaGame.name+" "+version.id);
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
                                list.add(version.id);
                            }
                        }
                    }

                    verList.setItems(list);
                }
                versionsTab.setText(game + " Versions");
            }
            web.getEngine().load("http://minersonline.ddns.net/games/"+game+"/news/");
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