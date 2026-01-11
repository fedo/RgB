/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import it.unibo.cs.rgb.gwt.RgB;
import it.unibo.cs.rgb.tei.TeiDocument;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;

/**
 *
 * @author fedo
 */
public class Differenziazione extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DifferenziazioneException {

        response.setCharacterEncoding("UTF-8");
        String xml1 = null;
        String xml2 = null;

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

        // la request dev'essere "multipart/form-data"
        if (!JakartaServletFileUpload.isMultipartContent(request)) {
            throw new DifferenziazioneException(response, "406", "Not acceptable", "Richiesto Content-Type 'multipart/form-data'.", "Imposta il Content-Type 'multipart/form-data'.");
        }

        // parsing request using Apache Commons FileUpload 2.x
        DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
        JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);
        
        try {
            java.util.List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String contentType = item.getContentType();
                    if (contentType != null && (contentType.equals("text/xml") || contentType.equals("application/xml"))) {
                        if (xml1 == null) {
                            xml1 = RgB.convertXmlStreamToString(item.getInputStream());
                        } else if (xml2 == null) {
                            xml2 = RgB.convertXmlStreamToString(item.getInputStream());
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new DifferenziazioneException(response, "500", "Internal Server Error", "Errore nel parsing della richiesta.", e.getMessage());
        }

        // verifica presenza documento TEI
        if (xml1 == null || xml2 == null) {
            throw new DifferenziazioneException(response, "406", "Not acceptable", "File input mancante.", "Inserisci nella richiesta due documenti TEI.");
        }

        TeiDocument tei1 = new TeiDocument("tei1", xml1, xsl);
        TeiDocument tei2 = new TeiDocument("tei2", xml2, xsl);

        String text1 = tei1.getTeiString();
        String text2 = tei2.getTeiString();

        // differenze
        diff_match_patch diff = new diff_match_patch();
        LinkedList<Diff> list = diff.diff_main(text1, text2);
        diff.diff_cleanupSemantic(list);
        String output = diff.diff_prettyHtml(list);

        // output
        PrintWriter out = response.getWriter();
        response.setContentType("text/xhtml;charset=UTF-8"); //DEBUG
        //out.print(output);
        out.print(output);

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
        } catch (DifferenziazioneException ex) {
            Logger.getLogger(Differenziazione.class.getName()).log(Level.SEVERE, null, ex);
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
            processRequest(request, response);
        } catch (DifferenziazioneException ex) {
            Logger.getLogger(Differenziazione.class.getName()).log(Level.SEVERE, null, ex);
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

    class DifferenziazioneException extends Exception {

        DifferenziazioneException(HttpServletResponse response, String code, String shortDescription, String description, String tip) {
            PrintWriter out = null;
            try {
                response.setContentType("application/json");
                response.setStatus(Integer.parseInt(code));
                out = response.getWriter();
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
                message += "\n\tcode: '" + code + "',";
                message += "\n\tshort: '" + shortDescription + "',";
                message += "\n\tdescription : '" + description + "',";
                message += "\n\ttip: '" + tip + "'";
                message += "\n}";
                out.print(message);
            } catch (IOException ex) {
                Logger.getLogger(EstrazioneDiConcordanze.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                out.close();
            }
        }
    }
}
