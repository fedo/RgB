/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.gwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;

/**
 *
 * @author fedo
 */
@RemoteServiceRelativePath("gwtservicetei")
public interface GWTServiceTei extends RemoteService {
    public String myMethod(String s);
    public String testMethod();
    public ArrayList getTeiInfo();
}
