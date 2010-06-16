package it.unibo.cs.rgb.test;


import it.unibo.cs.rgb.tei.TeiDocument;
import it.unibo.cs.rgb.tei.TeiSvg;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


import org.apache.commons.io.FileUtils;

/**
 *
 * @author fedo
 */
public class TeiTestInputStream {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String xmlString = "/Users/fedo/data/programming/netbeans/RgB/web/collection5/autumn.xml";
        String xslString = "/Users/fedo/data/programming/netbeans/RgB/web/stylesheets/hover.xsl";

        File xmlFile = new File(xmlString);
        File xslFile = new File(xslString);

        if (xmlFile.exists() && xslFile.exists()) {
            System.out.println("i files esistono");
        }

        HashMap xslHashMap = new HashMap();
        xslHashMap.put("/stylesheets/hover.xsl", FileUtils.readFileToString(xslFile));

        TeiDocument tei = new TeiDocument("zuppaditei", FileUtils.readFileToString(xmlFile), xslHashMap);


        System.out.println("nome " + tei.getTeiName());
        System.out.println("path " + tei.getAbsolutePath());
        System.out.println("xsl " + tei.getHover());
        
        TeiSvg svg = new TeiSvg(FileUtils.readFileToString(xslFile));
        System.out.println(svg.getSvg());


    }

    public static String convertStreamToString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
