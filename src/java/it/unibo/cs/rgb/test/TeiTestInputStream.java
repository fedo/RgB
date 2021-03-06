package it.unibo.cs.rgb.test;

import it.unibo.cs.rgb.gwt.RgB;
import it.unibo.cs.rgb.tei.TeiDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.io.FileUtils;

/**
 *
 * @author fedo
 */
public class TeiTestInputStream {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String xmlString = "/Users/fedo/data/programming/netbeans/RgB/web/collection6/autumn.xml";//"/Users/fedo/data/programming/netbeans/RgB/web/collection5/faith.xml";
        String xslname = "/stylesheets/content.xsl";
        String xslString = "/Users/fedo/data/programming/netbeans/RgB/web"+xslname;


        String catalogLTW1001 = "http://vitali.web.cs.unibo.it/twiki/pub/TechWeb10/GruppoLTW03/catalogo_ltw03.xml"; //"http://vitali.web.cs.unibo.it/twiki/pub/TechWeb10/GruppoLTW03/catalogo_ltw03.xml";
        URLConnection connectionLTW1001 = new URL(catalogLTW1001).openConnection();

        File xmlFile = new File(xmlString);

        FileInputStream fis = new FileInputStream(xmlFile);
        String ex = RgB.getXmlStreamEncoding(fis);
        System.out.println(ex);
        
        File xslFile = new File(xslString);

        if (xmlFile.exists() && xslFile.exists()) {
            //System.out.println("i files esistono");
        }

        HashMap xslHashMap = new HashMap();
        xslHashMap.put(xslname, FileUtils.readFileToString(xslFile));

         //TeiDocument dataCatalogLTW1001 = new TeiDocument("", RgB.convertStreamToString(connectionLTW1001.getInputStream(),"UTF-8"), xslHashMap);

        TeiDocument tei = new TeiDocument("zuppaditei", FileUtils.readFileToString(xmlFile), xslHashMap);
        String str = tei.xslt(xslname, "a2", "visualizzazione");

        System.out.println("xslt "+str);
        //System.out.println((String) dataCatalogLTW1001.getCatalogData("Ind").get("uri"));
        //System.out.println((String) dataCatalogLTW1001.getCatalogData("Ind").get("input"));
        //System.out.println((String) dataCatalogLTW1001.getCatalogData("Ind").get("output"));

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
        //ArrayList<HashMap> data = new ArrayList<HashMap>();
        //String svgDataString = tei.getSvgDataString();
        //String svgDataString = "der1 1 sigil1 id1" + "----" + "der2 0 sigil2 id2"; //tring "der", boolean "missing", String "sigil", String "id"
        //String svgDataString = "null 0 a a" + "----" + "a 0 b b";

        /*str = str.substring(0, str.indexOf("|"));
        System.out.println(str);
        String[] lines = str.split("-");

        for (int i = 0; i < lines.length; i++) {

            System.out.println(lines[i]);
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
        }*/

        //TeiSvg svg = new TeiSvg(data);
        //System.out.println("svg "+svg.getSvg());

        //System.out.println("γδεζηθικλμνξο");


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
