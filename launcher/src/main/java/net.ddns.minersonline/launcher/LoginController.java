package net.ddns.minersonline.launcher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import net.ddns.minersonline.shared.AuthClient;
import net.ddns.minersonline.shared.AuthException;
import net.ddns.minersonline.shared.json.AuthenticateResponse;

public class LoginController {
    @FXML private Text signInButton;
    @FXML private PasswordField passwordField;
    @FXML private TextField usernameField;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
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
        if(user != null){
            Main.user = user;
            signInButton.setText(user.getSelectedProfile().getName());
        }

    }

}