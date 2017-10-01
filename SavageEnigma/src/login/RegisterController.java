/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import database.Users;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import utility.Alerts;

/**
 * FXML Controller class 
 * @author Youngmin
 */
public class RegisterController implements Initializable{

    @FXML
    private JFXPasswordField register_passWordTextField;
    @FXML
    private JFXTextField register_userNameTextField;
    @FXML
    private JFXButton register_registerBtn;
    @FXML
    private JFXButton register_cancelBtn;
    @FXML
    private AnchorPane anchorPane;
    
    /**
     * Initializes the controller class
     * @param location
     * @param resources 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Request focus to username text field
        Platform.runLater(new Runnable() {
        @Override
            public void run() {
                register_userNameTextField.requestFocus();
            }
        });
    }
    
    /**
     * Closes the account registration window
     * @param event 
     */
    @FXML
    private void register_cancelRegistration(ActionEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Creates an account
     * @param event 
     */
    @FXML
    private void register_createAccount(ActionEvent event) throws SQLException {
        String username = register_userNameTextField.getText().trim();
        String password = register_passWordTextField.getText().trim();
        // Get scene global position
        Bounds bounds = anchorPane.localToScreen(anchorPane.getBoundsInLocal()); 
        // Check if username and password field is empty and username is alphanumeric
        if (username.isEmpty() || password.isEmpty() || !username.matches("[A-Za-z0-9]+")) {
            Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Error", "Please check the username and password field", "Okay", null);
            // Set window x,y coordinate
            tempDialog.getKey().setX(bounds.getMinX());
            tempDialog.getKey().setY(bounds.getMinY());
            tempDialog.getKey().show();
            tempDialog.getValue().show();
        } else {
            try {
                int registerResult = Users.UserHandler.registerUser(username, password);
                if (registerResult == 0) {
                    Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Registration Success", "Account created!"+ "\nUsername: " + username + "\nPassword: " + password, "Okay", null);
                    // Set window x,y coordinate
                    tempDialog.getKey().setX(bounds.getMinX());
                    tempDialog.getKey().setY(bounds.getMinY());
                    tempDialog.getKey().show();
                    tempDialog.getValue().show();
                    Stage stage = (Stage) anchorPane.getScene().getWindow();
                    stage.close();
                } else {
                    Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Registration Failed", "Username already exists \nPlease try a different username", "Okay", null);
                    // Set window x,y coordinate
                    tempDialog.getKey().setX(bounds.getMinX());
                    tempDialog.getKey().setY(bounds.getMinY());
                    tempDialog.getKey().show();
                    tempDialog.getValue().show();
                    register_passWordTextField.clear();
                    register_userNameTextField.clear();
                    register_userNameTextField.requestFocus();
                }
            } catch (SQLException except) {
                System.out.println("Error occured: " + except.toString());
            }   
        }
    }
    
}
