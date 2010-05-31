package it.unibo.cs.rgb.test;

import it.unibo.cs.rgb.gwt.RgBConfiguration;
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
        collection.init(RgBConfiguration.collectionDirectoryPath);
        ArrayList<String> alstr = new ArrayList<String>();

        for (int i = 0; i < collection.getNumberOfDocument(); i++) {
            System.out.println("------------------");
            alstr.add(collection.getTeiDocument(i).getAbsolutePath());
            //System.out.println(collection.getTeiDocument(i).getTitle());
            //System.out.println(collection.getTeiDocument(i).getAuthor());
            System.out.println(collection.getTeiDocument(i).firstFile());
            System.out.println(alstr.get(i));
            System.out.println(collection.getTeiDocument(i).getHover());

        }
        System.out.println("-----------------");
        System.out.println("la collezione contiene n elementi: " + collection.getNumberOfDocument());

    }
}
