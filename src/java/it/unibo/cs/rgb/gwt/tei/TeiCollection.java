package it.unibo.cs.rgb.gwt.tei;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 *
 * @author fedo
 */
public class TeiCollection {

    private static String folderPath;
    private static ArrayList<TeiDocument> listOfTei = new ArrayList<TeiDocument>();

    public TeiCollection() {
        folderPath = "toInitialize";
    }

    public void init(String folderPath){
                this.folderPath = folderPath;

            fillList(this.folderPath, 0);


    }

    /**
     * Aggiorna la lista dei TeiDocument contenuti nella collezione
     */
    public static void refresh(){

            fillList(folderPath, 0);

    }

    /**
     * Dato un path visita ricorsivamente la cartella e aggiunge i file XML trovati al listOfTei
     * @param path l'indirizzo della collezione da
     */
    private static void fillList(String path, int deep) {

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();


        for (int i = 0; i < listOfFiles.length; i++) {
            String filename = listOfFiles[i].getName();
            //System.out.println("DEBUG TeiCollection: processing "+path+"/"+filename);
            if (listOfFiles[i].isFile()) {
                if (filename.endsWith(".xml")) {
                    //System.out.println("DEBUG TeiCollection: found "+filename);
                    listOfTei.add(new TeiDocument(path +"/"+filename));
                    int zzz;
                }
            } else if (listOfFiles[i].isDirectory()) {
                fillList(path + "/" + filename, deep + 1);
            }
            
        }

    }

    /**
     * Numero di TeiDocument contenuti nella collezione
     * @return numero di TeiDocument
     */
    public int getNumberOfDocument() {

        return listOfTei.size();

    }

    public TeiDocument getTeiDocument(int n){
        return listOfTei.get(n);
    }

}
