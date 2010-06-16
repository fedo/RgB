package it.unibo.cs.rgb.test;

import it.unibo.cs.rgb.gwt.RgB;
import it.unibo.cs.rgb.tei.TeiCollection;
import java.util.ArrayList;

/**
 *
 * @author fedo
 */
public class TeiTest {

    public static void main(String[] args) {



        TeiCollection collection = new TeiCollection(new RgB().getBasename()+"/stylesheets");
        collection.init(new RgB().getBasename()+"/collection5");
        ArrayList<String> alstr = new ArrayList<String>();

        for (int i = 0; i < collection.getNumberOfDocument(); i++) {
            System.out.println("------------------");
            alstr.add(collection.getTeiDocument(i).getAbsolutePath());
            //System.out.println(collection.getTeiDocument(i).getTitle());
            //System.out.println(collection.getTeiDocument(i).getAuthor());
            System.out.println(collection.getTeiDocument(i).firstFile());
            System.out.println(alstr.get(i));
            System.out.println(collection.getTeiDocument(i).getView("asd"));

        }
        System.out.println("-----------------");
        System.out.println("la collezione contiene n elementi: " + collection.getNumberOfDocument());

        String splitta = "a, b, e, e, f";

    }
}
