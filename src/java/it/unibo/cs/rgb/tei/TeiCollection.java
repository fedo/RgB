package it.unibo.cs.rgb.tei;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author fedo
 */
public class TeiCollection {

    private String folderPath;
    private ArrayList<TeiDocument> listOfTei;
    private String stylesheetsPath;

    public TeiCollection(String stylePath) {
        this.stylesheetsPath = stylePath;
        folderPath = "toInitialize";
    }

    public void init(String folderPath) {
        this.folderPath = folderPath;
        listOfTei = new ArrayList<TeiDocument>();

        fillList(this.folderPath, 0);


    }

    /**
     * Aggiorna la lista dei TeiDocument contenuti nella collezione
     */
    public void refresh() {

        fillList(folderPath, 0);

    }

    /**
     * Dato un path visita ricorsivamente la cartella e aggiunge i file XML trovati al listOfTei
     * @param path l'indirizzo della collezione da
     */
    private void fillList(String path, int deep) {

        File folder = new File(path);
        if (folder.exists()) {
            File[] listOfFiles = folder.listFiles();
            ArrayList<TeiDocument> tmp = new ArrayList<TeiDocument>();


            for (int i = 0; i < listOfFiles.length; i++) {
                String filename = listOfFiles[i].getName();
                if (listOfFiles[i].isFile()) {
                    //file xml
                    if (filename.endsWith(".xml")) {
                        tmp.add(new TeiDocument(path + "/" + filename, stylesheetsPath));
                    }
                } else if (listOfFiles[i].isDirectory()) {
                    //cartella con documenti xml
                    //tmp.add(new TeiDocument(path + "/" + filename));
                    fillList(path + "/" + filename, deep + 1);
                }

            }
            for (int i = 0; i < tmp.size(); i++) {
                listOfTei.add(tmp.get(i));
            }
        }





        /*for (int i = 0; i < listOfFiles.length; i++) {
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

        }*/

    }

    /**
     * Numero di TeiDocument contenuti nella collezione
     * @return numero di TeiDocument
     */
    public int getNumberOfDocument() {

        return listOfTei.size();

    }

    public TeiDocument getTeiDocument(int n) {
        return listOfTei.get(n);
    }
}
