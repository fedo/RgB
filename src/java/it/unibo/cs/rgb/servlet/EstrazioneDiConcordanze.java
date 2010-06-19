/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import it.unibo.cs.rgb.gwt.RgB;
import it.unibo.cs.rgb.tei.TeiConcordanze;
import it.unibo.cs.rgb.tei.TeiDocument;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fedo
 */
public class EstrazioneDiConcordanze extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws RgBServletException, ServletException, IOException, EstrazioneDiConcordanzeException {

        String xml = null;

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
            throw new EstrazioneDiConcordanzeException(response, "406", "Not acceptable", "Richiesto Content-Type 'multipart/form-data'.", "Imposta il Content-Type 'multipart/form-data'.");
        }

        String word = request.getParameter("word");
        String stringNumber = request.getParameter("number");
        
        //verifica
        if (word == null) {
            throw new EstrazioneDiConcordanzeException(response, "406", "Not acceptable", "Manca il parametro 'word'.", "Inserisci nella richiesta il parametro 'word'.");
        }
        
        // verifica
        if (stringNumber == null) {
            throw new EstrazioneDiConcordanzeException(response, "406", "Not acceptable", "Manca il parametro 'number'.", "Inserisci nella richiesta il parametro 'number'.");
        }
        int number = Integer.parseInt(stringNumber);

        // parsing request
        MultipartParser mparser = new MultipartParser(request, Integer.MAX_VALUE);

        // ricerca documento TEI (xml)
        Part tmpPart = mparser.readNextPart();
        while (tmpPart != null) {
            if (tmpPart.isFile()) {
                String contentType = "" + ((FilePart) tmpPart).getContentType();
                if (contentType.equals("text/xml") || contentType.equals("application/xml")) {
                    xml = "" + RgB.convertXmlStreamToString(((FilePart) tmpPart).getInputStream());
                }
            }
            tmpPart = mparser.readNextPart();
        }

        // verifica presenza documento TEI
        if (xml == null) {
            throw new EstrazioneDiConcordanzeException(response, "406", "Not acceptable", "File input mancante.", "Inserisci nella richiesta un documento TEI.");
        }

        TeiDocument tei = new TeiDocument("TeiEstrazioneDiConcordanze", xml, xsl);

        String[] witnessesList = new String[999999]; //tei.getNumberOfWitnesses();
        String[] transcriptionTypes = new String[1000]; //tei.getTranscriptionTypes();
        String retval = "";

        for (int n = 0; n < witnessesList.length; n++) {
            for (int m = 0; m < transcriptionTypes.length; m++) {
                String data = "fajklfajdlk"; //tei.getEstrazioneDiConcordanzeData(witnessList[n], transcriptionTypes[m]);
                TeiConcordanze con = new TeiConcordanze(word, number, data, witnessesList[n], transcriptionTypes[m]);
                retval += con.getConcordanze();
            }
        }

        // output
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        out.print(retval);
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
        try {
            processRequest(request, response);
        } catch (EstrazioneDiConcordanzeException ex) {
            Logger.getLogger(EstrazioneDiConcordanze.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (EstrazioneDiConcordanzeException ex) {
            Logger.getLogger(EstrazioneDiConcordanze.class.getName()).log(Level.SEVERE, null, ex);
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

    class EstrazioneDiConcordanzeException extends Exception {

        EstrazioneDiConcordanzeException(HttpServletResponse response, String code, String shortDescription, String description, String tip) {
            PrintWriter out = null;
            try {
                response.setContentType("application/json");
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
