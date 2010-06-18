/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.servlet;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import it.unibo.cs.rgb.tei.TeiDocument;
import it.unibo.cs.rgb.tei.TeiSvg;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    ArrayList<String> errors = new ArrayList<String>();

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, StemmaCodicumException {

        String xml = null;

        // lettura degli stylesheets (xsl)
        String stylesheetsFolder = "/stylesheets";
        HashMap xsl = new HashMap();
        Set stylesheetsSet = getServletContext().getResourcePaths(stylesheetsFolder);
        Iterator stylesheetsIter = stylesheetsSet.iterator();
        while (stylesheetsIter.hasNext()) {
            String current = (String) stylesheetsIter.next();
            if (current.endsWith(".xsl")) { //TODO controlla meglio con il content type
                //out.println("Xsl: " + current + "<br/>");
                xsl.put(current, convertStreamToString(getServletContext().getResourceAsStream(current)));
            }
        }

        // imposto l'outputstream
        PrintWriter out = response.getWriter();
        //response.setContentType("teimage/svg+xml;charset=UTF-8");
        response.setContentType("text/xml;charset=UTF-8"); //DEBUG

        errors.clear();

        // la request dev'essere "multipart/form-data"
        if (!request.getContentType().startsWith("multipart/form-data")) {
            errors.add("content type sbagliatissimo");
            throw new StemmaCodicumException(response);
        }

        // parso la request
        MultipartParser mparser = new MultipartParser(request, 1000999);

        // esamino ogni Part della request
        Part tmpPart = mparser.readNextPart();
        while (tmpPart != null) {
            if (tmpPart.isFile()) {
                String filename = "" + ((FilePart) tmpPart).getFileName();
                if (filename.endsWith(".xml")) {
                    xml = "" + convertStreamToString(((FilePart) tmpPart).getInputStream());
                }
            }
            tmpPart = mparser.readNextPart();

        }

        // verifica
        if (xml == null) {
            errors.add("manca file xml");
            throw new StemmaCodicumException(response);
        }

        // elaborazione dati del documento TEI
        ArrayList<HashMap> data = new ArrayList<HashMap>();
        TeiDocument tei = new TeiDocument("", xml, xsl);
        String svgDataString = tei.getSvgDataString();

        // verifica
        if ( svgDataString.length() <= 0) {
            errors.add("svg non genrabile su questo documento, non contiene witlist");
            throw new StemmaCodicumException(response);
        }

        
        try{// parsing dei dati ricevuti dall'elaborazione
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

            //out.println(" "+der+" "+missing+" "+sigil+" "+id);

            data.add(witnessMap);
        }}catch(Exception e){
            errors.add("svg non genrabile su questo documento, dati mancanti dalla witlist");
            throw new StemmaCodicumException(response);

        }

        for (int i = 0; i < data.size(); i++) {
            HashMap witnessMap = data.get(i);
            //out.println("- " + (String) witnessMap.get("der") + "- " + (Boolean) witnessMap.get("missing") + "- " + (String) witnessMap.get("sigil") + "- " + (String) witnessMap.get("id"));
        }

        // inizializzo il TeiSvg
        TeiSvg svg = new TeiSvg(data);

        // verifica
        if (svg.hasDtd() && !svg.isValid()) {
            errors.add("tei non valido");
            throw new StemmaCodicumException(response);
        }

        // verifica
        if (!svg.canGetSvg()) {
        errors.add("svg non generabile per questo documento tei: i dati non generano un albero");
        throw new StemmaCodicumException(response);
        }

        out.print(svg.getSvg());
        //out.print(tei.getSvgDataString());

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

        errors.add("accetto solo richieste post");
        try {
            throw new StemmaCodicumException(response);
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

        StemmaCodicumException(HttpServletResponse response) {

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
                errors.clear();
            } catch (IOException ex) {
                Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
    }

    public String convertStreamToString(InputStream is) throws IOException {
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


