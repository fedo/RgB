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
    protected String id = null;
    protected String trasc = null;
    protected final List<String> words = new ArrayList<String>();
    protected final List<String> before = new ArrayList<String>();
    protected final List<String> after = new ArrayList<String>();

    public Wtw(String id, String trasc){
        this.id = id;
        this.trasc = trasc;
    }

    public String getId(){
        return id;
    }

    public String getTrasc(){
        return trasc;
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

    public void setId(String id){
        this.id = id;
    }

    public void setTrasc(String trasc){
        this.trasc = trasc;
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
