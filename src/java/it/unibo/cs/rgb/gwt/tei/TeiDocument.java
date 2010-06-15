package it.unibo.cs.rgb.gwt.tei;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.saxon.s9api.SaxonApiException;
import org.w3c.dom.*;
import javax.xml.xpath.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;

import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

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
                xmlList.add("" + listOfXml[i]);
            }
        }
    }

      public TeiDocument(InputStream stream) {
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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

public NodeList xpathQueryNL(String expression) {
        NodeList str = null;

            try {
                str = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
            } catch (XPathExpressionException ex) {
                Logger.getLogger(TeiDocument.class.getName()).log(Level.SEVERE, null, ex);
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

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        String inputXsl = stylesheetsPath + "/hover.xsl";
        String inputXml;
        if (type.equals("file")) {
            inputXml = absolutePath;
        } else {
            inputXml = xmlList.get(0);
        }

        try {
            Processor proc = new Processor(false);
            XsltCompiler comp = proc.newXsltCompiler();
            XsltExecutable exp = comp.compile(new StreamSource(new File(inputXsl)));
            XdmNode source = proc.newDocumentBuilder().build(new StreamSource(new File(inputXml)));
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

    public String getInfo() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String retval = "error";

        String inputXsl = stylesheetsPath + "/preface.xsl";
        String inputXml;
        if (type.equals("file")) {
            inputXml = absolutePath;
        } else {
            inputXml = xmlList.get(0);
        }

        try {
            Processor proc = new Processor(false);
            XsltCompiler comp = proc.newXsltCompiler();
            comp.setSchemaAware(false);
            XsltExecutable exp = comp.compile(new StreamSource(new File(inputXsl)));
            XdmNode source = proc.newDocumentBuilder().build(new StreamSource(new File(inputXml)));
            Serializer out = new Serializer();
            out.setOutputStream(outputStream);
            XsltTransformer trans = exp.load();
            trans.setInitialContextNode(source);
            trans.setDestination(out);
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

    public String getView(String witness) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String retval = "error";

        String inputXsl = stylesheetsPath + "/content.xsl";
        String inputXml;
        if (type.equals("file")) {
            inputXml = absolutePath;
        } else {
            inputXml = xmlList.get(0);
        }

        try {
            Processor proc = new Processor(false);
            XsltCompiler comp = proc.newXsltCompiler();
            comp.setSchemaAware(false);
            XsltExecutable exp = comp.compile(new StreamSource(new File(inputXsl)));
            XdmNode source = proc.newDocumentBuilder().build(new StreamSource(new File(inputXml)));
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

    public String firstFile() {
        String retval = "error";
        if (type.equals("file")) {
            retval = teiName + " file: " + absolutePath;
        } else {
            retval = teiName + " multifile: " + xmlList.get(0);
        }

        return retval;
    }
}
