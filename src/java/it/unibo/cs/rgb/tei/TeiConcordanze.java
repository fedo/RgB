package it.unibo.cs.rgb.tei;

import it.unibo.cs.rgb.util.Wtw;
import java.util.StringTokenizer;

/**
 *
 * @author gine
 */
public class TeiConcordanze {
    String parola, plainText, sigil, trascription;
    int numero;
    Wtw wtw;

    public TeiConcordanze(String parola, int numero, String plainText, String sigil, String trascription) {
        this.parola = parola;
        this.numero = numero;
        this.plainText = plainText;
        this.sigil = sigil;
        this.trascription = trascription;
    }

    private void pippo(){
        String type="", testo="";
        wtw.setType(type);
        cleanText(testo);
    }
    //public String getConcordanze(String parola, String testo, int numero) {
    private void cleanText(String testo) {
        StringTokenizer st = new StringTokenizer(testo);

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String invalidChar = getInvalidChar(token);
            if (invalidChar == null) {
                wtw.addWord(token);
            } else {
                String[] splitWord = token.split("\\" + invalidChar);
                for (int sw = 0; sw < splitWord.length; sw++) {
                    wtw.addWord(splitWord[sw]);
                }
            }
        }
    }

    private String getInvalidChar(String word) {
        String invalidChar = "!@#$%^&*()-_=+{}[]|\"\\':;<>,.?/~`";
        String ret = null;

        int nInvalidToken = invalidChar.length();
        for (int i = 0; i < nInvalidToken; i++) {
            if (word.indexOf(invalidChar.charAt(i)) > -1) {
                ret = "" + invalidChar.charAt(i);
                break;
            }
        }

        return ret;
    }

    private int makeConcordanze() {
        int nWordsXWtw = wtw.getWords().size();
        int nConcordanze = 0;
        for (int t = 0; t < nWordsXWtw; t++) {
            if (wtw.getWords().get(t).matches(parola)) {
                String contesto = "";
                for (int j = numero; j > 0; j--) {
                    if (t - j >= 0) {
                        if (j == 1) {
                            contesto += wtw.getWords().get((t - j));
                        } else {
                            contesto += wtw.getWords().get((t - j)) + " ";
                        }
                    } else {
                        j = t;
                    }
                }
                wtw.addBefore(contesto);

                contesto = "";
                for (int j = 1; j <= numero; j++) {
                    if (t + j <= nWordsXWtw) {
                        if (j == numero || t == (nWordsXWtw - 1)) {
                            contesto += wtw.getWords().get((t + j));
                        } else {
                            contesto += wtw.getWords().get((t + j)) + " ";
                        }
                    } else {
                        break;
                    }
                }
                wtw.addAfter(contesto);
                nConcordanze++;
            }
        }

        return nConcordanze;
    }

    public String getConcordanze() {
        String ret = "";

        pippo();
        int nConcordanze = makeConcordanze();

        ret +="<p>\n<div>";
        ret +="<span id=\""+wtw.getType()+"\">type</span><br/>\n";
        ret +="<span id=\""+sigil+"\">sigil</span><br/>\n";
        ret +="<span id=\""+trascription+"\">sigil</span><br/>\n<ul>\n";

        for (int o = 0; o < nConcordanze; o++) {
            ret += "<li>\n<span id=\""+ wtw.getBefore().get(o) +"\">sotto il maestrale</span><br/>\n";
            ret += "<span id=\""+ wtw.getAfter().get(o) +"\">sotto il maestrale</span><br/></li>\n";
        }
        ret += "</ul></div>\n";

        return ret;
    }
}
