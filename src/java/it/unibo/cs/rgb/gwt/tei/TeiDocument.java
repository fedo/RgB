package it.unibo.cs.rgb.gwt.tei;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import javax.xml.parsers.*;
import java.io.IOException;
import java.util.ArrayList;
import org.xml.sax.SAXException;

/**
 *
 * @author fedo
 */
public class TeiDocument {

    private String type;
    private String absolutePath;
    private String teiName;

    private XPath xpath;
    private Document doc;

    private ArrayList<String> xmlList = new ArrayList<String>();

    public TeiDocument(String absolutePath, String type) {
        this.type = type;
        this.absolutePath = absolutePath;
        this.teiName = new File(this.absolutePath).getName();
        
        if (type.equals("file")) {
            //System.out.println("DEBUG TeiDocument: creating " + absolutePath);
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = builder.parse(new File(this.absolutePath));
                xpath = XPathFactory.newInstance().newXPath();
            } catch (SAXException ex) {
                Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("directory")) {
            File folder = new File(absolutePath);;
            File[] listOfXml = folder.listFiles();
        }
    }

    public String getTeiName() {
        return teiName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String xpathQuery(String expression) {
        String str = "error";

        if(type.equals("file"))
            try {
                str = (String) xpath.evaluate(expression, doc, XPathConstants.STRING);
            } catch (XPathExpressionException ex) {
                Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return str;
    }

    public String getTitle() {
        String retval = "error";

        if(type.equals("file"))
            retval = xpathQuery("//title/text()");
        return retval;
    }

    public String getAuthor() {
        String retval = "error";
        
        if(type.equals("file"))
            retval = xpathQuery("//author/text()");
        return retval;
    }
}
