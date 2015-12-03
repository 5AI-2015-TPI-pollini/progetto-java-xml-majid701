package meteo.gui;

import com.sun.deploy.net.URLEncoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import meteo.googlemaps.Address;
import meteo.googlemaps.Geocode;
import static meteo.gui.AbdulWeatherFinder.stage;
import meteo.weather.WeatherState;
import meteo.wunderground.Wunderground;
/**
 *
 * @author Abdul Majid
 */
public class FXMLDocumentController implements Initializable {
    private static final String MAPS_URL_PREFIX = "https://maps.googleapis.com/maps/api/geocode/";
    private static final String DOCUMENT_TYPE = "xml"; //use 'json' to receive a json document
    private static final String ADDRESS_PREFIX = "?address=";
    
    private static final String WEATHER_URL_PREFIX = "http://api.wunderground.com/api/0376b1a50c7fe2fd/forecast/conditions/q/";
    private static final String CELSIUS = "°C";
    /**
     * Max number of threads
     */
    private static final int N_THREADS = 2;
    /**
     * Executor service to execute HTTP requests in parallel
     */
    public static final ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
    
    @FXML
    private Button btnSearch;
    
    @FXML
    private TextField txtSearch;
    
    @FXML
    private ProgressIndicator loading;
    
    @FXML
    private Label lblFormattedAddress;
    
    @FXML
    private Label lblObservationTime;
    
    @FXML
    private ImageView imgIcon;
    
    @FXML
    private Label lblHuman;
    
    @FXML
    private Label lblTemperatureC;
    
    @FXML
    private Label lblFeelsLike;
    
    @FXML
    private Label lblFeelsTemp;
            
    private URL url;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {  
        
        executorService.submit(new Runnable() {

            @Override
            public void run() {
                
                
                try {
                    System.out.println("Started request");
                    Wunderground w = new Wunderground();
                     WeatherState ws = w.getWeather(new URL("http://api.wunderground.com/api/2fe535a12fd3638f/forecast/conditions/q/Brescia.xml"));
                     System.out.println("Finished");
                     System.out.println(ws);
                     
                } catch (MalformedURLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
        });
    }
    
    @FXML 
    private void btnSearchClicked(){
        String find = txtSearch.getText();
        StringBuilder string = new StringBuilder(MAPS_URL_PREFIX);
        string.append(DOCUMENT_TYPE);
        string.append(ADDRESS_PREFIX);
        try {
            string.append(URLEncoder.encode(find, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Url encoding error");
        }
        try {
            url = new URL(string.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        executorService.submit(new Runnable() {

            @Override
            public void run() {
                Geocode geo = new Geocode();
                try {
                    enableLoading();
                    Address addressFound = geo.find(url)[0];
                    StringBuilder temp = new StringBuilder(WEATHER_URL_PREFIX);
                    temp.append(addressFound.getLatitude());
                    temp.append(",");
                    temp.append(addressFound.getLongitude());
                    temp.append(".");
                    temp.append(DOCUMENT_TYPE);
                    url = new URL(temp.toString());
                    Wunderground weatherService = new Wunderground();
                    WeatherState weatherstate = weatherService.getWeather(url);
                    viewInformation(addressFound.getFormattedAddress(), weatherstate);
                    
                    loadingDone();
                    System.out.println(addressFound);
                } catch (IOException ex) {
                    connectionFailed();
                }
            }
        });
    }
    
    private void viewInformation(final String addr, final WeatherState ws){ 
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                lblFeelsLike.setVisible(true);
                lblFormattedAddress.setText(addr);
                lblObservationTime.setText(ws.getObservationTime());
                lblHuman.setText(ws.getWeatherHuman());
                lblTemperatureC.setText(ws.getTemperatureC() + CELSIUS);
                lblFeelsTemp.setText(ws.getFeels() + CELSIUS);
                imgIcon.setImage(ws.getIconImage());
            }
        });
    }
    
    private void enableLoading(){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                loading.setDisable(false);
                loading.setProgress(-1.0);
            }
        });
    }
    
    private void loadingDone(){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                loading.setProgress(1.0);
            }
        });
    }
    
    private void connectionFailed(){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                loading.setProgress(0.0);
                loading.setDisable(true);
                Dialogs.showWarningDialog(stage, "Try setting proxy and checking your connection", "Impossible to connect", "Connection error");
            }
        });
    }
    
    @FXML
    private void openProxySettings() {
        Parent root;
        Stage newStage;
        
        try {
            newStage = new Stage();
            root = FXMLLoader.load(getClass().getResource("/meteo/res/proxy.fxml"));
            newStage.setScene(new Scene(root));
            newStage.setTitle("Proxy settings");
            newStage.initModality(Modality.NONE);
            newStage.initOwner(btnSearch.getScene().getWindow());
            ProxyController.dialogStage = newStage;
            newStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpThings();
        
    }
    
    private void setUpThings(){
        //Search textfield text change event
        ChangeListener c = new ChangeListener() {

            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (txtSearch.getText().length() > 0)
                    btnSearch.setDisable(false);
                else btnSearch.setDisable(true);
            }
        };
        txtSearch.textProperty().addListener(c);
        
    }
     
}
