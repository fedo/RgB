package it.unibo.cs.rgb.test;

import it.unibo.cs.rgb.tei.TeiDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.io.FileUtils;

/**
 *
 * @author fedo
 */
public class TeiTestInputStream {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String xmlString = "/Users/fedo/data/programming/netbeans/RgB/web/collection5/faith.xml";
        String xslString = "/Users/fedo/data/programming/netbeans/RgB/web/stylesheets/facility.xsl";

        File xmlFile = new File(xmlString);
        File xslFile = new File(xslString);

        if (xmlFile.exists() && xslFile.exists()) {
            //System.out.println("i files esistono");
        }

        HashMap xslHashMap = new HashMap();
        xslHashMap.put("/stylesheets/hover.xsl", FileUtils.readFileToString(xslFile));

        TeiDocument tei = new TeiDocument("zuppaditei", FileUtils.readFileToString(xmlFile), xslHashMap);

        //System.out.println(getContentType(new FileInputStream(xmlFile)));
        //System.out.println("nome " + tei.getTeiName());
        //System.out.println("path " + tei.getAbsolutePath());
        //System.out.println(tei.getHover());
        //System.out.println("view " + tei.getView("fasdfdfs"));

        //System.out.println(RgB.getXmlStreamEncoding(new FileInputStream(xmlFile)));
        //System.out.println("\n\n\nCCCCC\n\n\n");
        //System.out.println(RgB.convertXmlStreamToString(new FileInputStream(xmlFile)));
        //System.out.println("\n\n\nCCCCC\n\n\n");
        //System.out.println(FileUtils.readFileToString(xmlFile));
        //System.out.println(RgB.convertStreamToString(new FileInputStream(xmlFile), "UTF-8"));

        // parsing dei dati ricevuti
        ArrayList<HashMap> data = new ArrayList<HashMap>();
        //String svgDataString = tei.getSvgDataString();
        //String svgDataString = "der1 1 sigil1 id1" + "----" + "der2 0 sigil2 id2"; //tring "der", boolean "missing", String "sigil", String "id"
        String svgDataString = "null 0 a a" + "----" + "a 0 b b";

        String[] lines = svgDataString.split("----");
        for (int i = 0; i < lines.length; i++) {
            HashMap witnessMap = new HashMap();
            StringTokenizer tokens = new StringTokenizer(lines[i]);
            String der = tokens.nextToken();
            if (!der.equalsIgnoreCase("null")) {
                witnessMap.put("der", der);
            }
            witnessMap.put("missing", Boolean.parseBoolean(tokens.nextToken()));
            witnessMap.put("sigil", tokens.nextToken());
            witnessMap.put("id", tokens.nextToken());
            data.add(witnessMap);
        }

        //TeiSvg svg = new TeiSvg(FileUtils.readFileToString(xmlFile));
        //System.out.println("svg "+svg.getSvg());


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
                sb.append(line).append("\n");
                if ((sb.toString().contains("<?") && sb.toString().contains("?>")) && sb.toString().contains("encoding=")) {

                    Pattern p = Pattern.compile(".*<\\?.*encoding=.(.*).\\?>.*", Pattern.DOTALL);

                    Matcher matcher = p.matcher(sb.toString());

                    if (matcher.matches()) {
                        encoding = matcher.group(1);
                    }

                    break;
                }
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
                    System.out.println(line);
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "error";
        }
    }

    public static String getContentType(InputStream is) throws IOException {
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
                sb.append(line).append("\n");
                if ((sb.toString().contains("<?") && sb.toString().contains("?>")) && sb.toString().contains("encoding=")) {

                    Pattern p = Pattern.compile(".*<\\?.*encoding=.(.*).\\?>.*", Pattern.DOTALL);

                    Matcher matcher = p.matcher(sb.toString());

                    if (matcher.matches()) {
                        encoding = matcher.group(1);
                    }

                    break;
                }
            }

        }

        return encoding;
    }
}
