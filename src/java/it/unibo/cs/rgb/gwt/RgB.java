/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.gwt;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author fedo
 */

public class RgB {

    public  String getBasename(){

        String basePublic = "/public/ltw1001";
        String base = "/Users/fedo/Temp"; //{ stylesheets, collection5 }
        String baseCs = "/home/web/ltw1001/html/RgB";

        String retval = "error";

        if(new File(basePublic).exists())
            retval = basePublic;
        if(new File(baseCs).exists())
            retval = baseCs;
        else if(new File(base).exists())
            retval = base;
        

        return retval;
    }

}
