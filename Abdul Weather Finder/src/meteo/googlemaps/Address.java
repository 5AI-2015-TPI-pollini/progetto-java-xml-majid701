package meteo.googlemaps;

/**
 *
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class Address {
    private String formattedAddress;
    private Coordinate coordinate;
    
    public Address(String address, Coordinate c){
        address = formattedAddress;
        coordinate = c;
    }
    /**
     * 
     * @return Name of the location 
     */
    public String getFormattedAddress() {
        return formattedAddress;
    }
    
    /**
     * 
     * @return Latitude of the location
     */
    public double getLatitude(){
        return coordinate.getLatitude();
    }
    /**
     * 
     * @return Longitude of the location 
     */
    public double getLongitude(){
        return coordinate.getLongitude();
    }
}
