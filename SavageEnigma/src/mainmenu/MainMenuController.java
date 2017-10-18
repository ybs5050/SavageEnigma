/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package mainmenu;

import cipher.CaesarCipher;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import database.Log;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import ocr.Tesseract;
import utility.Alerts;

/**
 * FXML Controller Class
 * @author Youngmin
 */
public class MainMenuController implements Initializable {

    @FXML
    private JFXButton mainMenu_ocr;
    @FXML
    private TableView<Log> mainMenu_log;
    
    private ObservableList<Log> logList;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXButton mainMenu_decryptText;
    @FXML
    private JFXButton mainMenu_exit;
    @FXML
    private JFXButton mainMenu_deleteLog;
    
    /**
     * Initializes the controller class
     * @param location given/none by default
     * @param resources given/none by default
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Curret user ID: " + database.Users.UserHandler.userId);
        // Configure TableView
        // Used example codes from https://examples.javacodegeeks.com/desktop-java/javafx/tableview/javafx-tableview-example/
        TableColumn encryptedText = new TableColumn("Encrypted Text");
        TableColumn decryptedText = new TableColumn("Decrypted Text");
        mainMenu_log.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        encryptedText.setCellValueFactory(
                new PropertyValueFactory<Log, String>("encryptedText")
        );
        decryptedText.setCellValueFactory(
                new PropertyValueFactory<Log, String>("decryptedText")
        );
        mainMenu_log.getColumns().setAll(encryptedText, decryptedText);
        try {
            getAllLogs();
        } catch (SQLException ex) {
            System.out.println("Error occured: " + ex.toString());
        }
        
    }
    
    /**
     * Calls getAllLogs() function in Database.DatabaseHandler
     * @throws SQLException 
     */
    private void getAllLogs() throws SQLException {
        logList = database.Database.DatabaseHandler.getAllLogs();
        mainMenu_log.setItems(logList);
    }
    
    /**
     * Let the user select a png file and call OCR function
     * @param event 
     */
    @FXML
    private void mainMenu_doOCR(ActionEvent event) throws IOException {
        // used examples from http://code.makery.ch/blog/javafx-dialogs-official/
        Tesseract tes = new Tesseract();
        boolean result = tes.TesseractOCR();
        // Get scene global position
        Bounds bounds = anchorPane.localToScreen(anchorPane.getBoundsInLocal()); 
        if (result) {
            try {
                getAllLogs();
                // Show confirmation window
                Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Text Parse Success", "Parsed Text has been added to the database", "Okay", null);
                // Set window x,y coordinate and show alert window
                tempDialog.getKey().setX(bounds.getMinX());
                tempDialog.getKey().setY(bounds.getMinY());
                tempDialog.getKey().show();
                tempDialog.getValue().show();
            } catch (SQLException ex) {
                System.out.println("Error occured: " + ex.toString());
            }
        }
    }
    
    /**
     * Create a CaesarCipher object and attempt to decrypt the text
     * @param event 
     */
    @FXML
    private void mainMenu_decryptText(ActionEvent event) {
        Bounds bounds = anchorPane.localToScreen(anchorPane.getBoundsInLocal()); 
        // Table view is empty or user did not select any row
        if (mainMenu_log.getItems().isEmpty() || mainMenu_log.getSelectionModel().isEmpty()) {
            // Show confirmation window
            Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Error", "Please select a row in the log table to proceed", "Okay", null);
            // Set window x,y coordinate and show alert window
            tempDialog.getKey().setX((bounds.getMinX()));
            tempDialog.getKey().setY(bounds.getMinY());
            tempDialog.getKey().show();
            tempDialog.getValue().show();
        } else {
            
            String logId = Integer.toString(mainMenu_log.getSelectionModel().getSelectedItem().getLogId());
            String encryptedText = mainMenu_log.getSelectionModel().getSelectedItem().getEncryptedText();
            // Decrypt text
            CaesarCipher cs = new CaesarCipher(encryptedText);
            cs.decryptCipher();
            List<String> choices = cs.getDecryptedTextList();
            // Show new stage for decrypted text selection
            StackPane stackPane1 = new StackPane();
            Stage stage = new Stage();
            stage.setScene(new Scene(stackPane1, 300, 100));
            stage.setTitle("Select cipher text");
            // JFOENIX combo box
            JFXComboBox<Label> jfxCombo = new JFXComboBox<>();
            jfxCombo.setUnFocusColor(Color.valueOf("#ff2f0b"));
            jfxCombo.setFocusColor(Color.valueOf("#ff2f0b"));
            jfxCombo.setPromptText("Select decrypted cipher text");
            choices.forEach((decryptedText) -> {
                jfxCombo.getItems().add(new Label(decryptedText));
            });
            // JFOENIX Dialog window
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(jfxCombo);
            // Buttons
            JFXButton okayButton = new JFXButton("Okay");
            okayButton.setButtonType(ButtonType.RAISED);
            okayButton.setRipplerFill(Color.valueOf("000000"));
            JFXButton cancelButton = new JFXButton("Cancel");
            cancelButton.setButtonType(ButtonType.RAISED);
            cancelButton.setRipplerFill(Color.valueOf("000000"));
            // Add button actions
            okayButton.setOnAction((ActionEvent event1) -> {
                // Add selection to database
                if(jfxCombo.getSelectionModel().isEmpty()) {
                    // If nothing is selected and user pressed okay button
                    Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Error", "Please select a row in the log table to proceed", "Okay", null);
                    tempDialog.getKey().show();
                    tempDialog.getValue().show();
                } else {
                    // Update to database
                    String decryptedText = jfxCombo.getSelectionModel().getSelectedItem().getText();
                    System.out.println("User Selected Decrypted Text:" + decryptedText);
                    try {
                        database.Database.DatabaseHandler.updateLog(logId, decryptedText);
                        getAllLogs();
                        Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Success", "Selected decrypted text \nhas been updated to the database", "Okay", null);
                        tempDialog.getKey().show();
                        tempDialog.getValue().show();
                        stage.close();
                    } catch (SQLException ex) {
                        System.out.println("Error occured: " + ex.toString());
                        Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Error", "Please try again", "Okay", null);
                        tempDialog.getKey().show();
                        tempDialog.getValue().show();
                    }
                }
            });
            cancelButton.setOnAction((ActionEvent event1) -> {
                // Closestage
                stage.close();
            });
            // Add content to dialog
            content.setActions(okayButton, cancelButton);
            JFXDialog dialog = new JFXDialog(stackPane1, content, JFXDialog.DialogTransition.CENTER); 
            // Show dialog and stage
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.resizableProperty().set(false);
            stage.show();
            dialog.show();
        }
    }
    
    /**
     * Attempts to delete user selected log(row)
     * @param event
     * @throws SQLException 
     */
    @FXML
    private void mainMenu_deleteLog(ActionEvent event) throws SQLException {
        Bounds bounds = anchorPane.localToScreen(anchorPane.getBoundsInLocal()); 
        // Table view is empty or user did not select any row
        if (mainMenu_log.getItems().isEmpty() || mainMenu_log.getSelectionModel().isEmpty()) {
            // Show confirmation window
            Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Error", "Please select a row in the log table to proceed", "Okay", null);
            // Set window x,y coordinate and show alert window
            tempDialog.getKey().setX((bounds.getMinX()));
            tempDialog.getKey().setY(bounds.getMinY());
            tempDialog.getKey().show();
            tempDialog.getValue().show();
        } else {
            // Delete log
            String logId = Integer.toString(mainMenu_log.getSelectionModel().getSelectedItem().getLogId());
            if (database.Database.DatabaseHandler.deleteLog(logId)) {
                // Delete Sucess
                Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Success", "Log deleted", "Okay", null);
                // Set window x,y coordinate and show alert window
                tempDialog.getKey().setX((bounds.getMinX()));
                tempDialog.getKey().setY(bounds.getMinY());
                tempDialog.getKey().show();
                tempDialog.getValue().show();
                // Refresh table
                getAllLogs();
            } else {
                // Delete Failed
                Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Error", "Log delete failed \nPlease try again", "Okay", null);
                // Set window x,y coordinate and show alert window
                tempDialog.getKey().setX((bounds.getMinX()));
                tempDialog.getKey().setY(bounds.getMinY());
                tempDialog.getKey().show();
                tempDialog.getValue().show();
            }
        }
    }
    
    /**
     * Exit program
     * @param event 
     */
    @FXML
    private void mainMenu_exit(ActionEvent event) {
        System.out.println("Exiting Savage Enigma....");
        Platform.exit();
    }
 
}
