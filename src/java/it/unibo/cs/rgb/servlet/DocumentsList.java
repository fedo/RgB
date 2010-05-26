/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import it.unibo.cs.rgb.gwt.RgBConfiguration;
import it.unibo.cs.rgb.gwt.tei.TeiCollection;
import java.io.IOException;
import java.io.PrintWriter;
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

        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(200);

        PrintWriter out = response.getWriter();

        // creazione json dai files tei
        TeiCollection collection = new TeiCollection();
        String path = RgBConfiguration.collectionDirectoryPath;
        collection.init(path);

        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" version=\"1.0\">");
        out.println("<h1>DocumentsList: lista e descrizione dei documenti Tei</h1>");

        out.println("<p>Path selezionato: "+path+"<br />"
                + "Numero documenti Tei presenti: <span id=\"numberOfDocuments\">" + collection.getNumberOfDocument() + "</span></p>");

        out.println("\t<ul>");
        for (int i = 0; i < collection.getNumberOfDocument(); i++) {
            out.println("<li id=\"document\">");
            out.println("<span id=\"id\">" + collection.getTeiDocument(i).getTeiName() + "</span>");
            out.println("<br /><span id=\"path\">" + collection.getTeiDocument(i).getAbsolutePath() + "</span>");
            out.println("<br /><span id=\"shortName\">C " + collection.getTeiDocument(i).getTeiName() + "</span>"); //TODO
            out.println("<br /><span id=\"longName\">Lungo " + collection.getTeiDocument(i).getTeiName() + "</span>"); //TODO
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
}
