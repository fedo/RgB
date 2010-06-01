/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.test;

import it.unibo.cs.rgb.gwt.tei.TeiCollection;
import it.unibo.cs.rgb.util.TreeNode;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.w3c.dom.NodeList;

/**
 *
 * @author gine
 */
public class TreeTest {
    public static void main(String[] args) throws IOException {
        System.out.println("Test tree");

        TeiCollection collection = new TeiCollection();
        collection.init("src/java/resources/collection1/parallel segmentation/");
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        NodeList witness = (NodeList) collection.getTeiDocument(0).xpathQueryNL("//witList/witness");
        
        int rCircle = 40, fontSize = 0, nFontSize = 20, gFontSize =26;
        String colourStroke = "", nColourStroke = "#83adf7", gColourStroke = "red";
        String littleLetters="filtjr", mediumLetters="abcdeghnopqzsuvkzy0123456789", bigLetters="mw";
        int xLitWordLoc =-1, xMedWordLoc =-5, xBigWordLoc =-7, xGWordLoc = -7, xWordLoc = 0,nGreekNodes = 0;
        int yWordLoc = 5, yGWordLoc = 7;
        //DEBUG disegno del nodo
        //int xCirclePos=0, yCirclePos=0,xTextPos=0,yTextPos=0,xFirstPLinePos=0,yFirstPLinePos=0,xSecondPLinePos=0,ySecondPLinePos=0;
        int xCirclePos=100, yCirclePos=50;//,xFirstPLinePos=0,yFirstPLinePos=0,xSecondPLinePos=0,ySecondPLinePos=0;
        String labelId ="init";
        //end debug

        //riempo l'albero della astrutta dati
        for (int i=0; i<witness.getLength(); i++){
            String id= witness.item(i).getAttributes().getNamedItem("sigil").getTextContent().toLowerCase();
            if (witness.item(i).getAttributes().getNamedItem("missing").getTextContent().equalsIgnoreCase("true"))
                nodes.add(new TreeNode(null, id, true));
            else
                nodes.add(new TreeNode(null, id, false));
        }

        for (int i=0; i<nodes.size(); i++){
            if (witness.item(i).getAttributes().getNamedItem("der") != null){
                String parent=witness.item(i).getAttributes().getNamedItem("der").getTextContent();
            
                for (int n=0; n<nodes.size();n++){
                    if (nodes.get(n).getId().equalsIgnoreCase(parent)){
                        nodes.get(n).addChild(nodes.get(i));
                        break;
                    }
                 }
            }
        }

        for (int i=0;i<nodes.size();i++){
             nodes.get(i).setDepth(countDepth(nodes.get(i)));
        }
        
        int nLeaf=countLeaf(nodes.get(0));
        int depth=maxDepth(nodes);
        // end
        /*System.out.println("depth:"+depth);
        int circleSpace = 80, spaceBetweenCicle = 40;

        //per tutti i livelli
        for (int i=depth;i>0;i--) {
            
            int x=0, y=i*((rCircle*2)+spaceBetweenCicle)-(rCircle*2), nNodeXLvl=1;
            //per tutti i nodi
            for (int n=0;n<nodes.size();n++){
                //se sono nodi di quel livello
                if (nodes.get(n).getDepth()==i){
                        System.out.println("node:"+nodes.get(n).getId()+" depth:"+nodes.get(n).getDepth());
                        // se non sono stati disegnati
                        if (! nodes.get(n).getDrawned()) {
                            if (nodes.get(n).getParent() != null) {
                                int nSibling = nodes.get(n).getParent().getChildren().size();
                                if (n != 0) {
                                    int swapX=0;
                                    for (int c=0; c<nSibling;c++) {
                                        swapX=nodes.get(n).getParent().getChildren().get(c).getX();
                                        if (x<swapX)
                                            x = swapX;
                                    }
                                }

                                //per tutti i fratelli
                                for (int c=0; c<nSibling;c++) {
                                    if (c == 0 && n != 0)
                                        x += (rCircle+spaceBetweenCicle);
                                    else
                                        x += (rCircle*2)+spaceBetweenCicle;

                                     nodes.get(n).getParent().getChildren().get(c).setPosition(x, y);
                                     nodes.get(n).getParent().getChildren().get(c).setDrawned(true);
                                }

                                int xParent = 0;
                                if (isOdd(nSibling)) {
                                    if (nSibling == 1)
                                        xParent = nodes.get(n).getParent().getChildren().get(0).getX();
                                    else
                                        xParent = nodes.get(n).getParent().getChildren().get(((nSibling-1)/2)).getX();
                                } else {
                                    int x1=nodes.get(n).getParent().getChildren().get(0).getX();
                                    int x2=nodes.get(n).getParent().getChildren().get(nSibling).getX();
                                    xParent =(x1+x2)/2;
                                }

                                y = y=(i-1)*((rCircle*2)+spaceBetweenCicle);

                                nodes.get(n).getParent().setPosition(xParent, y);
                                nodes.get(n).getParent().setDrawned(true);
                                }
                            }
                    }
            }

        }*/
 
        try {
            FileWriter outFile = new FileWriter("/Users/fedo/Desktop/svg1.xml");
            PrintWriter out = new PrintWriter(outFile);

            out.println("<html xmlns:svg=\"http://www.w3.org/2000/svg\">");
            out.println("<body>");
            out.println("<svg:svg width=\"2000\" height=\"1000\" version=\"1.1\" >");

            for (int i = 0; i < nodes.size(); i++){
                if (nodes.get(i).getMissing()) {
                    labelId = getGreek(nGreekNodes);
                    xWordLoc=xGWordLoc;
                    yWordLoc=yGWordLoc;
                    fontSize=gFontSize;
                    colourStroke=gColourStroke;
                    nGreekNodes++;
                } else {
                    xWordLoc = 0;
                    labelId=nodes.get(i).getId();
                    xWordLoc += howMuchMove(littleLetters,xLitWordLoc,labelId);
                    xWordLoc += howMuchMove(mediumLetters,xMedWordLoc,labelId);
                    xWordLoc += howMuchMove(bigLetters,xBigWordLoc,labelId);

                    fontSize=nFontSize;
                    colourStroke=nColourStroke;
                }

                String circleStyle="style=\"fill:none;fill-opacity:1;fill-rule:nonzero;stroke:"+colourStroke+";stroke-width:3;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-dashoffset:0;stroke-opacity:1",
                       textStyle="style=\"font-size:"+fontSize+"px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;writing-mode:lr-tb;text-anchor:start;fill:black;fill-opacity:1;stroke:none;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;font-family:Arial",
                       lineStyle="style=\"fill:none;fill-rule:evenodd;stroke:"+nColourStroke+";stroke-width:2.93415856;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1";
                //debug
                xCirclePos += 120;
                //xCirclePos = nodes.get(i).getX();
                //yCirclePos = nodes.get(i).getY();
                int xIdPos=xCirclePos+xWordLoc, yIdPos=yCirclePos+yWordLoc;
                //System.out.println("labelId:"+labelId+" xCirclePos:"+xCirclePos+ " xIdPos:"+xIdPos+" xWordLoc:"+xWordLoc);
                String text="<svg:text id=\""+labelId+"\" x=\""+xIdPos+"\" y=\""+yIdPos+"\"  " + textStyle + "\">"+ labelId +"</svg:text>";
                String circle="<svg:circle id=\""+labelId+"\" cx=\""+xCirclePos+"\" cy=\""+yCirclePos+"\" r=\""+rCircle+"\" "+circleStyle+"\" />";//,
                       //line="<svg:line id=\""+id+"\" x1=\""+xFirstPLinePos+"\" y1=\""+yFirstPLinePos+"\" x2=\""+xSecondPLinePos+"\" y2=\""+ySecondPLinePos+"\" " +lineStyle+"\"/>";

                out.println(circle);
                out.println(text);
                //out.println(line);
           }

           out.println("</svg:svg>");
           out.println("</body>");
           out.println("</html>");
           out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // DEBUG: tree */
        /*for (int i=0; i<nodes.size(); i++){
           System.out.println("node id:"+nodes.get(i).getId()+" node depth:"+nodes.get(i).getDepth());
        }
        
        System.out.println(""+nodes.get(0).getChildren().get(0).getId());
        System.out.println(""+nodes.get(1).getParent().getId());
        System.out.println(""+nLeaf);
        */
    }

    public static boolean isOdd(int n){
        boolean ret=false;

        if (n%2 > 0) {
            ret = true;
        }

        return ret;
    }

    public static String getGreek(int nGreekNodes){
        String gLetters="αβγδεζηθικλμνξοπρςτυφχψω";
        //char ret =
        return ""+gLetters.charAt(nGreekNodes);
    }

    public static int howMuchMove(String letters, int x, String id){
        int ret = 0;

        for (int i=0; i<id.length();i++){
            if(letters.indexOf(id.charAt(i)) > -1 ) {
                    ret += x;
            }
        }
        //System.out.println("type:" + x + "");
        return ret;
    }

    private static int countDepth(TreeNode node){
        int count=0;
        TreeNode swapNode = node.getParent();

        while (swapNode != null){
            count++;
            swapNode = swapNode.getParent();
        }

        System.out.println("count:"+count);

        return count;
    }
    
    private static boolean isLeaf(TreeNode node)
    {
        boolean ret=false;


        if (node.getChildren().size() == 0)
            ret=true;

        //System.out.println("isLeaf:"+ret+" nodes id:"+nodes.getId());
        return ret;
    }

    private static int countLeaf(TreeNode root)
    {
        int count = 0;

        if (isLeaf(root)) {
            count++;
            //System.out.println("root isLeaf"+count);
        } else {
            //System.out.println("children"+count);
            for (int i=0; i<root.getChildren().size(); i++ ){
                count += countLeaf(root.getChildren().get(i));
                //System.out.println("children isLeaf"+count);
            }
        }
        
        return count;
    }

    private static int maxDepth(ArrayList<TreeNode> tree) {
        int ret=0;
        for (int i=0; i<tree.size();i++){
            int newRet = tree.get(i).getDepth();
            //System.out.println("depth:"+newRet);
            if (ret<newRet)
                ret = newRet;
        }

        return ret;
    }
}