/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.servlet;

import it.unibo.cs.rgb.gwt.RgBConfiguration;
import it.unibo.cs.rgb.gwt.tei.*;
import it.unibo.cs.rgb.util.TreeNode;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gine
 */
public class StemmaCodicum extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TeiCollection collection = new TeiCollection();
        TeiSvg stemma = new TeiSvg();
        String path = RgBConfiguration.collection1DirectoryPath;
        int nLeaf;

        collection.init(path);
        NodeList witness = (NodeList) collection.getTeiDocument(0).xpathQueryNL("//witList/witness");
        ArrayList<TreeNode> nodes = stemma.getTree(witness);
        nLeaf = stemma.getNumberOfLeaf(nodes.get(0));

    }
}
