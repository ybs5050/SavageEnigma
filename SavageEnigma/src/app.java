
import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        Database.DatabaseHandler.connectDatabase();
    }
    
    /**
     * Main method for the execution of this application
     * @param args None for normal use
     */
    public static void main(String[] args) {
        launch();
    }

}
