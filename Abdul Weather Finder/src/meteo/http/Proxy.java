package meteo.http;

import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 *
 * Proxy data storage
 *
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class Proxy extends MyUrlConnection implements Connectable {

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
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }

    @Override
    public boolean testConnection() throws IOException {
        try {
            URL url = new URL("http://www.google.it/");
            MyUrlConnection con = new MyUrlConnection();
            con.executeConnection(url);
            int responseCode = con.getResponseCode();
            if (responseCode == 200)
                return true;
            return false;
        } catch (MalformedURLException ex) {
            System.out.println("Url not formed correctly");
        }
        return false;
    }
}
