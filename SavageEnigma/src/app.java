
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import login.LoginController;

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
    private static LoginController loginController;
    
    /**
     * Stage method for Main Menu 
     * @param primaryStage Give by default
     * @throws java.lang.Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Loads FXML resources and create,display a FXML scene 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Login.fxml"));
        Parent root = loader.load();
        base = primaryStage;
        loginController = loader.getController();
        base.setTitle("Savage Enigma");
        login = new Scene(root);
        base.setScene(login);
        base.setResizable(false);
        base.show();
    }
    
    /**
     * Main method for the execution of this application
     * @param args None for normal use
     */
    public static void main(String[] args) {
        launch();
    }

}
