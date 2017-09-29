/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

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
    @FXML
    private AnchorPane anchorPane;
    
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
    
    /**
     * Show exit prompt to the user and exit the program if confirmed
     * @param event
     * @throws IOException 
     */
    @FXML
    private void login_exitProgram(ActionEvent event) throws IOException {
        
        // Get scene global position
        Bounds bounds = anchorPane.localToScreen(anchorPane.getBoundsInLocal());        

        // Set location relative to the scene and show alert
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setX(bounds.getMinX());
        alert.setY(bounds.getMinY());
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Exit Savage Enigma?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Platform.exit();
        }   
    }
    
}
