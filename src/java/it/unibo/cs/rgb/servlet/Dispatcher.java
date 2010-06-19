/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import com.oreilly.servlet.MultipartRequest;
import it.unibo.cs.rgb.gwt.RgB;
import it.unibo.cs.rgb.util.ClientHttpRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
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

        String host = "localhost:8080";

        String service = request.getParameter("service");

        //dispatching dei servizi
        if (service == null) {
            errors.add("servizio inesistente");
            throw new DispatcherException(response);

        } else if (service.equalsIgnoreCase("StemmaCodicum")) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String filePath = request.getParameter("path");

            ClientHttpRequest nreq = new ClientHttpRequest("http://"+host+"/RgB/StemmaCodicum");
            nreq.setParameter("tei", filePath, getServletContext().getResourceAsStream(filePath), "application/xml");
            out.println(RgB.convertXmlStreamToString(nreq.post()));

        } else if (service.equalsIgnoreCase("EstrazioneDiConcordanze")) {

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String filePath = request.getParameter("path");
            String word = request.getParameter("word");
            String number = request.getParameter("number");


        } else if (service.equalsIgnoreCase("Colocazioni")) {

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String filePath = request.getParameter("path");
            String word = request.getParameter("word");

            ClientHttpRequest nreq = new ClientHttpRequest("http://ltw1002.web.cs.unibo.it:8080/Colocazioni/Servizio_Col"+"/"+word);
            nreq.setParameter("files", filePath, getServletContext().getResourceAsStream(filePath), "application/xml");
            out.println(RgB.convertXmlStreamToString(nreq.post()));

        }else if (service.equalsIgnoreCase("FrequenzeDiOccorrenza")) {

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String filePath = request.getParameter("path");

            ClientHttpRequest nreq = new ClientHttpRequest("http://ltw1002.web.cs.unibo.it:8080/FrequenzeOccorrenza/Servizio_Occorrenze");
            nreq.setParameter("files", filePath, getServletContext().getResourceAsStream(filePath), "application/xml");
            out.println(RgB.convertXmlStreamToString(nreq.post()));
            
        }else if (service.equalsIgnoreCase("Differenziazione")) {

            response.setContentType("application/rdf+xml");
            PrintWriter out = response.getWriter();
            out.println("differenziazione");
            
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
        try {
            processRequest(request, response);
        } catch (DispatcherException ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
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
