/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

/**
 *
 * @author fedo
 */
public interface GWTServiceTeiAsync {
    public void myMethod(String s, AsyncCallback<String> callback);

    public void testMethod(AsyncCallback<String> asyncCallback);

    public void getTeiInfo(AsyncCallback<ArrayList> asyncCallback);
}
