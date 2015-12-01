package meteo.http;

import java.io.IOException;

/**
 *
 * Connection interface for testing and proxy usage
 * @author Abdul Majid <majid70111@gmail.com>
 */
public interface Connectable {
    /**
     * Applies proxy settings to the program
     */
    public void applyProxy();
    /**
     * Sends a simple request to a link verifying connectivity
     * @return - Connection success status
     * @throws IOException - In case of connection error
     */
    public boolean testConnection() throws IOException;
}
