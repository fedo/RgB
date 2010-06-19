/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.test;

import it.unibo.cs.rgb.util.Wtw;
import java.util.StringTokenizer;

/**
 *
 * @author gine
 */
public class ConcordanzeTest {
  public static void main(String[] args){
    String parola = "urla";
    int numero = 3;
    String testo = "La nebbia agli irti colli piovvigginando sale e sotto il"+
                   " maestrale urla e biancheggia il mare. Ma per le vie del"+
                   " borgo dal ribollir dei tini va l'aspro odor dei vini, le"+
                   " anime a rallegrar . gira sui ceppi accesi lo spiedo scoppiettando,"+
                   " va il cacciator fischiando sull'uscio rimirar. Stormi di uccelli"+
                   " neri come esuli pensieri urla nel vespero migrar.";

    System.out.println("Test concordanze");

    StringTokenizer st = new StringTokenizer(testo);
    Wtw wtw = new Wtw("type");
    while (st.hasMoreTokens()) {
        String token=st.nextToken();
        String invalidChar=getInvalidChar(token);
        if(invalidChar == null) {
            wtw.addWord(token);
        } else {
            String[] splitWord=token.split("\\"+invalidChar);
            for (int sw=0; sw<splitWord.length; sw++) {
                wtw.addWord(splitWord[sw]);
            }
        }
    }
    
    int nWordsXWtw = wtw.getWords().size();
    int nConcordanze = 0;
    for (int t=0; t<nWordsXWtw ;t++) {
        if (wtw.getWords().get(t).matches(parola)) {
            String contesto = "";
            for (int j=numero;j>0;j--) {
                if (t-j >= 0)
                    if (j == 1)
                        contesto += wtw.getWords().get((t-j));
                    else
                        contesto += wtw.getWords().get((t-j))+" ";
                else
                    j = t; 
            }
            wtw.addBefore(contesto);

            contesto="";
            for (int j=1;j<=numero;j++){
                if (t+j <= nWordsXWtw )
                    if (j == numero || t == (nWordsXWtw -1))
                        contesto += wtw.getWords().get((t+j));
                    else
                        contesto += wtw.getWords().get((t+j))+" ";
                else
                    break;
            }
            wtw.addAfter(contesto);
            nConcordanze++;
        }
    }

    //OUT: System.out.println("<concordanze parola=\""+parola+"\" numero=\""+numero+"\">");
    System.out.println("    <concordanza type=\""+wtw.getType());
                                      // +"\" wit=\""+wtw.getSigil()
                                      // +"\" trasc=\""+wtw.getTrasc()+"\">" );

    for (int o=0;o<nConcordanze;o++){
        System.out.println("        <occorrenza n=\""+(o+1)+"\">");
        System.out.println("              <before>"+wtw.getBefore().get(o)+"</before>");
        System.out.println("              <after>"+wtw.getAfter().get(o)+"</after>");
        System.out.println("        </occorrenza>");
    }
    System.out.println("    </concordanza>");
    //OUT: System.out.println("<concordanze>");
}
  public static String getInvalidChar(String word){
      String invalidChar="!@#$%^&*()-_=+{}[]|\"\\':;<>,.?/~`";
      String ret=null;

      int nInvalidToken=invalidChar.length();
      for (int i=0; i<nInvalidToken;i++){
            if(word.indexOf(invalidChar.charAt(i)) > -1) {
                ret=""+invalidChar.charAt(i) ;
                break;
            }
      }

      return ret;
  }
}