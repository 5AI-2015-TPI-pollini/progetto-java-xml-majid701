package meteo.weather;

import javafx.scene.image.Image;

/**
 * Holds forecast information of a WeatherState object
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class Forecast {
    private String weekDay;
    private String dayNum;
    private String monthNum;
    private String maxTempC;
    private String minTempC;
    private String human;
    private String iconName;
    private String dayRain;
    
    private Image iconImage;

    public Forecast() {}

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    public void setMonthNum(String monthNum) {
        this.monthNum = monthNum;
    }

    public void setMaxTempC(String maxTempC) {
        this.maxTempC = maxTempC;
    }

    public void setMinTempC(String minTempC) {
        this.minTempC = minTempC;
    }

    public void setHuman(String human) {
        this.human = human;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setDayRain(String dayRain) {
        this.dayRain = dayRain;
    }

    public void setIconImage(Image iconImage) {
        this.iconImage = iconImage;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public String getDayNum() {
        return dayNum;
    }

    public String getMonthNum() {
        return monthNum;
    }

    public String getMaxTempC() {
        return maxTempC;
    }

    public String getMinTempC() {
        return minTempC;
    }

    public String getHuman() {
        return human;
    }

    public String getIconName() {
        return iconName;
    }

    public String getDayRain() {
        return dayRain;
    }

    public Image getIconImage() {
        return iconImage;
    }
    
    
    
    @Override
    public String toString() {
        return "Forecast{" + "weekDay=" + weekDay + ", dayNum=" + dayNum + ", monthNum=" + monthNum + ", maxTempC=" + maxTempC + ", minTempC=" + minTempC + ", human=" + human + ", iconName=" + iconName + ", dayRain=" + dayRain + '}';
    } 
}
