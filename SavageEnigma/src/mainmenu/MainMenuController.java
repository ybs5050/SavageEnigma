/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package mainmenu;

import com.jfoenix.controls.JFXButton;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ocr.Tesseract;

/**
 * FXML Controller Class
 * @author Youngmin
 */
public class MainMenuController implements Initializable {

    @FXML
    private JFXButton mainMenu_ocr;
    
    /**
     * Initializes the controller class
     * @param location given/none by default
     * @param resources given/none by default
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Curret user ID: " + database.Users.UserHandler.userId);
    }

    /**
     * Let the user select a png file and call OCR function
     * @param event 
     */
    @FXML
    private void mainMenu_doOCR(ActionEvent event) throws IOException {
        // used examples from http://code.makery.ch/blog/javafx-dialogs-official/
        Tesseract tes = new Tesseract();
        String parsedText = tes.TesseractOCR();
        
        if(parsedText != null) {
            // Allow user to correct parsed text
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Confirm Parsed Text");
            alert.setHeaderText("Make necessary corrections");
            TextArea textArea = new TextArea(parsedText.trim());
            textArea.setPrefColumnCount(60);
            textArea.setPrefRowCount(20);
            textArea.setEditable(true);
            textArea.setWrapText(true);
            alert.getDialogPane().setContent(textArea);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()){
                parsedText = textArea.getText().trim();
                System.out.println("User corrected text: " + parsedText);
            }
            
            // Add parsed text to the database
            
        }
    }
   
}
