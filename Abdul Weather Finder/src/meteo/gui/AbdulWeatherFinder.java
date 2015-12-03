package meteo.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author Abdul Majid
 */
public class AbdulWeatherFinder extends Application {
    public static Stage stage;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/meteo/res/application.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Abdul Weather Finder");
        stage.setScene(scene);
        stage.show();  
    }
   
}
