/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.servlet;

import it.unibo.cs.rgb.gwt.tei.TeiCollection;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fedo
 */
public class Tei extends HttpServlet {
   
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
        PrintWriter out = response.getWriter();
        try {
            //TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Tei</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Tei at " + request.getContextPath() + "</h1>");

            out.println("<p>Absolutepath"+request.getRealPath("")+"</p>");

            /*File folder = new File("/Users/fedo/data/programming/netbeans/RgB/src/java/resources/collection1");
            File[] list = folder.listFiles();
            for(int i = 0; i < list.length; i++){
                out.println("<p>"+list[i].getName()+"</p>");
            }*/




                    TeiCollection collection = new TeiCollection();
        collection.init("/Users/fedo/data/programming/netbeans/RgB/src/java/resources/collection1");
        ArrayList<String> alstr = new ArrayList<String>();

        for(int i = 0; i < collection.getNumberOfDocument(); i++){
            alstr.add(collection.getTeiDocument(i).getAbsolutePath());
            out.println("<p>"+alstr.get(i)+"</p>");
            out.println("<p>"+collection.getTeiDocument(i).getAuthor()+"</p>");
        }
            out.println("<h1>Servlet end</h1>");
            out.println("</body>");
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
