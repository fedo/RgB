package it.unibo.cs.rgb.test;


import it.unibo.cs.rgb.gwt.tei.TeiCollection;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 *
 * @author fedo
 */
public class TeiTest {

    public static void main(String[] args) {
        
        TeiCollection collection = new TeiCollection();
        collection.init("src/java/resources/collection1");
        ArrayList<String> alstr = new ArrayList<String>();
        
        for(int i = 0; i < collection.getNumberOfDocument(); i++){
            alstr.add(collection.getTeiDocument(i).getAbsolutePath());
            System.out.println(alstr.get(i));
        }
       System.out.println("la collezione contiene n elementi: "+collection.getNumberOfDocument());

        System.out.println(collection.getTeiDocument(0).getTitle());
        System.out.println(collection.getTeiDocument(0).getAuthor());
        System.out.println(collection.getTeiDocument(0).xpathQuery("//p"));
    }

}
