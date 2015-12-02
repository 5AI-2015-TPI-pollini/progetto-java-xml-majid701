package meteo.weather;

import java.util.Arrays;

/**
 *
 * @author Abdul Majid <majid70111@hotmail.com>
 */
public class WeatherState {
    private String ObservationTime;
    private String TemperatureC;
    private String WeatherHuman;
    private String Humidity;
    private String Feels;
    private String WindKph;
    private String WindDir;
    private String WindMaxKph;
    private String WindChill;
    private String PressurePa;
    private String VisibilityKm;
    private String Uv;
    private String RainToday;
    private String icon;
    
    private Forecast[] forecast;
    
    public WeatherState(){}

    public void setObservationTime(String ObservationTime) {
        this.ObservationTime = ObservationTime;
    }

    public void setTemperatureC(String TemperatureC) {
        this.TemperatureC = TemperatureC;
    }

    public void setWeatherHuman(String WeatherHuman) {
        this.WeatherHuman = WeatherHuman;
    }

    public void setHumidity(String Humidity) {
        this.Humidity = Humidity;
    }

    public void setFeels(String Feels) {
        this.Feels = Feels;
    }

    public void setWindKph(String WindKph) {
        this.WindKph = WindKph;
    }

    public void setWindDir(String WindDir) {
        this.WindDir = WindDir;
    }

    public void setWindMaxKph(String WindMaxKph) {
        this.WindMaxKph = WindMaxKph;
    }

    public void setWindChill(String WindChill) {
        this.WindChill = WindChill;
    }

    public void setPressurePa(String PressurePa) {
        this.PressurePa = PressurePa;
    }

    public void setVisibilityKm(String VisibilityKm) {
        this.VisibilityKm = VisibilityKm;
    }

    public void setUv(String Uv) {
        this.Uv = Uv;
    }

    public void setRainToday(String RainToday) {
        this.RainToday = RainToday;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setForecast(Forecast[] forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        return "WeatherState{" + "ObservationTime=" + ObservationTime + ", TemperatureC=" + TemperatureC + ", WeatherHuman=" + WeatherHuman + ", Humidity=" + Humidity + ", Feels=" + Feels + ", WindKph=" + WindKph + ", WindDir=" + WindDir + ", WindMaxKph=" + WindMaxKph + ", WindChill=" + WindChill + ", PressurePa=" + PressurePa + ", VisibilityKm=" + VisibilityKm + ", Uv=" + Uv + ", RainToday=" + RainToday + ", icon=" + icon + "\n" + ", forecast=" + Arrays.toString(forecast) + '}';
    }

    
    
}
