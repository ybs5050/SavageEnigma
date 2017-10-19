/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
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
import javafx.stage.Stage;
import javafx.util.Pair;
import utility.Alerts;
import database.Users;
import java.security.NoSuchAlgorithmException;
import utility.Stages;


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
    private JFXPasswordField login_passWordTextField;
    @FXML
    private JFXButton login_exitBtn;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXButton login_registerBtn;
    
    /**
     * Initializes the controller class
     * @param location given/none by default
     * @param resources given/none by default
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Request focus to the username text field
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
    
    /**
     * Authenticates a user
     * @param event 
     */
    @FXML
    private void login_authenticateUser(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        
        String username = login_userNameTextField.getText().trim();
        String password = login_passWordTextField.getText().trim();
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(1, "Your password does not match the records \nPlease try again");
        errorMap.put(2, "Your username does not exist in the database \nPlease try again");
        errorMap.put(3, "Failed to establish a connection to the database \nPlease try again");
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
            login_userNameTextField.clear();
            login_passWordTextField.clear();
            login_userNameTextField.requestFocus();
            
        } else {
            try {
                int authResult = Users.UserHandler.authenticateUser(username, password);
                if (authResult == 0) {
                    Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Login Success", "Welcome, " + username, "Okay", new Pair("/mainmenu/MainMenu.fxml", "Savage Enigma - Main Menu"));
                    // Set window x,y coordinate and show alert window
                    tempDialog.getKey().setX(bounds.getMinX());
                    tempDialog.getKey().setY(bounds.getMinY());
                    tempDialog.getKey().show();
                    tempDialog.getValue().show();
                    Stage stage = (Stage) anchorPane.getScene().getWindow();
                    stage.close();
                } else {
                    Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Login Failed", errorMap.get(authResult), "Okay", null);
                    // Set window x,y coordinate and show alert window
                    tempDialog.getKey().setX(bounds.getMinX());
                    tempDialog.getKey().setY(bounds.getMinY());
                    tempDialog.getKey().show();
                    tempDialog.getValue().show();
                    login_userNameTextField.clear();
                    login_passWordTextField.clear();
                    login_userNameTextField.requestFocus();
                }
            } catch (SQLException except) {
                System.out.println("Error occured: " + except.toString());
            }
        }      
    }
    
    /**
     * Displays a user registration stage
     * @param event 
     */
    @FXML
    private void login_registerUser(ActionEvent event) throws IOException {
        // Get scene global position
        Bounds bounds = anchorPane.localToScreen(anchorPane.getBoundsInLocal());
        // Create and display registration stage
        Stage base = Stages.StageHandler.createNewStage("/login/Register.fxml", "Savage Enigma - Create Account");
        base.setX(bounds.getMinX());
        base.setY(bounds.getMinY());
        base.setResizable(false);
        base.show();
    }  
}
