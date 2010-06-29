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
    //public String getConcordanze(String parola, String testo, int numero) {

    private void cleanTextAndGetType() {

        String[] text = plainText.split("\\|");
        this.wtw = new Wtw(text[0]);

        StringTokenizer st = new StringTokenizer(text[1]);

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
                    if (t - j < 0) {
                        j = t;
                    }

                    if (j == 1) {
                        contesto += wtw.getWords().get((t - j));
                    } else {
                        contesto += wtw.getWords().get((t - j)) + " ";
                    }
                }
                wtw.addBefore(contesto);

                contesto = "";
                for (int j = 1; j <= numero; j++) {
                    if (t + j < nWordsXWtw) {
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

        cleanTextAndGetType();
        int nConcordanze = makeConcordanze();

        if (nConcordanze > 0) {

            /*ret +="<p>\n";
            //ret +="<span>finded in: "+wtw.getType()+"</span><br/>\n";
            ret +="<span>Concordanze trovate nella versione del testimone: "+sigil+"</span><br/>\n";
            //ret +="<span>trascription: "+trascription+"</span><br/>\n";
            if (nConcordanze > 0) {
            ret += "<ul>\n";
            for (int o = 0; o < nConcordanze; o++) {
            ret += "<li>\n"+(o+1)+" <b>parole prima</b>: "+wtw.getBefore().get(o)+" \n";
            ret += "<b>parole dopo</b>: "+wtw.getAfter().get(o)+"</li>\n";
            }
            ret += "</ul><br/>\n";
            }
            //ret += "<span>numero concordanze:"+nConcordanze+"</span><br/>\n</p>\n";
             */

            ret += "<p>\n<div>";
            ret += "<span id=\"" + wtw.getType() + "\">type</span><br/>\n";
            ret += "<span id=\"" + sigil + "\">sigil</span><br/>\n";
            ret += "<span id=\"" + trascription + "\">sigil</span><br/>\n<ul>\n";

            for (int o = 0; o < nConcordanze; o++) {
                ret += "<li>\n<span id=\"" + wtw.getBefore().get(o) + "\">sotto il maestrale</span><br/>\n";
                ret += "<span id=\"" + wtw.getAfter().get(o) + "\">sotto il maestrale</span><br/></li>\n";
            }
            ret += "</ul></div>\n";
        }

        return ret;
    }
}
