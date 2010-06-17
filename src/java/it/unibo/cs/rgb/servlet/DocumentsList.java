/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import it.unibo.cs.rgb.tei.TeiDocument;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fedo
 */
public class DocumentsList extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String teiFolder = "/collection5";
        
        ArrayList<String> xmlFilesList = new ArrayList<String>();

        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(200);

        PrintWriter out = response.getWriter();

        // lettura degli stylesheets (xsl)
        String stylesheetsFolder = "/stylesheets";
        HashMap xsl = new HashMap();
        Set stylesheetsSet = getServletContext().getResourcePaths(stylesheetsFolder);
        Iterator stylesheetsIter = stylesheetsSet.iterator();
        while (stylesheetsIter.hasNext()) {
            String current = (String) stylesheetsIter.next();
            if (current.endsWith(".xsl")) {
                //out.println("Xsl: " + current + "<br/>");
                xsl.put(current, convertStreamToString(getServletContext().getResourceAsStream(current)));
            }
        }

        // lettura dei documenti TEI (xml) e aggiunta dei nomi in una lista
        Set set = getServletContext().getResourcePaths(teiFolder);
        Object[] files = set.toArray();
        for (int i = 0; i < files.length; i++) {
            if (((String) files[i]).endsWith(".xml")) {
                xmlFilesList.add("" + (String) files[i]);
            }
        }

        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" version=\"1.0\">");
        out.println("<h1>DocumentsList: lista e descrizione dei documenti Tei</h1>");
        out.println("<p>");

        out.println("Cartella files TEI (xml) selezionato: " + teiFolder + "<br/>");
        out.println("Numero documenti Tei presenti: <span id=\"numberOfDocuments\">" + xmlFilesList.size() + "</span><br/>");

        out.println("</p>");
        out.println("<ul>");

        // scrittura informazioni sui documenti TEI
        for (int i = 0; i < xmlFilesList.size(); i++) {
            TeiDocument tei = new TeiDocument(xmlFilesList.get(i), convertStreamToString(getServletContext().getResourceAsStream(xmlFilesList.get(i))), xsl);

            out.println("<li id=\"document\">");
            out.println("<span id=\"id\">" + tei.getTeiName() + "</span>");
            out.println("<br /><span id=\"path\">" + tei.getAbsolutePath() + "</span>");
            out.println("<br /><span id=\"longName\">" + tei.getHover() + "</span>");
            //out.println("<br /><span id=\"debug\">" + tei.getTeiString() + "</span>"); //DEBUG
            out.println("</li>");
        }

        out.println("</ul>");
        out.println("</html>");

        out.close();
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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public static String convertStreamToString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
