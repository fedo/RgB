package it.unibo.cs.rgb.tei;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.saxon.s9api.SaxonApiException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.SAXException;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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
    private String stylesheetsPath;
    private InputStream teiStream;
    private String teiString;
    private HashMap xsl;

    public TeiDocument(String absolutePath, String tei, HashMap xsl) {

        this.teiString = ignoreDTD(tei);
        this.xsl = xsl;
        this.absolutePath = absolutePath;
        this.teiName = absolutePath;

    }

    public TeiDocument(String tei) {

        this.teiString = ignoreDTD(tei);

    }

    public TeiDocument(String absolutePath, String stylePath) {

        stylesheetsPath = stylePath;
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
                javax.xml.parsers.DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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

            File[] listOfXml = folder.listFiles();
            for (int i = 0; i < listOfXml.length; i++) {
                xmlList.add("" + listOfXml[i]);
            }
        }
    }

    public TeiDocument(InputStream stream) {
        try {
            javax.xml.parsers.DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(stream);
            xpath = XPathFactory.newInstance().newXPath();
        } catch (SAXException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTeiName() {
        return teiName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public NodeList xpathQueryNL(String xpath) {
        NodeList ret = null;

        return ret;
    }

    public NodeList xpathQuerySaxNL() {
        NodeList str = null;

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

    public String getTeiString() {
        return teiString;
    }

    public String ignoreDTD(String tei) {
        int start = tei.indexOf("<!D");
        int end = tei.indexOf("<TE");
        String retval = tei; // se non c'è il doctype da eliminare, restituisci la stessa stringa

        if (start > -1 && end > -1) {
            retval = tei.replace(tei.substring(start, end), " ");
        }
        return retval;
    }

    public String getHover() {

        return xslt("/stylesheets/hover.xsl");
    }

    public String getInfo() {

        return xslt("/stylesheets/preface.xsl");
    }

    public String getSvgDataString() {

        return xslt("/stylesheets/facility.xsl");
    }

    public String getEstrazioneDiConcordanzeDataString(String witness) {

        return xslt("/stylesheets/content_text.xsl", witness);
    }

    public String getView(String witness) {

        return xslt("/stylesheets/content.xsl", witness);
    }

    public String xslt(String stylesheetFile) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        StringReader xmlStringReader = new StringReader(teiString);
        String xslString = (String) xsl.get(stylesheetFile);
        StringReader xslStringReader = new StringReader(xslString);

        try {
            Processor proc = new Processor(false);
            XsltCompiler comp = proc.newXsltCompiler();
            XsltExecutable exp = comp.compile(new StreamSource(xslStringReader));
            XdmNode source = proc.newDocumentBuilder().build(new StreamSource(xmlStringReader));
            Serializer out = new Serializer();
            out.setOutputStream(outputStream);
            XsltTransformer trans = exp.load();
            trans.setInitialContextNode(source);
            trans.setDestination(out);
            trans.transform();
        } catch (SaxonApiException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        String retval = "encoding error";
        try {
            retval = outputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retval;//.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ".length(), output.toString().length()-1);
    }

    public String xslt(String stylesheet, String witness) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        StringReader xmlStringReader = new StringReader(teiString);
        String xslString = (String) xsl.get(stylesheet);
        StringReader xslStringReader = new StringReader(xslString);

        try {
            Processor proc = new Processor(false);
            XsltCompiler comp = proc.newXsltCompiler();
            comp.setSchemaAware(false);
            XsltExecutable exp = comp.compile(new StreamSource(xslStringReader));
            XdmNode source = proc.newDocumentBuilder().build(new StreamSource(xmlStringReader));
            Serializer out = new Serializer();
            out.setOutputStream(outputStream);
            XsltTransformer trans = exp.load();
            trans.setInitialContextNode(source);
            trans.setDestination(out);
            trans.setParameter(new QName("witNum"), new XdmAtomicValue(witness)); //"witNum", a666
            trans.transform();
        } catch (SaxonApiException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        String ret = "encoding error";
        try {
            ret = outputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;//.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ".length(), output.toString().length()-1);
    }

    public String[] getWitnessesList() {

        String str = xslt("/stylesheets/witList.xsl");
        //str = str.substring(0, str.indexOf("|"));
        String[] tokens = str.split(" ");
        
        if (tokens[0].equalsIgnoreCase("")) {
            tokens[0] = "default";
        }

        return tokens;

    }

    public HashMap getCatalogData(String service) {

        String str = xslt("/stylesheets/catalog_elements.xsl");
        //String str = "DiffOntologico@POST@text/xml@application/rdf+xml|EstrazioneDiConcordanze@POST@text/xml@text/html|StemmaCodicum@POST@text/xml@text/xml";
        HashMap retval = new HashMap();

        String[] services = str.split("\\|");
        for (int n = 0; n < services.length; n++) {
            String[] data = services[n].split("@");

            if (data[0].contains(service)) {
                retval.put("uri", data[0]);
                retval.put("method", data[1]);
                retval.put("input", data[2]);
                retval.put("output", data[3]);
                for (int m = 4; m < data.length; m++) {
                    retval.put("param" + (m - 3), data[m]);
                }
            }
        }
        return retval;
    }

    public String getDifferenziazioneData(){
        return xslt("/stylesheets/content_text.xsl", ""); //TODO
    }
}
