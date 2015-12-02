package meteo.wunderground;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import meteo.http.XMLDownloader;
import meteo.weather.WeatherState;
import org.w3c.dom.Document;

/**
 *
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class Wunderground {
    /**
     * XPath queries to retrieve data from the XML of CURRENT WEATHER
     */
    //time
    private static final String QUERY_OBSERVATION_TIME = "/response/current_observation/local_time_rfc822";
    //temperature
    private static final String QUERY_TEMP_C = "/response/current_observation/temp_c";
    //weather
    private static final String QUERY_WEATHER_HUMAN = "/response/current_observation/weather";
    private static final String QUERY_HUMIDITY = "/response/current_observation/relative_humidity";
    private static final String QUERY_FEELS = "/response/current_observation/feelslike_c";
    //wind
    private static final String QUERY_WIND_KPH = "/response/current_observation/wind_kph";
    private static final String QUERY_WIND_DIR = "/response/current_observation/wind_dir";
    private static final String QUERY_WIND_MAX_KPH = "/response/current_observation/wind_gust_kph";
    private static final String QUERY_WIND_CHILL = "/response/current_observation/windchill_c";
    //pressure
    private static final String QUERY_PRESSURE_PA = "/response/current_observation/pressure_mb";
    //visibility
    private static final String QUERY_VISIBILITY_KM = "/response/current_observation/visibility_km";
    //solar
    private static final String QUERY_UV = "/response/current_observation/UV";
    //rain level
    private static final String QUERY_RAIN_TODAY_MM = "/response/current_observation/precip_today_metric";
    //icon 
    private static final String QUERY_ICON_NAME = "/response/current_observation/icon";
    
    //ICON URL (needs icon name + .gif)
    private static final String ICON_URL = "http://icons.wxug.com/i/c/k/";
    
    
    /**
     * XPath factory reference
     */
    private static final XPathFactory xpathFactory = XPathFactory.newInstance();
    
    public Wunderground() {}
    
    private void download(URL url) throws IOException{
        //Downloade the xml file
        XMLDownloader downloader = new XMLDownloader();
        downloader.download(url);
        //get document
        Document doc = downloader.getXmlDocument();
        //execute queries on doc
        executeQueries(doc);
    }
    
    private void executeQueries(Document doc){
        //xpath object
        XPath xpath = xpathFactory.newXPath();
        ArrayList<XPathExpression> queries = prepareQueries(xpath);
        //weather information holder object
        WeatherState weather = new WeatherState();
        int count = 0;
        for(XPathExpression i : queries){
            try {
                String temp = (String) i.evaluate(doc, XPathConstants.STRING);
                switch(count){
                    case 0:
                        weather.setObservationTime(temp);
                        count++;
                        break;
                    case 1:
                        weather.setTemperatureC(temp);
                        count++;
                        break;
                    case 2:
                        weather.setWeatherHuman(temp);
                        count++;
                        break;
                    case 3:
                        weather.setHumidity(temp);
                        count++;
                        break;
                    case 4:
                        weather.setFeels(temp);
                        count++;
                        break;
                    case 5:
                        weather.setWindKph(temp);
                        count++;
                        break;
                    case 6:
                        weather.setWindDir(temp);
                        count++;
                        break;
                    case 7:
                        weather.setWindMaxKph(temp);
                        count++;
                        break;
                    case 8:
                        weather.setWindChill(temp);
                        count++;
                        break;
                    case 9:
                        weather.setPressurePa(temp);
                        count++;
                        break;
                    case 10:
                        weather.setVisibilityKm(temp);
                        count++;
                        break;
                    case 11:
                        weather.setUv(temp);
                        count++;
                        break;
                    case 12:
                        weather.setRainToday(temp);
                        count++;
                        break;
                    case 13:
                        weather.setIcon(temp);
                        count++;
                        break;
                }
            } catch (XPathExpressionException ex) {
                Logger.getLogger(Wunderground.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void find(URL url) throws IOException{
        download(url);
    }
    
    private ArrayList<XPathExpression> prepareQueries(XPath xpath){
        ArrayList<XPathExpression> queries = new ArrayList();
        try {
            //prepare queries
            XPathExpression qryObservationTime = xpath.compile(QUERY_OBSERVATION_TIME);
            XPathExpression qryTempC = xpath.compile(QUERY_TEMP_C);
            XPathExpression qryWeatherHuman = xpath.compile(QUERY_WEATHER_HUMAN);
            XPathExpression qryHumidity = xpath.compile(QUERY_HUMIDITY);
            XPathExpression qryFeels = xpath.compile(QUERY_FEELS);
            XPathExpression qryWindKph = xpath.compile(QUERY_WIND_KPH);
            XPathExpression qryWindDir = xpath.compile(QUERY_WIND_DIR);
            XPathExpression qryWindMaxKph = xpath.compile(QUERY_WIND_MAX_KPH);
            XPathExpression qryWindChill = xpath.compile(QUERY_WIND_CHILL);
            XPathExpression qryPressurePa = xpath.compile(QUERY_PRESSURE_PA);
            XPathExpression qryVisibilityKm = xpath.compile(QUERY_VISIBILITY_KM);
            XPathExpression qryUv = xpath.compile(QUERY_UV);
            XPathExpression qryRainToday = xpath.compile(QUERY_RAIN_TODAY_MM);
            XPathExpression qryIcon = xpath.compile(QUERY_ICON_NAME);
            
            //add them to a list 
            queries.add(qryObservationTime);
            queries.add(qryTempC);
            queries.add(qryWeatherHuman);
            queries.add(qryHumidity);
            queries.add(qryFeels);
            queries.add(qryWindKph);
            queries.add(qryWindDir);
            queries.add(qryWindMaxKph);
            queries.add(qryWindChill);
            queries.add(qryPressurePa);
            queries.add(qryVisibilityKm);
            queries.add(qryUv);
            queries.add(qryRainToday);
            queries.add(qryIcon);
        } catch (XPathExpressionException ex) {
            System.out.println("Error in queries");
        }
        return queries;
    }
}
