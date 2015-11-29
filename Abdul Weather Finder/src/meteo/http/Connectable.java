package meteo.http;

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
     */
    public void testConnection();
}
