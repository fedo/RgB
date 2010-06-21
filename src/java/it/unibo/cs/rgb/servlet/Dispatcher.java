/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import com.oreilly.servlet.MultipartRequest;
import it.unibo.cs.rgb.gwt.RgB;
import it.unibo.cs.rgb.tei.TeiDocument;
import it.unibo.cs.rgb.util.ClientHttpRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
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

        // lettura degli stylesheets (xsl)
        String stylesheetsFolder = "/stylesheets";
        HashMap xsl = new HashMap();
        Set stylesheetsSet = getServletContext().getResourcePaths(stylesheetsFolder);
        Iterator stylesheetsIter = stylesheetsSet.iterator();
        while (stylesheetsIter.hasNext()) {
            String current = (String) stylesheetsIter.next();
            if (current.endsWith(".xsl")) {
                xsl.put(current, RgB.convertStreamToString(getServletContext().getResourceAsStream(current), "UTF-8"));
            }
        }

        String catalogLTW1003 = "http://vitali.web.cs.unibo.it/twiki/pub/TechWeb10/GruppoLTW03/catalogo_ltw03.xml";
        String catalogLTW1001 = "http://ltw1001.web.cs.unibo.it/catalog.xml";

        URLConnection connectionLTW1003 = new URL(catalogLTW1003).openConnection();
        URLConnection connectionLTW1001 = new URL(catalogLTW1001).openConnection();

        TeiDocument dataCatalogLTW1003 = new TeiDocument("", RgB.convertStreamToString(connectionLTW1003.getInputStream(), "UTF-8"), xsl);
        TeiDocument dataCatalogLTW1001 = new TeiDocument("", RgB.convertStreamToString(connectionLTW1001.getInputStream(), "UTF-8"), xsl);

        String service = request.getParameter("service");

        //dispatching dei servizi
        if (service == null) {
            errors.add("servizio inesistente");
            throw new DispatcherException(response);

        } else if (service.equalsIgnoreCase("StemmaCodicum")) {
            HashMap map = dataCatalogLTW1001.getCatalogData(service);
            String serviceuri = (String) map.get("uri");
            String servicemethod = (String) map.get("method");
            String serviceinput = (String) map.get("input");
            String serviceoutput = (String) map.get("output");

            String filePath = request.getParameter("path");

            ClientHttpRequest nreq = new ClientHttpRequest(serviceuri);
            nreq.setParameter("tei", filePath, getServletContext().getResourceAsStream(filePath), serviceinput);

            PrintWriter out = response.getWriter();
            response.setContentType(serviceoutput);
            //out.println(serviceuri+" "+servicemethod+" "+serviceinput+" "+serviceoutput);
            out.println(RgB.convertXmlStreamToString(nreq.post()));

        } else if (service.equalsIgnoreCase("EstrazioneDiConcordanze")) {

            HashMap map = dataCatalogLTW1001.getCatalogData("EstrazioneDiConcordanze");
            String serviceuri = (String) map.get("uri");
            String servicemethod = (String) map.get("method");
            String serviceinput = (String) map.get("input");
            String serviceoutput = (String) map.get("output");

            String filePath = request.getParameter("path");
            String word = request.getParameter("word");
            String number = request.getParameter("number");

            ClientHttpRequest nreq = new ClientHttpRequest(serviceuri + "/" + word + "/" + number);
            nreq.setParameter("tei", filePath, getServletContext().getResourceAsStream(filePath), serviceinput);

            PrintWriter out = response.getWriter();
            response.setContentType(serviceoutput);
            //out.println(serviceuri+" "+servicemethod+" "+serviceinput+" "+serviceoutput);
            out.println(RgB.convertXmlStreamToString(nreq.post()));


        } else if (service.equalsIgnoreCase("Colocazioni")) {

            HashMap map = dataCatalogLTW1003.getCatalogData("Colocazioni");
            String serviceuri = (String) map.get("uri");
            String servicemethod = (String) map.get("method");
            String serviceinput = (String) map.get("input");
            String serviceoutput = (String) map.get("output");

            String filePath = request.getParameter("path");
            String word = request.getParameter("word");

            ClientHttpRequest nreq = new ClientHttpRequest(serviceuri + "/" + word);
            nreq.setParameter("tei", filePath, getServletContext().getResourceAsStream(filePath), serviceinput);

            PrintWriter out = response.getWriter();
            response.setContentType(serviceoutput);
            //out.println(serviceuri+" "+servicemethod+" "+serviceinput+" "+serviceoutput);
            out.println(RgB.convertXmlStreamToString(nreq.post()));

        } else if (service.equalsIgnoreCase("FrequenzeDiOccorrenza")) {

            HashMap map = dataCatalogLTW1003.getCatalogData("FrequenzeOccorrenza/Servizio_Occorrenze");
            String serviceuri = (String) map.get("uri");
            String servicemethod = (String) map.get("method");
            String serviceinput = (String) map.get("input");
            String serviceoutput = (String) map.get("output");

            String filePath = request.getParameter("path");

            ClientHttpRequest nreq = new ClientHttpRequest(serviceuri);
            nreq.setParameter("tei", filePath, getServletContext().getResourceAsStream(filePath), serviceinput);

            PrintWriter out = response.getWriter();
            response.setContentType(serviceoutput);
            //out.println(serviceuri+" "+servicemethod+" "+serviceinput+" "+serviceoutput);
            out.println(RgB.convertXmlStreamToString(nreq.post()));

        } else if (service.equalsIgnoreCase("Differenziazione")) {

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
