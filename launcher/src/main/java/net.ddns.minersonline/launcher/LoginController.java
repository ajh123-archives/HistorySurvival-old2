package net.ddns.minersonline.launcher;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import net.ddns.minersonline.shared.AuthClient;
import net.ddns.minersonline.shared.AuthException;
import net.ddns.minersonline.shared.TokenPair;
import net.ddns.minersonline.shared.json.AuthenticateResponse;

import java.io.IOException;

public class LoginController {
    @FXML private Text signInButton;
    @FXML private PasswordField passwordField;
    @FXML private TextField usernameField;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws IOException {
        Gson gson = new Gson();

        signInButton.setText("Sign in button pressed");
        String password = passwordField.getText();
        String username = usernameField.getText();
        AuthenticateResponse user = null;
        try {
            user = AuthClient.authenticate(username, password, Main.clientToken);
        }catch (AuthException e){
            signInButton.setText(e.getMessage());
        }catch (Exception e){
            Main.LOGGER.trace(e);
        }
        if(user != null && user.user != null){
            Main.user = user;
            Main.user.user.password = password;

            TokenPair pair = new TokenPair();
            pair.accessToken = Main.user.getAccessToken();
            pair.clientToken = Main.user.getClientToken();
            String json = gson.toJson(pair);

            Parent login = FXMLLoader.load(this.getClass().getResource("/user.fxml"));
            Scene scene = new Scene(login, 640, 480);

            Main.writeFile("userCache.json", json);

            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }

    }

}