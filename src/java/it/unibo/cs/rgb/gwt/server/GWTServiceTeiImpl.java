/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.gwt.server;

import com.google.gwt.http.client.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import it.unibo.cs.rgb.gwt.client.GWTServiceTei;

import java.util.ArrayList;

/**
 *
 * @author fedo
 */
public class GWTServiceTeiImpl extends RemoteServiceServlet implements GWTServiceTei {
    public String myMethod(String s) {
        // Do something interesting with 's' here on the server.
        return "Server says: " + s;
    }

    public String testMethod(){
        return "GWTServiceTei Test :)";
    }

    public ArrayList getTeiInfo() {
        final ArrayList<String> teiDocuments = new ArrayList<String>();
        teiDocuments.add("Info primo documento tei");
        teiDocuments.add("Info secondo documento tei");
        teiDocuments.add("Info terzo documento tei");

        /*
        ArrayList<String> alstr = new ArrayList<String>();

        for(int i = 0; i < collection.getNumberOfDocument(); i++){
            alstr.add(collection.getTeiDocument(i).getAbsolutePath());
            teiDocuments.add(alstr.get(i));
        }*/

        return teiDocuments;
    }
}
