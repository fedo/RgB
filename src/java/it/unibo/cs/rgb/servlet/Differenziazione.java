/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        if (!request.getContentType().startsWith("multipart/form-data")) {
            throw new DifferenziazioneException(response, "406", "Not acceptable", "Richiesto Content-Type 'multipart/form-data'.", "Imposta il Content-Type 'multipart/form-data'.");
        }

        // parsing request
        MultipartParser mparser = new MultipartParser(request, Integer.MAX_VALUE);

        // ricerca documento TEI (xml)
        Part tmpPart = mparser.readNextPart();
        while (tmpPart != null) {
            if (tmpPart.isFile()) {
                String contentType = "" + ((FilePart) tmpPart).getContentType();
                if ((contentType.equals("text/xml") || contentType.equals("application/xml"))) {
                    if (xml1 == null) {
                        xml1 = "" + RgB.convertXmlStreamToString(((FilePart) tmpPart).getInputStream());
                    } else if (xml1 != null && xml2 == null) {
                        xml2 = "" + RgB.convertXmlStreamToString(((FilePart) tmpPart).getInputStream());
                    }
                }
            }
            tmpPart = mparser.readNextPart();

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
