package it.unibo.cs.rgb.gwt.tei;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import javax.xml.parsers.*;
import java.io.IOException;
import org.xml.sax.SAXException;

/**
 *
 * @author fedo
 */
public class TeiDocument {

    private String absolutePath;
    private XPath xpath;
    private Document doc;

    public TeiDocument(String absolutePath) {
        //System.out.println("DEBUG TeiDocument: creating " + absolutePath);
        try {
            this.absolutePath = absolutePath;
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
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String xpathQuery(String expression) {
        String str = "error";
        try {
            str = (String) xpath.evaluate(expression, doc, XPathConstants.STRING);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public String getTitle() {
        return xpathQuery("//title/text()");
    }

    public String getAuthor() {
        return xpathQuery("//author/text()");
    }
}
