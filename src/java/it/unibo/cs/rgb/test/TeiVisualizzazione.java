/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.test;

import it.unibo.cs.rgb.tei.TeiDocument;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author fedo
 */
public class TeiVisualizzazione {

    public static void main(String[] args) throws IOException {

        //INPUT
        String xmlFolder = "/Users/fedo/data/programming/netbeans/RgB/web/collection6"; // path della collezione da trasformare
        String xslFolder = "/Users/fedo/data/programming/netbeans/RgB/web/stylesheets"; // path della cartella contenente i files xsl
        File file = new File("/Users/fedo/Desktop/output.html"); // path dell'output

        File xmlFile = new File(xmlFolder);
        File[] xmlFiles = xmlFile.listFiles();

        File[] xslFiles = new File(xslFolder).listFiles();

        HashMap xslHashMap = new HashMap();
        for (int i = 0; i < xslFiles.length; i++) {
            if (xslFiles[i].getName().endsWith(".xsl")) {
                xslHashMap.put("/stylesheets/" + xslFiles[i].getName(), FileUtils.readFileToString(xslFiles[i]));

            }
        }

        String output = "<table width=\"" + (300 * xmlFiles.length) + "\"><tr>";

        for (int n = 0; n < xmlFiles.length; n++) {
            if (xmlFiles[n].getAbsolutePath().endsWith(".xml")) {

                System.out.println("xml file: " + xmlFiles[n].getAbsolutePath());


                TeiDocument tei = new TeiDocument(xmlFiles[n].getName(), FileUtils.readFileToString(xmlFiles[n]), xslHashMap);

                //System.out.println("xml "+tei.getHover());

                String witnessesString = tei.xslt("/stylesheets/witness_transcription.xsl");

                ArrayList<String> wit = new ArrayList<String>();
                if (!witnessesString.equalsIgnoreCase("")) {
                    witnessesString = witnessesString.substring(0, witnessesString.indexOf("|"));
                    String[] witlist = witnessesString.split("-");
                    for (int z = 0; z < witlist.length; z++) {
                        output += "<td valign=\"top\" width=\"300\">SPACESPACESPACESPACE<br/><b>FILE</b><br/>"+xmlFiles[n].getName()+"<br/><b>WITNESS</b><br/>"+witlist[z]+"\n";
                        output += tei.getView(witlist[z]);
                        output += "</td>";
                        System.out.println(witlist[z]);
                    }

                }


            }
        }

        output += "</tr></table>";

        FileUtils.writeStringToFile(file, output);
    }
}
