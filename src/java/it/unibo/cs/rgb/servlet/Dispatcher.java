/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import it.unibo.cs.rgb.servlet.util.MultiPartFormData;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author fedo
 */
public class Dispatcher extends HttpServlet {

    ArrayList<String> errors = new ArrayList<String>();

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DispatcherException, ParserConfigurationException, SAXException {







        //FrequenzeDiOccorrenze POST input: 1 tei, 1 parametro (da una lista ristretta) output: html
        //IndiciDiTerminiMarcati POST input: 1 tei, output: html
        //StemmaCodicum POST input: 1 file tei, output: svg o html+svg
        //Differenziazione input: > 2 files tei, output: RDF


        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("sono il dispatcher");
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            out.println(params.nextElement());
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String retVal = "";
        String line;
        while ((line = rd.readLine()) != null) {
            retVal = retVal + line;
        }
        out.println(retVal);


        try {
            out.println("ci provo servlet file upload");
            boolean isMultipart = true; //ServletFileUpload.isMultipartContent(request); //BOH
            if (!isMultipart) {
            } else {
                InputStreamReader input = new InputStreamReader(request.getInputStream());
                BufferedReader buffer = new BufferedReader(input);

                String sline = "";
                do {
                    sline = buffer.readLine();
                    out.println("Multipart data " + sline);
                } while (sline != null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }





        try {

            Document testDocument;
            DocumentBuilder testBuilder;
            DocumentBuilderFactory testFactory;

            testFactory = DocumentBuilderFactory.newInstance();
            testBuilder = testFactory.newDocumentBuilder();
            testDocument = testBuilder.parse(request.getInputStream());

            Element tempUser = testDocument.getDocumentElement();
            out.println(tempUser.getTextContent());


            if (request.getParameter("service") == null) {
                errors.add("Nessun servizio richiesto");
                throw new DispatcherException(response);
            }

            String service = request.getParameter("service");

            if (service.equalsIgnoreCase("FrequenzeDiOccorrenza")) {
                if (request.getParameter("keyword") == null) {
                    errors.add("FrequenzeDiOccorrenza: parametro mancante");
                    throw new DispatcherException(response);
                } else {
                    //frequenzeDiOccorrenze(String tei, keyword);
                }

            } else if (service.equalsIgnoreCase("IndiciDiTerminiMarcati")) {
            } else if (service.equalsIgnoreCase("StemmaCodicum")) {
            } else if (service.equalsIgnoreCase("Differenziazione")) {
            } else {
                errors.add("Servizio " + service + " richiesto inesistente");
                throw new DispatcherException(response);
            }

            /*
            TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Dispatcher</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Dispatcher at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
             */
        } finally {
            out.close();
        }
    }

    class DispatcherException extends Exception {

        DispatcherException(HttpServletResponse response) {

            try {
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet Dispatcher</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Dispatcher</h1>");









                out.println("</body>");
                out.println("</html>");
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            try {
                processRequest(request, response);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
            }



        } catch (DispatcherException ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            try {
                processRequest(request, response);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
            }



        } catch (DispatcherException ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";

    }// </editor-fold>
}
