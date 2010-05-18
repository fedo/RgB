/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.test;


import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

import java.util.Iterator;
/**
 *
 * @author fedo
 */
public class ListTeiTest {

    private static final String JSON_URL = "http://localhost:8080/RgB/Tei";

    public static void main(String[] args) {
        System.out.println("Test lista tei");
        String url = JSON_URL;
        url = URL.encode(url);

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

        try {
            Request request = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    //displayError("Couldn't retrieve JSON");
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        try {
                            // parse the response text into JSON
                            JSONValue jsonValue = JSONParser.parse(response.getText());
                            JSONArray jsonArray = jsonValue.isArray();

                            if (jsonArray != null) {
                                System.out.println("Ricevo");
                                //updateTable(jsonArray);
                            } else {
                                throw new JSONException();
                            }
                        } catch (JSONException e) {
                            //displayError("Could not parse JSON");
                        }
                    } else {
                        //displayError("Couldn't retrieve JSON (" + response.getStatusText() + ")");
                    }
                }
            });
        } catch (RequestException e) {
            //displayError("Couldn't retrieve JSON");
        }



    }

}
