/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller Class
 * @author Youngmin
 */
public class LoginController implements Initializable{

    @FXML
    private JFXButton login_LoginBtn;
    @FXML
    private JFXTextField login_userNameTextField;
    @FXML
    private JFXTextField login_passWordTextField;
    @FXML
    private JFXButton login_registerBtn;
    @FXML
    private JFXButton login_exitBtn;
    
    /**
     * Initializes the controller class
     * @param location given/none by default
     * @param resources given/none by default
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Request focus to Login text field
        Platform.runLater(new Runnable() {
        @Override
            public void run() {
                login_userNameTextField.requestFocus();
            }
        });
    }
    
}
