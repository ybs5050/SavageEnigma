/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package utility;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Class for creating alert window display static classes
 * @author Youngmin
 */
public class Alerts {
    
    /**
     * Static class for displaying confirmation window
     */
    public static class AlertHandler {
        
        /**
         * Creates a confirmation window with given title and heading
         * @param title Title of the alert window
         * @param heading Alert window heading
         * @param buttonName Button name
         * @param newScene Scene to display Pair<K, V> K = FXML file location, V = Stage Title
         * @return Stage and Dialog UI 
         */
        public static Pair<Stage,JFXDialog> createConfirmWindow(String title, String heading, String buttonName, Pair<String, String> newScene) {
            // Show new stage for exit alert
            StackPane stackPane1 = new StackPane();
            Stage stage = new Stage();
            stage.setScene(new Scene(stackPane1, 300, 110));
            stage.setTitle(title);
            
            // JFOENIX Dialog window
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(heading));
            JFXDialog dialog = new JFXDialog(stackPane1, content, JFXDialog.DialogTransition.CENTER);
            JFXButton okayButton = new JFXButton(buttonName);
            okayButton.setButtonType(ButtonType.RAISED);
            okayButton.setRipplerFill(Color.valueOf("000000"));
            okayButton.setOnAction((ActionEvent event1) -> {
                dialog.close();
                stage.close();
                if (newScene != null) {
                    // Create and display registration stage
                    Stage base;
                    try {
                        base = Stages.StageHandler.createNewStage(newScene.getKey(), newScene.getValue());
                        base.show();
                    } catch (IOException except) {
                        System.out.println("Error occured: " + except.toString());
                     }              
                }
            });
            stage.resizableProperty().set(false);
            content.setActions(okayButton);
            return new Pair(stage, dialog);
        }
    }
}
