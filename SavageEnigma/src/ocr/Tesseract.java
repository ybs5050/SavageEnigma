/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package ocr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.*;
/**
 *
 * @author Youngmin
 */
public class Tesseract {
    
        /**
         * Test image OCR
         * Used examples from http://tess4j.sourceforge.net/tutorial/
         */
        public static void test() {
            File imageFile = new File("images\\TEST.png");
            ITesseract instance = new Tesseract1();
            try {
                String result = instance.doOCR(imageFile);
                System.out.println(result);
            } catch (TesseractException e) {
                System.out.println(e.getMessage());
            }
        }
        
        public boolean TesseractOCR() throws IOException {
            // Used example from https://stackoverflow.com/questions/29338352/create-filechooser-in-fxml
            Stage primaryStage = null;
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PNG files", "*.png");
            fc.getExtensionFilters().add(filter);
            File file = fc.showOpenDialog(primaryStage);
            if(file != null) {
                ITesseract instance = new Tesseract1();
                try {
                    String result = instance.doOCR(file);
                    System.out.println("Parsed text: " + result.trim());
                    boolean confirm = confirmParse(file.getAbsolutePath(), result.trim());
                    return confirm;
                } catch (TesseractException e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            }
            return false;
        }
        
        /**
         * Shows a preview of a user selected image and let the user make corrections to the parsed text
         * @param path
         * @param parsedText
         * @return 
         * @throws IOException 
         */
        public boolean confirmParse(String path, String parsedText) throws IOException {
            // used example codes from http://code.makery.ch/blog/javafx-dialogs-official/
            Image image = null;
            ImageView view = new ImageView();
            BorderPane p = new BorderPane();
            Stage base = new Stage();
            base.setTitle("Image Preview");
            try {
                image = new Image(new FileInputStream(path));
            }   catch (FileNotFoundException e) {
                System.out.println("Image file not found");
            }
            base.setHeight(400);
            base.setWidth(400);
            view.setImage(image);   
            // Fit base window
            view.fitWidthProperty().bind(base.widthProperty());
            view.fitHeightProperty().bind(base.heightProperty());
            
            // Add image
            p.setCenter(view);
            
            // Set scene
            Scene s = new Scene(p);
            base.setScene(s);
            base.show();
            
            if(parsedText != null) {
                // Allow user to correct parsed text
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Parsed Text");
                alert.setHeaderText("Make necessary corrections");
                TextArea textArea = new TextArea(parsedText.trim());
                textArea.setPrefColumnCount(40);
                textArea.setPrefRowCount(10);
                textArea.setEditable(true);
                textArea.setWrapText(true);
                alert.getDialogPane().setContent(textArea);
                alert.setX(base.getX()+400);
                alert.setY(base.getY());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    parsedText = textArea.getText().trim();
                    System.out.println("User corrected text: " + parsedText);
                    try {
                        // Add parsed text to the database
                        database.Database.DatabaseHandler.insertLog(parsedText);
                        // Close image preview
                        base.close();
                        return true;
                    } catch (SQLException ex) {
                        System.out.println("Error occured: " + ex.toString());
                        return false;
                    }
                } else {
                    // User pressed Cancel
                    base.close();
                    return false;
                }
            }
            return false;
        }
}
    
