/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.tei;

import it.unibo.cs.rgb.util.Wtw;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author gine
 */
public class ConcordanzeTest {
  public static void main(String[] args){
    ArrayList<String> prova= new ArrayList<String>();
    String parola = "urla";
    int numero = 3;
    prova.add("La nebbia agli irti colli piovvigginando sale e sotto il"+
              " maestrale urla e biancheggia il mare. Ma per le vie del"+
              " borgo dal ribollir dei tini va l'aspro odor dei vini, le");

    prova.add(" anime a rallegrar . gira sui ceppi accesi lo spiedo scoppiettando,"+
              " va il cacciator fischiando sull'uscio rimirar. Stormi di uccelli"+
              " neri come esuli pensieri nel vespero migrar.");

    System.out.println("Test concordanze");

    List<Wtw> wtw = new ArrayList<Wtw>();
    for (int wit=0; wit<prova.size();wit++){
        StringTokenizer st = new StringTokenizer(prova.get(wit));
        wtw.add(new Wtw(""+wit,"pirla"));
        while (st.hasMoreTokens()) {
            String token=st.nextToken();
            String invalidChar=getInvalidChar(token);
            if(invalidChar == null) {
                wtw.get(wit).addWord(token);
            } else {
                String[] splitWord=token.split("\\"+invalidChar);
                for (int sw=0; sw<splitWord.length; sw++) {
                    wtw.get(wit).addWord(splitWord[sw]);
                }
            }
        }
    }

    for (int w=0; w<wtw.size(); w++) {
        int nWordsXWtw = wtw.get(w).getWords().size();
        for (int t=0; t<nWordsXWtw ;t++) {
          if (wtw.get(w).getWords().get(t).matches(parola)) {
              for (int j=numero;j>0;j--) {
                  if (t-j >= 0) 
                      wtw.get(w).addBefore(wtw.get(w).getWords().get((t-j)));
                  else
                      j = t; 
              }

              for (int j=1;j<=numero;j++){
                  if (t+j <= nWordsXWtw )
                      wtw.get(w).addAfter(wtw.get(w).getWords().get((t+j)));
                  else
                      break;
              }
          }
        }
    }

    //DEBUG word x type
    /*for (int w=0;w<wtw.size();w++){
        for (int i=0; i<wtw.get(w).getWords().size();i++)
            System.out.println("w"+w+" "+wtw.get(w).getWords().get(i));
    }*/

    //DEBUG N parole prima e dopo a P parola
    for (int w=0;w<wtw.size();w++){
        System.out.println("w:"+w+" before");
        for (int a=0; a<wtw.get(w).getBefore().size();a++)
            System.out.println(wtw.get(w).getBefore().get(a));

        System.out.println("parola:" + parola);

        System.out.println("w:"+w+" after");
        for (int b=0; b<wtw.get(w).getAfter().size();b++)
            System.out.println(wtw.get(w).getAfter().get(b));
    }
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