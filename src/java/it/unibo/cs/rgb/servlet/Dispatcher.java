/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import com.oreilly.servlet.MultipartRequest;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import it.unibo.cs.rgb.gwt.RgBConfiguration;

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

        //StemmaCodicum POST input: 1 file tei, output: svg o html+svg
        //EstrazioneDiConcordanze: un documento TEI + una parola P + un numero N
        //Differenziazione input: > 2 files tei, output: RDF

        if (!request.getContentType().startsWith("multipart/form-data")) {
            errors.add("content type sbagliatissimo");
            throw new DispatcherException(response);
        }

        MultipartRequest mrequest = new MultipartRequest(request, RgBConfiguration.tempRequestsDirectory);
        Enumeration filesEnumeration = mrequest.getFileNames();
        ArrayList<String> files = new ArrayList<String>();
        Enumeration parametersEnumeration = mrequest.getParameterNames();
        ArrayList<String> parameters = new ArrayList<String>();

        while (parametersEnumeration.hasMoreElements()) {
            String tmp = (String) parametersEnumeration.nextElement();
            parameters.add(tmp);
        }

        while (filesEnumeration.hasMoreElements()) {
            String tmp = (String) filesEnumeration.nextElement();
            files.add(tmp);
        }

        String service = mrequest.getParameter("service");
        if (service == null) {
            errors.add("non definito parametro service");
            throw new DispatcherException(response);
        }

        //dispatching dei servizi
        if (service.equalsIgnoreCase("StemmaCodicum")) {
            response.setContentType("teimage/svg+xml");
            PrintWriter out = response.getWriter();

            String xml = getFileNameByContentType(mrequest, "text/xml");
            String dtd = getFileNameByContentType(mrequest, "application/octet-stream");

            if (xml == null) {
                errors.add("stemma: xml mancante");
                throw new DispatcherException(response);
            }

            if (dtd == null) {
                //stemma(xml)
            } else {
                //stemma(xml,dtd)
            }


        } else if (service.equalsIgnoreCase("EstrazioneDiConcordanze")) {
            //EstrazioneDiConcordanze edc;

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            String xml = getFileNameByContentType(mrequest, "text/xml");
            String dtd = getFileNameByContentType(mrequest, "application/octet-stream");
            String word = mrequest.getParameter("word");
            String number = mrequest.getParameter("number");

            if (xml == null) {
                errors.add("estrazione: xml mancante");
                if (word == null) {
                    errors.add("estrazione: word mancante");
                }
                if (number == null) {
                    errors.add("estrazione: number mancante");
                }
                throw new DispatcherException(response);
            }

            if (dtd == null) {
                //edc = EstrazioneDiConcordanze(xml,word,number)
            } else {
                //edc = EstrazioneDiConcordanze(xml,dtd,word,number)
            }
            out.println("<p>xmlfile: "+xml+" dtdfile: "+dtd+" word: "+word+" number: "+number+"</p>");


        } else if (service.equalsIgnoreCase("Differenziazione")) {
            response.setContentType("application/rdf+xml");
            PrintWriter out = response.getWriter();
            out.println("differenziazione");

        } else {
            errors.add("servizio inesistente");
            throw new DispatcherException(response);
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
                out.println("<h1>Dispatcher errors:</h1>");
                for (int i = 0; i < errors.size(); i++) {
                    out.println(errors.get(i));
                }
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

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<p>accetto solo richieste post, tiè O,/</p> ");

        /*

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
        }*/


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

    private int getIndex(ArrayList<String> arrayList, String keyword) {
        int retval = -1;

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equalsIgnoreCase(keyword)) {
                retval = i;
            }
        }

        return retval;
    }

    private String getFileNameByContentType(MultipartRequest mrequest, String contentType) {
        String retval = null;

        Enumeration files = mrequest.getFileNames();

        while (files.hasMoreElements()) {
            String currentFileName = (String) files.nextElement();
            String currentContentType = (String) mrequest.getContentType(currentFileName);
            if (currentContentType != null && currentContentType.equalsIgnoreCase(contentType)) {
                retval = currentFileName;
            }
        }

        return retval;
    }
}
