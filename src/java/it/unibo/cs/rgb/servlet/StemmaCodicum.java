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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
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

        // imposto l'outputstream
        PrintWriter out = response.getWriter();
        //response.setContentType("teimage/svg+xml;charset=UTF-8");
        response.setContentType("plain/text;charset=UTF-8"); //DEBUG

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

        // parsing dei dati ricevuti
        ArrayList<HashMap> data = new ArrayList<HashMap>();
        TeiDocument tei = new TeiDocument(xml);
        //String svgDataString = tei.getSvgDataString();
        String svgDataString = "der1 1 sigil1 id1" + "----" + "der2 0 sigil2 id2"; //tring "der", boolean "missing", String "sigil", String "id"

        String[] lines = svgDataString.split("----");
        for (int i = 0; i < lines.length; i++) {
            HashMap witnessMap = new HashMap();
            StringTokenizer tokens = new StringTokenizer(lines[i]);
            witnessMap.put("der", tokens.nextToken());
            witnessMap.put("missing", Boolean.parseBoolean(tokens.nextToken()));
            witnessMap.put("sigil", tokens.nextToken());
            witnessMap.put("id", tokens.nextToken());
            data.add(witnessMap);
        }

        for (int i = 0; i < data.size(); i++) {
            HashMap witnessMap = data.get(i);
            out.println("- " + (String) witnessMap.get("der") + "- " + (Boolean) witnessMap.get("missing") + "- " + (String) witnessMap.get("sigil") + "- " + (String) witnessMap.get("id"));
        }

        // inizializzo il TeiSvg
        TeiSvg svg = new TeiSvg(data);

        // verifica
        /*if (tei.hasDtd() && !tei.isValid()) {
        errors.add("tei non valido");
        throw new StemmaCodicumException(response);
        }*/

        // verifica
        /*if (!tei.canGetSvg()) {
        errors.add("svg non generabile per questo documento tei");
        throw new StemmaCodicumException(response);
        }*/

        //out.print(docu.getTeiString());
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


