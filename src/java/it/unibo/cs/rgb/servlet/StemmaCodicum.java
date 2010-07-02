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
import it.unibo.cs.rgb.tei.TeiSvg;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
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
public class StemmaCodicum extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, StemmaCodicumException {

        response.setCharacterEncoding("UTF-8");
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
            throw new StemmaCodicumException(response, "406", "Not acceptable", "Richiesto Content-Type 'multipart/form-data'.", "Imposta il Content-Type 'multipart/form-data'.");
        }

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
            throw new StemmaCodicumException(response, "406", "Not acceptable", "File input mancante.", "Inserisci nella richiesta un documento TEI.");
        }

        // elaborazione dati del documento TEI
        ArrayList<HashMap> data = new ArrayList<HashMap>();
        TeiDocument tei = new TeiDocument("", xml, xsl);
        String svgDataString = tei.getSvgDataString();

        // verifica
        if (svgDataString.length() <= 0) {
            throw new StemmaCodicumException(response, "406", "Not acceptable", "Il documento TEI dato in input non contiene witList.", "Inserisci nella richiesta un documento TEI contenente witList.");
        }

        // parsing dei dati ricevuti dall'elaborazione
        try {
            String[] lines = svgDataString.split("\\-");
            //out.println("_linesNumber_"+lines.length+"_");
            for (int i = 0; i < lines.length; i++) {

                HashMap witnessMap = new HashMap();
                StringTokenizer tokens = new StringTokenizer(lines[i]);

                String der = tokens.nextToken();
                boolean missing = Boolean.parseBoolean(tokens.nextToken());
                String sigil = tokens.nextToken();
                String id = tokens.nextToken();

                if (!der.equalsIgnoreCase("null")) {
                    witnessMap.put("der", der);
                }
                witnessMap.put("missing", missing);
                witnessMap.put("sigil", sigil);
                witnessMap.put("id", id);

                data.add(witnessMap);
            }
        } catch (Exception e) {
            throw new StemmaCodicumException(response, "406", "Not acceptable", "Il documento TEI dato in input non contiene witList corretta.", "Inserisci nella richiesta un documento TEI contenente witList corretta.");
        }

        // inizializzo il TeiSvg
        TeiSvg svg = new TeiSvg(data);

        // verifica
        if (svg.hasDtd() && !svg.isValid()) {
            throw new StemmaCodicumException(response, "406", "Not acceptable", "Il documento TEI dato in input non è valido.", "Inserisci nella richiesta un documento TEI valido.");
        }

        // verifica
        if (!svg.canGetSvg()) {
            throw new StemmaCodicumException(response, "406", "Not acceptable", "Il documento TEI dato in input non permette di generare un albero.", "Inserisci nella richiesta un documento TEI che permetta di generare un albero.");
        }

        // output
        PrintWriter out = response.getWriter();
        //response.setContentType("teimage/svg+xml;charset=UTF-8");
        response.setContentType("text/xhtml;charset=UTF-8"); //DEBUG
        out.print(svg.getSvg());
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
            throw new StemmaCodicumException(response, "405", "Method not allowed", "Accettate solo richieste POST.", "Imposta il metodo POST.");
        } catch (StemmaCodicumException ex) {
            Logger.getLogger(StemmaCodicum.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (StemmaCodicumException ex) {
            Logger.getLogger(StemmaCodicum.class.getName()).log(Level.SEVERE, null, ex);
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

    class StemmaCodicumException extends Exception {

        StemmaCodicumException(HttpServletResponse response, String code, String shortDescription, String description, String tip) {
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
