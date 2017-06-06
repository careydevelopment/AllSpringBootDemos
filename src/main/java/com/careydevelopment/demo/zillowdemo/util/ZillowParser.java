package com.careydevelopment.demo.zillowdemo.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.careydevelopment.demo.zillowdemo.model.ZillowSearchResults;

public class ZillowParser {
	
    public static ZillowSearchResults parseSearchResults(InputStream is) {
        ZillowSearchResults results = null;
    	
        try {
            Document doc = getDocument(is);
    		
            //if the doc returned results...
            if (validReturn(doc)) {
                results = populateZillowSearchResults(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
        return results;
    }
    
    
    /**
     * Creates the ZillowSearchResults object from the data in the XML doc
     */
    private static ZillowSearchResults populateZillowSearchResults(Document doc) {
        ZillowSearchResults results = new ZillowSearchResults();
    	
        results.setValue(formatCurrency(getTextNodeFromFirstNode(doc, "amount")));
        results.setHighValue(formatCurrency(getTextNodeFromFirstNode(doc,"high")));
        results.setLowValue(formatCurrency(getTextNodeFromFirstNode(doc,"low")));
        results.setLastUpdated(getTextNodeFromFirstNode(doc,"last-updated"));
        results.setZpid(getTextNodeFromFirstNode(doc,"zpid"));
        results.setComparablesUrl(getTextNodeFromFirstNode(doc,"comparables"));
        results.setMapUrl(getTextNodeFromFirstNode(doc,"mapthishome"));
    	
        return results;
    }
    
    
    /**
     * This method takes a number and formats it for U.S. currency
     */
    private static String formatCurrency(String num) {
        String formattedNum = "";
    	
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        currencyFormatter.setMaximumFractionDigits(0);
    	
        formattedNum = currencyFormatter.format(new Integer(num));
    			
        return formattedNum;
    }
    
    
    /**
     * Convenience method to grab text from the first text node of a list
     */
    private static final String getTextNodeFromFirstNode(Document doc, String nodeName) {
        String val = "";
    	
        NodeList nl = doc.getElementsByTagName(nodeName);
    	
        if (nl.getLength() > 0) {
            val = nl.item(0).getTextContent();
        }
    	
        return val;
    }
    
    
    /**
     * Parses the xml to make sure we have a valid response
     */
    private static boolean validReturn(Document doc) {
        boolean valid = false;
    	
        NodeList messageNodes = doc.getElementsByTagName("message");
    	
        if (messageNodes.getLength() > 0) {
            NodeList textNodes = messageNodes.item(0).getChildNodes();
    		
            if (textNodes.getLength() > 0) {
                Node textNode = doc.getChildNodes().item(0);
                String message = textNode.getTextContent();
                if (message.contains("successful")) {
                    valid = true;
                }
            }
        }
    	
        return valid;
    }
    
    
    private static Document getDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);
    	
        return doc;
    }
}