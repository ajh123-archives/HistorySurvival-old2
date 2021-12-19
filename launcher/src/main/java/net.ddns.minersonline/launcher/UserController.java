package net.ddns.minersonline.launcher;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML private Button menu;
    @FXML private Button logout;
    @FXML private ListView navList;
    @FXML private AnchorPane navListParent;
    @FXML private Tab newsTab;
    @FXML private Tab versionsTab;
    @FXML private WebView web;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //navList.setItems(FXCollections.observableArrayList("Red","Yellow","Blue"));
        navList.setItems(FXCollections.observableArrayList("All", "HistorySurvival"));
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

        web.getEngine().load("http://minersonline.ddns.net/games/All/news/");
        navList.setOnMouseClicked(event -> {
            String game = (String) navList.getSelectionModel().getSelectedItem();
            newsTab.setText(game+" News");
            if(Objects.equals(game, "All")){
                versionsTab.setText(game+" Games/Versions");
            } else {
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