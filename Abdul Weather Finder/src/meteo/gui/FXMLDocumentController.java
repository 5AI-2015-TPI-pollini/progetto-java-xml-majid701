package meteo.gui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import meteo.googlemaps.Address;
import meteo.googlemaps.Geocode;
import static meteo.gui.AbdulWeatherFinder.stage;
import meteo.weather.Forecast;
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
    
    @FXML
    private Label lblPressure;
    
    @FXML
    private Label lblVisibility;
    
    @FXML
    private Label lblWindKph;
    
    @FXML
    private Label lblWindMaxKph;
    
    @FXML
    private Label lblWindChill;
    
    @FXML
    private Label lblWindDirection;
    
    @FXML
    private Label lblUv;
    
    @FXML
    private Label lblRainPreview;
    
    @FXML
    private GridPane weatherInfos;
    
    @FXML
    private BorderPane forecast1;
    
    @FXML
    private BorderPane forecast2;
    
    @FXML
    private BorderPane forecast3;
    
    @FXML
    private BorderPane forecast4;
    
    @FXML
    private ImageView imgForecast1;

    @FXML
    private ImageView imgForecast2;

    @FXML
    private ImageView imgForecast3;

    @FXML
    private ImageView imgForecast4;
    
    @FXML
    private Label lblForecastDay1;

    @FXML
    private Label lblForecastDay2;

    @FXML
    private Label lblForecastDay3;

    @FXML
    private Label lblForecastDay4;
    
    @FXML
    private Label lblForecastMax1;

    @FXML
    private Label lblForecastMax2;

    @FXML
    private Label lblForecastMax3;

    @FXML
    private Label lblForecastMax4;
    
    @FXML
    private Label lblForecastMin1;

    @FXML
    private Label lblForecastMin2;

    @FXML
    private Label lblForecastMin3;

    @FXML
    private Label lblForecastMin4;
    
    @FXML
    private Label lblForecastHuman1;

    @FXML
    private Label lblForecastHuman2;

    @FXML
    private Label lblForecastHuman3;

    @FXML
    private Label lblForecastHuman4;
    
    @FXML
    private Label lblForecastRain1;

    @FXML
    private Label lblForecastRain2;

    @FXML
    private Label lblForecastRain3;

    @FXML
    private Label lblForecastRain4;
    
    private URL url;
    
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
                weatherInfos.setVisible(true);
                
                lblFormattedAddress.setText(addr);
                lblObservationTime.setText(ws.getObservationTime());
                lblHuman.setText(ws.getWeatherHuman());
                lblTemperatureC.setText(ws.getTemperatureC() + CELSIUS);
                lblFeelsTemp.setText(ws.getFeels() + CELSIUS);
                imgIcon.setImage(ws.getIconImage());
                lblPressure.setText(ws.getPressurePa() + "Pa");
                lblVisibility.setText(ws.getVisibilityKm());
                lblWindKph.setText(ws.getWindKph());
                lblWindMaxKph.setText(ws.getWindMaxKph());
                lblWindChill.setText(ws.getWindChill() + CELSIUS);
                lblWindDirection.setText(ws.getWindDir());
                lblUv.setText(ws.getUv());
                lblRainPreview.setText(ws.getRainToday() + "mm");
                viewForecast(ws.getForecast());
            }
        });
    }
    
    private void viewForecast(Forecast[] forecast){
        ArrayList<String> data = new ArrayList();
        for(Forecast i : forecast){
            data.add(i.getWeekDay() + " " + i.getDayNum() + "/" + i.getMonthNum());
            data.add(i.getMaxTempC() + CELSIUS);
            data.add(i.getMinTempC() + CELSIUS);
            data.add(i.getDayRain() + "mm");
            data.add(i.getHuman());
        }
        
        for (BorderPane i : forecastPanels){
            i.setVisible(true);
        }
        
        int count = 0;
        //Day 1
        for (Label i : lblForecastData1){
            i.setText(data.get(count));
            count++;
        }
        imgForecast1.setImage(forecast[0].getIconImage());
        //Day 2
        for (Label i : lblForecastData2){
            i.setText(data.get(count));
            count++;
        }
        imgForecast2.setImage(forecast[1].getIconImage());
        //Day 3
        for (Label i : lblForecastData3){
            i.setText(data.get(count));
            count++;
        }
        imgForecast3.setImage(forecast[2].getIconImage());
        //Day 4
        for (Label i : lblForecastData4){
            i.setText(data.get(count));
            count++;
        }
        imgForecast4.setImage(forecast[3].getIconImage());
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
    
    @FXML
    private void closeWindow(){
        executorService.shutdown();
        System.exit(0);
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
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                System.out.println("Here");
                executorService.shutdown();
            }
        });
        
        forecastPanels = new BorderPane[4];
        forecastPanels[0] = forecast1;
        forecastPanels[1] = forecast2;
        forecastPanels[2] = forecast3;
        forecastPanels[3] = forecast4;
        
        lblForecastData1 = new Label[5];
        lblForecastData1[0] = lblForecastDay1;
        lblForecastData1[1] = lblForecastMax1;
        lblForecastData1[2] = lblForecastMin1;
        lblForecastData1[3] = lblForecastRain1;
        lblForecastData1[4] = lblForecastHuman1;
        
        lblForecastData2 = new Label[5];
        lblForecastData2[0] = lblForecastDay2;
        lblForecastData2[1] = lblForecastMax2;
        lblForecastData2[2] = lblForecastMin2;
        lblForecastData2[3] = lblForecastRain2;
        lblForecastData2[4] = lblForecastHuman2;
        
        lblForecastData3 = new Label[5];
        lblForecastData3[0] = lblForecastDay3;
        lblForecastData3[1] = lblForecastMax3;
        lblForecastData3[2] = lblForecastMin3;
        lblForecastData3[3] = lblForecastRain3;
        lblForecastData3[4] = lblForecastHuman3;

        lblForecastData4 = new Label[5];
        lblForecastData4[0] = lblForecastDay4;
        lblForecastData4[1] = lblForecastMax4;
        lblForecastData4[2] = lblForecastMin4;
        lblForecastData4[3] = lblForecastRain4;
        lblForecastData4[4] = lblForecastHuman4;

    }   
    
    private BorderPane[] forecastPanels;
    private Label[] lblForecastData1;
    private Label[] lblForecastData2;
    private Label[] lblForecastData3;
    private Label[] lblForecastData4;
     
}
