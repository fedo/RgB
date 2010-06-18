package it.unibo.cs.rgb.test;

import it.unibo.cs.rgb.gwt.RgB;
import it.unibo.cs.rgb.tei.TeiCollection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            //System.out.println(collection.getTeiDocument(i).firstFile());
            //System.out.println(alstr.get(i));
            //System.out.println(collection.getTeiDocument(i).getView("asd"));

        }
        System.out.println("-----------------");
        System.out.println("la collezione contiene n elementi: " + collection.getNumberOfDocument());

        String splitta = "a, b, e, e, f";

    }

    public static String convertStreamToString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */

        String encoding = "UTF-8"; //default


        // cerca se il documento specifica un altro encoding
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));//"UTF-8"));
            while ((line = reader.readLine()) != null) { //CRASHA
                /*sb.append(line).append("\n");
                if ((sb.toString().contains("<?") && sb.toString().contains("?>")) && sb.toString().contains("encoding=")) {

                    Pattern p = Pattern.compile(".*<\\?.*encoding=.(.*).\\?>.*", Pattern.DOTALL);

                    Matcher matcher = p.matcher(sb.toString());

                    if (matcher.matches()) {
                        encoding = matcher.group(1);
                    }

                    break;
                }*/
            }

        }

        // converte
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding));//"UTF-8"));
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
