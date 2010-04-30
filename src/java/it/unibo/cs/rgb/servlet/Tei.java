/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.servlet;

import it.unibo.cs.rgb.gwt.tei.TeiCollection;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

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

        response.setContentType("application/json;charset=UTF-8"); //corretto
        response.setHeader("Cache-Control", "no-cache");

        JSONObject json = new JSONObject();
        try {
            JSONObject file = new JSONObject();
            
            /*// esempio
            file.accumulate("aboslutePath", "/uon/due/tre");
            file.accumulate("filename", "testo.xml");
            json.put("primo",file);*/

            // creazione json dai files tei
            TeiCollection collection = new TeiCollection();
            collection.init("/Users/fedo/data/programming/netbeans/RgB/src/java/resources/collection1");

            for (int i = 0; i < collection.getNumberOfDocument(); i++) {
                file = new JSONObject();
                file.accumulate("aboslutePath", collection.getTeiDocument(i).getAbsolutePath());
                file.accumulate("author", collection.getTeiDocument(i).getAuthor());
                json.put(""+i, file);
            }

        } catch (JSONException ex) {
            Logger.getLogger(Tei.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PrintWriter out = response.getWriter();
        out.print(json.toString());

        /*response.setContentType("text/html;charset=UTF-8");
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

            TeiCollection collection = new TeiCollection();
            collection.init("/Users/fedo/data/programming/netbeans/RgB/src/java/resources/collection1");
            ArrayList<String> alstr = new ArrayList<String>();

            for (int i = 0; i < collection.getNumberOfDocument(); i++) {
                alstr.add(collection.getTeiDocument(i).getAbsolutePath());
                out.println("<p>" + alstr.get(i) + "</p>");
                out.println("<p>" + collection.getTeiDocument(i).getAuthor() + "</p>");
            }
            out.println("<h1>Servlet end</h1>");
            out.println("</body>");
            out.println("</html>");

        } finally {
            out.close();
        }*/
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
