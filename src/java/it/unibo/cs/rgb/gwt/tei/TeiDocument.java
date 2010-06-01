package it.unibo.cs.rgb.gwt.tei;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.*;
import javax.xml.xpath.*;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import org.xml.sax.SAXException;
import it.unibo.cs.rgb.gwt.RgBConfiguration;
import javax.xml.parsers.*;

/**
 *
 * @author fedo
 */
public class TeiDocument {

    private String type; //{"file", "directory"}
    private String absolutePath;
    private String teiName;
    private XPath xpath;
    private Document doc;
    private ArrayList<String> xmlList = new ArrayList<String>();

    public TeiDocument(String absolutePath) {

        File file = new File(absolutePath);
        if (file.isFile()) {
            this.type = "file";
        } else if (file.isDirectory()) {
            this.type = "directory";
        }


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
            File folder = new File(absolutePath);
            ;
            File[] listOfXml = folder.listFiles();
            for (int i = 0; i < listOfXml.length; i++) {
                xmlList.add(""+listOfXml[i]);
            }
        }
    }

    public String getTeiName() {
        return teiName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public NodeList xpathQueryNL(String expression) {
        NodeList str = null;

        if (type.equals("file")) {
            try {
                str = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
            } catch (XPathExpressionException ex) {
                Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return str;
    }

    public String xpathQuery(String expression) {
        String str = "error";

        if (type.equals("file")) {
            try {
                str = (String) xpath.evaluate(expression, doc, XPathConstants.STRING);
            } catch (XPathExpressionException ex) {
                Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return str;
    }

    public String xpathQuery(Document doc, String expression) {
        String retval = "error";
        try {
            retval = (String) xpath.evaluate(expression, doc, XPathConstants.STRING);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retval;
    }

    public String getTitle() {
        String retval = "error";

        if (type.equals("file")) {
            retval = xpathQuery("//title/text()");
        } else {
            try {
                try {
                    Document tmp = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(xmlList.get(0)));
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SAXException ex) {
                Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retval;
    }

    public String getAuthor() {
        String retval = "error";

        if (type.equals("file")) {
            retval = xpathQuery("//author/text()");
        }
        return retval;
    }

    public String getType() {
        return type;
    }

    public String getShortName() {

        String retval = "Nome breve";

        return retval;
    }

    public String getHover() {



        String retval ="error";

        TransformerFactory tFactory = TransformerFactory.newInstance();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String xml;
        String xsl = RgBConfiguration.stylesheetsDirectoryPath + "/hover.xsl";

        if (type.equals("file")) {
            xml = absolutePath;
        } else {
            xml = xmlList.get(0);
        }

        try {
            Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(xsl));
 
            transformer.transform(new javax.xml.transform.stream.StreamSource(xml),
                    new javax.xml.transform.stream.StreamResult(output));
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        /*try {
            try {
                InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(output.toString()));

                Document tmp = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is); //fa schifo
                retval = tmp.getElementsByTagName("div").item(0).getTextContent();

                retval = "info "+teiName;

            } catch (ParserConfigurationException ex) {
                Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SAXException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        }*/


        String ret = "encoding error";
        try {
            ret = output.toString("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;//.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ".length(), output.toString().length()-1);
    }

    public String firstFile(){
        String retval = "error";
        if (type.equals("file")) {
            retval = teiName+" file: "+absolutePath;
        }else{
            retval = teiName+" multifile: "+xmlList.get(0);
        }

        return retval;
    }
}
