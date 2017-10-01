/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package utility;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class for creating new stages from FXML
 * @author Youngmin
 */
public class Stages {
    
    /**
     * Static class for creating new stages from FXML
     */
    public static class StageHandler {
        
        /**
         * Creates and returns a new stage
         * @param fxmlDirectory FXML location
         * @param stageTitle stage title
         * @return 
         * @throws java.io.IOException 
         */
        public static Stage createNewStage(String fxmlDirectory, String stageTitle) throws IOException {
            Stage base = new Stage();
            Scene register;
            // Loads FXML resources and create,display a FXML stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Class.class.getResource(fxmlDirectory));
            Parent root = loader.load();
            register = new Scene(root);
            base.setScene(register);
            base.setTitle(stageTitle);
            return base;
        }
    }
    
}
