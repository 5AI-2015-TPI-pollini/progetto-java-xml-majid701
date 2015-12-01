package meteo.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 *
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class MyUrlConnection {
    /**
     * User agent to add in the request headers
     */
    private static final String USER_AGENT = "Mozilla/5.0";
    protected int responseCode;
    private HttpURLConnection connection;
    private URL url;
    
    public MyUrlConnection(){}
    /**
     * 
     * @param url - URL where the connection will be made
     * @throws IOException - If connection error occurred
     */
    public void executeConnection(URL url) throws IOException{
        this.url = url;
        startConnection();
    }
    /**
     * executeConnection() needs to be called before this
     * @return Response code for the connection is returned
     */
    public int getResponseCode(){
        return responseCode;
    }
    
    public HttpURLConnection getConnection(){
        return connection;
    }
    /**
     * Makes a get connection to the URL adding the user agent in the header
     * @param url - URL where the connection will be made
     * @throws IOException - In case of errors exception is thrown
     */
    private void startConnection() throws IOException{
        //Open http connection
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //Set request method to get
        con.setRequestMethod("GET");
        //Add user agent to the request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        //get response code
        responseCode = con.getResponseCode(); 
        connection = con;
        System.out.println("connection terminated");
    }
    
}
