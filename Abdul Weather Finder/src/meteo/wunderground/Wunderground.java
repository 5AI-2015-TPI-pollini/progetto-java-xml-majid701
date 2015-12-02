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
import meteo.weather.Forecast;
import meteo.weather.WeatherState;
import org.w3c.dom.Document;

/**
 *
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class Wunderground {
    private static final int SHORT_FORECAST = 4;
    private static final int FORECAST_DATA = 8;
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
     * XPath queries to retrieve forecast from the XML of the given place
     */
    private static final String QUERY_FORECAST = "/response/forecast/simpleforecast/forecastdays/forecastday";
    //Add to the above string including index in the middle ["+i+"]
    private static final String FORECAST_DAY_NAME = "/date/weekday";
    private static final String FORECAST_DAY_NUM = "/date/day";
    private static final String FORECAST_MONTH_NUM = "/date/month";
    private static final String FORECAST_MAXTEMP_C = "/high/celsius";
    private static final String FORECAST_MINTEMP_C = "/low/celsius";
    private static final String FORECAST_HUMAN = "/conditions";
    private static final String FORECAST_ICON = "/icon";
    private static final String FORECAST_RAIN = "/qpf_allday/mm";
    
    /**
     * XPath factory reference
     */
    private static final XPathFactory xpathFactory = XPathFactory.newInstance();
    /**
     * Weather state object containing current weather + forecast
     */
    private WeatherState weatherState;
    
    public Wunderground() {}
    /**
     * 
     * @param url - URL to download the weather XML from
     * @return Weather state object containing current weather + forecast
     * @throws IOException - In case of connection error 
     */
    public WeatherState getWeather(URL url) throws IOException{
        download(url);
        return weatherState;
    }
    
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
        //Forecast information holder object
        Forecast [] forecast = new Forecast[SHORT_FORECAST];
        int count = 0;
        boolean endLoop = false;
        for(int i = 0; i < queries.size(); i++){
            try {
                String temp = (String) queries.get(i).evaluate(doc, XPathConstants.STRING);
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
                    case 14:
                        forecast = prepareForecast(i, queries, forecast, doc);
                        endLoop = true;
                        count++;
                        break;
                }
                if (endLoop){
                    break;
                }
            } catch (XPathExpressionException ex) {
                Logger.getLogger(Wunderground.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        weather.setForecast(forecast);
        weatherState = weather;
    }
    
    private Forecast[] prepareForecast(int start, ArrayList<XPathExpression> queries, Forecast[] forecast, Document doc) throws XPathExpressionException {
        Forecast temp;
        for (int i = 0; i < SHORT_FORECAST; i++){
            temp = new Forecast();
            int count = 0;
            for (int j = start; j < (start + FORECAST_DATA); j++){
                String asp = (String) queries.get(j).evaluate(doc, XPathConstants.STRING);
                switch(count){
                    case 0:
                        temp.setWeekDay(asp);
                        count++;
                        break;
                    case 1:
                        temp.setDayNum(asp);
                        count++;
                        break;
                    case 2:
                        temp.setMonthNum(asp);
                        count++;
                        break;
                    case 3:
                        temp.setMaxTempC(asp);
                        count++;
                        break;
                    case 4:
                        temp.setMinTempC(asp);
                        count++;
                        break;
                    case 5:
                        temp.setHuman(asp);
                        count++;
                        break;
                    case 6:
                        temp.setIconName(asp);
                        count++;
                        break;
                    case 7:
                        temp.setDayRain(asp);
                        count++;
                        break;
                }
            }
            start += FORECAST_DATA;
            forecast[i] = temp;
        }
        return forecast;
    }
     
    private ArrayList<XPathExpression> prepareQueries(XPath xpath){
        ArrayList<XPathExpression> queries = new ArrayList();
        try {
            //prepare queries CURRENT WEATHER
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
            
            // prepare queries FORECAST
            for(int i = 1; i < 5; i++){
                //compile FORECAST queries
                XPathExpression qryForecastDayName = xpath.compile(QUERY_FORECAST + "["+i+"]" + FORECAST_DAY_NAME);
                XPathExpression qryForecastDayNum = xpath.compile(QUERY_FORECAST + "["+i+"]" + FORECAST_DAY_NUM);
                XPathExpression qryForecastMonthNum = xpath.compile(QUERY_FORECAST + "["+i+"]" + FORECAST_MONTH_NUM);
                XPathExpression qryForecastMaxTempC = xpath.compile(QUERY_FORECAST + "["+i+"]" + FORECAST_MAXTEMP_C);
                XPathExpression qryForecastMinTempC = xpath.compile(QUERY_FORECAST + "["+i+"]" + FORECAST_MINTEMP_C);
                XPathExpression qryForecastHuman = xpath.compile(QUERY_FORECAST + "["+i+"]" + FORECAST_HUMAN);
                XPathExpression qryForecastIcon = xpath.compile(QUERY_FORECAST + "["+i+"]" + FORECAST_ICON);
                XPathExpression qryForecastRain = xpath.compile(QUERY_FORECAST + "["+i+"]" + FORECAST_RAIN);
                //add them to the list
                queries.add(qryForecastDayName);
                queries.add(qryForecastDayNum);
                queries.add(qryForecastMonthNum);
                queries.add(qryForecastMaxTempC);
                queries.add(qryForecastMinTempC);
                queries.add(qryForecastHuman);
                queries.add(qryForecastIcon);
                queries.add(qryForecastRain);
            }
            
        } catch (XPathExpressionException ex) {
            System.out.println("Error in queries");
        }
        return queries;
    }
}
