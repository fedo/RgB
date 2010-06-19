/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fedo
 */
public class RgBServletException extends ServletException {

    RgBServletException(HttpServletResponse response, String code, String shortDescription, String description, String tip) throws IOException {
        
            PrintWriter out = response.getWriter();

            response.setContentType("application/json");
            response.setStatus(200);

            /*
            {
            code : 'error code',
            short : 'A short error description',
            description : 'A verbose error description',
            tip : 'One or more tips to resolve the error'
            }
             */

            String message = "";
            message += "{";
            message += "code: '" + code + "'";
            message += "short: '" + shortDescription + "'";
            message += "description : '" + description + "'";
            message += "tip: '" + tip + "'";
            message += "}";

            out.print(message);


    }
}
