package meteo.http;

import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Generic XML downloader and parser class
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class XMLDownloader extends MyUrlConnection{
    /**
     * Document builder for the XML file
     */
    private static final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

    public XMLDownloader() {}
    
    /**
     * Call after download to get a parsed document of an XML 
     * @return Document - Dom builded document of the XML downloaded
     * @throws IOException - Error reading XML from the Internet
     */
    public Document getXmlDocument() throws IOException{
        try {
            return docBuilderFactory.newDocumentBuilder().parse(this.getConnection().getInputStream());
        } catch (ParserConfigurationException ex) {
            System.out.println("Error in parser configuration");
        } catch (SAXException ex) {
            System.out.println("Error parcing document");
        }
        return null;
    }
    /**
     * Starts the download on a specified URL
     * @param url 
     * @throws IOException - In case of connection errors exception is thrown
     */
    public void download(URL url) throws IOException{
        this.executeConnection(url);
    }
    
}
