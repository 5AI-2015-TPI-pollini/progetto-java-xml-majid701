package meteo.weather;

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

    @Override
    public String toString() {
        return "Forecast{" + "weekDay=" + weekDay + ", dayNum=" + dayNum + ", monthNum=" + monthNum + ", maxTempC=" + maxTempC + ", minTempC=" + minTempC + ", human=" + human + ", iconName=" + iconName + ", dayRain=" + dayRain + '}';
    } 
}
