/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.gwt.server;

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
        ArrayList<String> teiDocuments = new ArrayList<String>();
        teiDocuments.add("Info primo documento tei");
        teiDocuments.add("Info secondo documento tei");
        teiDocuments.add("Info terzo documento tei");
        return teiDocuments;
    }
}
