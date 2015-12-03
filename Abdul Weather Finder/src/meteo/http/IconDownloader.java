package meteo.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.scene.image.Image;

/**
 * Given a URL downloads the image 
 * @author Abdul Majid
 */
public class IconDownloader extends MyUrlConnection{
    
    public IconDownloader(){}
    /**
     * 
     * @param link - To download the image from
     * @return Image 
     * @throws IOException - In case of connection error
     */
    public Image download(String link) throws IOException{
        executeConnection(new URL(link));
        HttpURLConnection connection = getConnection();
        Image image = new Image(connection.getInputStream());
        return image;
    }
}
