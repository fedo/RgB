/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.gwt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fedo
 */
public class RgB {

    public String getBasename() {

        String basePublic = "/public/ltw1001";
        String base = "/Users/fedo/Temp"; //{ stylesheets, collection5 }
        String baseCs = "/home/web/ltw1001/html/RgB";

        String retval = "error";

        if (new File(basePublic).exists()) {
            retval = basePublic;
        }
        if (new File(baseCs).exists()) {
            retval = baseCs;
        } else if (new File(base).exists()) {
            retval = base;
        }


        return retval;
    }

    public static String convertStreamToString(InputStream is, String encoding) throws IOException {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */

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

    public static String getXmlStreamEncoding(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */

        String encoding = null;

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

    public static String convertXmlStreamToString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */

        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(Integer.MAX_VALUE);
        String encoding = "UTF-8"; //default
        String retval = null;

        // trova encoding
        if (bis != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "UTF-8"));//"UTF-8"));
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

        bis.reset();

        // converte
        if (bis != null) {
            StringBuilder sb = new StringBuilder();
            String line;


            BufferedReader reader = new BufferedReader(new InputStreamReader(bis, encoding));//"UTF-8"));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            retval = sb.toString();
        }

        return retval;
    }
}
