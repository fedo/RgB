/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gine
 */
public class Wtw {
    //protected String sigil = null;
    //protected String trasc = null;
    protected String type = null;
    protected int nConcordanze = 0;
    protected final List<String> words = new ArrayList<String>();
    protected final List<String> before = new ArrayList<String>();
    protected final List<String> after = new ArrayList<String>();

    public Wtw(/*String sigil, String trasc,*/ String type){
    //   this.sigil = sigil;
    //    this.trasc = trasc;
        this.type = type;
    }

    //public String getSigil(){
    //    return sigil;
    //}

    //public String getTrasc(){
    //    return trasc;
    //}

    public String getType(){
        return type;
    }

    public int getConcordanze(){
        return nConcordanze;
    }

    public List<String> getWords(){
        return words;
    }

    public List<String> getBefore(){
        return before;
    }

    public List<String> getAfter(){
        return after;
    }

    //public void setSigil(String sigil){
    //   this.sigil = sigil;
    //}

    //public void setTrasc(String trasc){
    //    this.trasc = trasc;
    //}

    public void setType(String type){
        this.type = type;
    }

    public void setConcordanze(int concordanze){
        this.nConcordanze = concordanze;
    }

    public void addWord(String word){
        words.add(word);
    }

    public void addBefore(String word){
        before.add(word);
    }

    public void addAfter(String word){
        after.add(word);
    }
}
