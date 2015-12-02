package meteo.googlemaps;

/**
 *
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class Coordinate {
    /**
     * latitude of a location
     */
    private double latitude;
    /**
     * longitude of a location
     */
    private double longitude;

    public Coordinate(String lat, String lon) {
       latitude = Double.parseDouble(lat);
       longitude = Double.parseDouble(lon);
    }
    
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
