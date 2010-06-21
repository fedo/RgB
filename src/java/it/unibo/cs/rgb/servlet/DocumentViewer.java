/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import it.unibo.cs.rgb.gwt.RgB;
import it.unibo.cs.rgb.tei.TeiDocument;
import java.io.IOException;
import java.io.PrintWriter;
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
public class DocumentViewer extends HttpServlet {

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

        // lettura degli stylesheets (xsl)
        String stylesheetsFolder = "/stylesheets";
        HashMap xsl = new HashMap();
        Set stylesheetsSet = getServletContext().getResourcePaths(stylesheetsFolder);
        Iterator stylesheetsIter = stylesheetsSet.iterator();
        while (stylesheetsIter.hasNext()) {
            String current = (String) stylesheetsIter.next();
            if (current.endsWith(".xsl")) {
                //out.println("Xsl: " + current + "<br/>");
                xsl.put(current, RgB.convertXmlStreamToString(getServletContext().getResourceAsStream(current)));
            }
        }

        PrintWriter out = response.getWriter();
        try {

            String path = request.getParameter("path");
            String witness = request.getParameter("witness");
            TeiDocument tei = new TeiDocument(path, RgB.convertXmlStreamToString(getServletContext().getResourceAsStream(path)), xsl);

            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" version=\"1.0\">");
            out.println("<div id=\"view\" style=\"width:400px;\">" + tei.getView(witness) + "</div>");
            out.println("</html>");

        } finally {
            out.close();
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
