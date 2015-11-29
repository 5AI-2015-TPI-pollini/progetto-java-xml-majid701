package meteo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 *
 * Proxy data storage
 *
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class Proxy implements Connectable {

    /**
     * IP address of the proxy server
     */
    private String host;
    /**
     * Port number of the proxy server
     */
    private int port;
    /**
     * Authentication username
     */
    private String username;
    /**
     * Authentication password
     */
    private String password;

    /**
     *
     * @param host - Ip address of the proxy server
     * @param port - Port on proxy server listens
     * @param username - Username to authenticate with
     * @param password - Password to authenticate with
     */
    public Proxy(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public void applyProxy() {
        System.setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", "8080");
        System.out.println("Im here1");
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }

    @Override
    public void testConnection() {
        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    String USER_AGENT = "Mozilla/5.0";
                    String url = "http://www.google.com/";
                    
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    
                    System.out.println("Connection done");
                    // optional default is GET
                    con.setRequestMethod("GET");

                    //add request header
                    con.setRequestProperty("User-Agent", USER_AGENT);

                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'GET' request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    //print result
                    System.out.println(response.toString());
                } catch (IOException ex) {
                    System.out.println("Error in connection");
                }
            }
        }).run();
    }
}
