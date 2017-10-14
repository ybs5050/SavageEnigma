/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package ocr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
        
        public String TesseractOCR() throws IOException {
            // Used example from https://stackoverflow.com/questions/29338352/create-filechooser-in-fxml
            Stage primaryStage = null;
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PNG files", "*.png");
            fc.getExtensionFilters().add(filter);
            File file = fc.showOpenDialog(primaryStage);
            if(file != null) {
                showImage(file.getAbsolutePath());
                ITesseract instance = new Tesseract1();
                try {
                    String result = instance.doOCR(file);
                    System.out.println("Parsed text: " + result.trim());
                    return result;
                } catch (TesseractException e) {
                    System.out.println(e.getMessage());
                    return null;
                }
            }
            return null;
        }
        
        /**
         * Shows a preview of a user selected image
         * @param path
         * @throws IOException 
         */
        public void showImage(String path) throws IOException {

            Image image = null;
            ImageView view = new ImageView();
            BorderPane p = new BorderPane();
            Stage base = new Stage();
            base.setTitle("Image Preview");
            try {
                image = new Image(new FileInputStream(path));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            base.setHeight(500);
            base.setWidth(500);
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
        }
}
    
