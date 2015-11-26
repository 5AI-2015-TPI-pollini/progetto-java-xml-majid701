package meteo.http;

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

    @Override
    public boolean applyProxy() {
        return false;
        
    }

    @Override
    public boolean testConnection() {
        return false;
        
    }
    
    
}