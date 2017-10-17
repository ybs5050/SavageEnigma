/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package mainmenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import database.Log;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
    
}
