package meteo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Proxy data storage 
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class Proxy implements Connectable{
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
        System.setProperty("http.proxyPort", Integer.toString(port));
        Authenticator.setDefault(new Authenticator() {
            @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }

    @Override
    public boolean testConnection() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    System.out.println("Starting connection");
                    URL url = new URL("http://www.google.it");
                    URLConnection con = url.openConnection();
                    System.out.println("Connection done");
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));

                    // Read it ...
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        System.out.println(inputLine);

                    in.close();
                    }catch (MalformedURLException ex) {
                        Logger.getLogger(Proxy.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Proxy.class.getName()).log(Level.SEVERE, null, ex);
                    }
        
            }
        });

        return true;
    }   
    
    
}