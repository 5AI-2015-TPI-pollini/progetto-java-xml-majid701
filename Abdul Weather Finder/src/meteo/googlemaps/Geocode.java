package meteo.googlemaps;

import java.io.IOException;
import java.net.URL;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import meteo.http.XMLDownloader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author Abdul Majid <majid70111@gmail.com>
 */
public class Geocode {
    /**
     * XPath queries to retrieve data from the XML
     */
    private static final String QUERY_ADDRESS = "/GeocodeResponse/result/formatted_address/text()";
    private static final String QUERY_LATITUDE = "/GeocodeResponse/result/geometry/location/lat/text()";
    private static final String QUERY_LONGITUDE = "/GeocodeResponse/result/geometry/location/lng/text()";
    /**
     * XPath factory reference
     */
    private static final XPathFactory xpathFactory = XPathFactory.newInstance();
    /**
     * Result to return
     */
    private Address[] result;
    
    public Geocode() {}
    
    private void download(URL url) throws IOException{
        //Downloade the xml file
        XMLDownloader downloader = new XMLDownloader();
        downloader.download(url);
        //get document
        Document doc = downloader.getXmlDocument();
        //execute queries on doc
        executeQueries(doc);
    }
    
    private void executeQueries(Document doc){
        //xpath object
        XPath xpath = xpathFactory.newXPath();
        
        try {
            //prepare queries
            XPathExpression formattedAddress = xpath.compile(QUERY_ADDRESS);
            XPathExpression latitude = xpath.compile(QUERY_LATITUDE);
            XPathExpression longitude = xpath.compile(QUERY_LONGITUDE);
            
            //Execute queries
            NodeList addressesFound = (NodeList) formattedAddress.evaluate(doc, XPathConstants.NODESET);
            NodeList lats = (NodeList) latitude.evaluate(doc, XPathConstants.NODESET);
            NodeList lons = (NodeList) longitude.evaluate(doc, XPathConstants.NODESET);
            
            Address[] addresses = new Address[addressesFound.getLength()];
            for (int i = 0; i < addressesFound.getLength(); i++){
                addresses[i] = new Address(addressesFound.item(i).getNodeValue(), 
                        new Coordinate(lats.item(i).getNodeValue(), lons.item(i).getNodeValue()));
            }
            
            result = addresses;
        } catch (XPathExpressionException ex) {
            System.out.println("Errors in queries");
        }
    }
    /**
     * Executes the queries and returns address vector containing all the results
     * found by the search in the XML file
     * @param url - Link from where the XML will be downloaded
     * @return Address array containing all addresses found
     * @throws IOException - In case of connection or XML parsing error 
     */
    public Address[] find(URL url) throws IOException{
        download(url);
        return result;
    }
    
}
