
import com.jfoenix.controls.JFXDialog;
import database.Database;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import utility.Alerts;

/*
 * IST 440W Team 6
 * Project Savage Enigma
 * Application Main Class
 */

/**
 *
 * @author Youngmin
 */
public class app extends Application {
    
    private static Stage base;
    private static Scene login;
    
    /**
     * Stage method for Main Menu 
     * @param primaryStage Give by default
     * @throws java.lang.Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Loads FXML resources and create,display a FXML stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Login.fxml"));
        Parent root = loader.load();
        base = primaryStage;
        base.setTitle("Savage Enigma - Login");
        login = new Scene(root);
        base.setScene(login);
        base.setResizable(false);
        base.show(); 
        // Connect to db
        if (!Database.DatabaseHandler.connectDatabase()) {
            Pair<Stage, JFXDialog> tempDialog = Alerts.AlertHandler.createConfirmWindow("Error", "Failed to connect to the database \nClosing Program", "Okay", null);
            tempDialog.getKey().setOnHiding((WindowEvent event) -> {
                System.out.println("Connection to db failed. Aborting");
                base.close();
            });
            tempDialog.getKey().centerOnScreen();
            tempDialog.getKey().show();
            tempDialog.getValue().show();
        }
    }
    
    /**
     * Main method for the execution of this application
     * @param args None for normal use
     */
    public static void main(String[] args) {
        launch();
        //ocr.Tesseract.TesseractOCR.test();
    }

}
