/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.gwt.tei;

import it.unibo.cs.rgb.util.TreeNode;
import java.io.InputStream;
import java.util.ArrayList;
import org.w3c.dom.NodeList;
/**
 *
 * @author gine
 */
public class TeiSvg {
    int rCircle = 40;
    NodeList witness;

    TeiSvg(InputStream input) {
        TeiDocument xml = new TeiDocument(input);
        witness = (NodeList) xml.xpathQueryNL("//witList/witness");
    }
    
    public boolean hasDtd(){
        return true;
    }
    
    public boolean isValid(){
        return true;
    }

    public boolean canGetSvg(){
        boolean ret = false;
        int wLength=witness.getLength(), derCounter=0, sigilCounter=0, idCounter=0, missingCounter=0;

        for (int i=0; i<wLength; i++){
            if (witness.item(i).getAttributes().getNamedItem("der") != null)
                derCounter++;

            if (witness.item(i).getAttributes().getNamedItem("sigil") != null)
                sigilCounter++;

            if (witness.item(i).getAttributes().getNamedItem("id") != null)
                idCounter++;

            if (witness.item(i).getAttributes().getNamedItem("missing") != null)
                missingCounter++;
        }

        if (derCounter == --wLength && sigilCounter == wLength && idCounter == wLength && missingCounter == wLength) {
            ret=true;
        }

        return ret;
    }

    public String getSvg(){
        int footerSpace=80;//, fontSize = 0, nFontSize = 20, gFontSize =26;
        //String colourStroke = "", nColourStroke = "#83adf7", gColourStroke = "red";
        String littleLetters="filtjr", mediumLetters="abcdeghnopqzsuvkzy0123456789", bigLetters="mw";
        int xLitWordLoc =-1, xMedWordLoc =-5, xBigWordLoc =-7, xGWordLoc = -7, xWordLoc = 0,nGreekNodes = 0;
        int yWordLoc = 5, yGWordLoc = 7;
        int xCirclePos, yCirclePos,xFirstPLinePos,yFirstPLinePos,xSecondPLinePos,ySecondPLinePos;
        String labelId ="", out="", classStyle="";

        ArrayList<TreeNode> nodes = getTree();

        int maxX=maxPos(nodes, true);
        int maxY=maxPos(nodes, false);

        out.concat("<?xml-stylesheet type=\"text/css\" href=\"svg.css\" ?>");
        out.concat("<html xmlns:svg=\"http://www.w3.org/2000/svg\">");
        out.concat("<body>");
        out.concat("<svg:svg width=\""+(maxY+footerSpace)+"\" height=\""+(maxX+footerSpace)+"\" version=\"1.1\" >");

        //String lineStyle="style=\"fill:none;fill-rule:evenodd;stroke:"+nColourStroke+";stroke-width:2.93415856;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1";
        //disegna le linee
        for (int i = 0; i < nodes.size(); i++){
             if (! isLeaf(nodes.get(i))) {
                int nSibling=nodes.get(i).getChildren().size();
                for (int s=0;s<nSibling;s++) {
                    xFirstPLinePos=nodes.get(i).getX();
                    yFirstPLinePos=nodes.get(i).getY();
                    xSecondPLinePos=nodes.get(i).getChildren().get(s).getX();
                    ySecondPLinePos=nodes.get(i).getChildren().get(s).getY();

                    //String line="<svg:line id=\""+nodes.get(i).getId()+"\" x1=\""+xFirstPLinePos+"\" y1=\""+yFirstPLinePos+"\" x2=\""+xSecondPLinePos+"\" y2=\""+ySecondPLinePos+"\" " +lineStyle+"\"/>";
                    String line="<svg:line id=\""+nodes.get(i).getId()+"\" x1=\""+xFirstPLinePos+"\" y1=\""+yFirstPLinePos+"\" x2=\""+xSecondPLinePos+"\" y2=\""+ySecondPLinePos+"\"/>";
                    out.concat(line);
                 }
              }
        }

        //disegna cerchi e testo
        for (int i = 0; i < nodes.size(); i++){
            if (nodes.get(i).getMissing()) {
                labelId = getGreek(nGreekNodes);
                xWordLoc=xGWordLoc;
                yWordLoc=yGWordLoc;
                classStyle="witness-greek";
                //fontSize=gFontSize;
                //colourStroke=gColourStroke;
                nGreekNodes++;
            } else {
                xWordLoc = 0;
                labelId=nodes.get(i).getSigil();
                xWordLoc += howMuchMove(littleLetters,xLitWordLoc,labelId);
                xWordLoc += howMuchMove(mediumLetters,xMedWordLoc,labelId);
                xWordLoc += howMuchMove(bigLetters,xBigWordLoc,labelId);
                classStyle="witness";
                //fontSize=nFontSize;
                //colourStroke=nColourStroke;
            }

            //String circleStyle="style=\"fill:"+colourStroke+";fill-opacity:1;fill-rule:nonzero;stroke:"+colourStroke+";stroke-width:3;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-dashoffset:0;stroke-opacity:1",
            //    textStyle="style=\"font-size:"+fontSize+"px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;writing-mode:lr-tb;text-anchor:start;fill:black;fill-opacity:1;stroke:none;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;font-family:Arial";

             xCirclePos = nodes.get(i).getX();
             yCirclePos = nodes.get(i).getY();
             int xIdPos=xCirclePos+xWordLoc, yIdPos=yCirclePos+yWordLoc;
             //String text="<svg:text id=\""+nodes.get(i).getId()+"\" x=\""+xIdPos+"\" y=\""+yIdPos+"\"  " + textStyle + "\">"+ labelId +"</svg:text>",
             //     circle="<svg:circle id=\""+nodes.get(i).getId()+"\" cx=\""+xCirclePos+"\" cy=\""+yCirclePos+"\" r=\""+rCircle+"\" "+circleStyle+"\" />";
             String text="<svg:text class=\""+classStyle+"\" id=\""+nodes.get(i).getId()+"\" x=\""+xIdPos+"\" y=\""+yIdPos+"\">"+ labelId +"</svg:text>",
                  circle="<svg:circle class=\""+classStyle+"\" id=\""+nodes.get(i).getId()+"\" cx=\""+xCirclePos+"\" cy=\""+yCirclePos+"\" r=\""+rCircle+"\"/>";

             out.concat(circle);
             out.concat(text);
        }

        out.concat("</svg:svg>");
        out.concat("</body>");
        out.concat("</html>");


        return out;
    }

    private ArrayList<TreeNode> getTree(){
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        int ySpaceBetweenCircle = 60, xSpaceBetweenCircle = 60;

        for (int i=0; i<witness.getLength(); i++){
            String id= witness.item(i).getAttributes().getNamedItem("id").getTextContent().toLowerCase();
            String sigil= witness.item(i).getAttributes().getNamedItem("sigil").getTextContent().toLowerCase();
            if (witness.item(i).getAttributes().getNamedItem("missing").getTextContent().equalsIgnoreCase("true"))
                nodes.add(new TreeNode(null, id, sigil, true));
            else
                nodes.add(new TreeNode(null, id, sigil, false));
        }

        //collego i nodi fra loro cosi' che siano un albero generico
        for (int i=0; i<nodes.size(); i++){
            if (witness.item(i).getAttributes().getNamedItem("der") != null){
                String parent=witness.item(i).getAttributes().getNamedItem("der").getTextContent().toLowerCase();

                for (int n=0; n<nodes.size();n++){
                    if (nodes.get(n).getId().matches(parent)){
                        nodes.get(n).addChild(nodes.get(i));
                        break;
                    }
                 }
            }
        }

        //setto la profondita' dei nodi
        for (int i=0;i<nodes.size();i++){
             nodes.get(i).setDepth(countDepth(nodes.get(i)));
        }

        //setta le coordinate x,y delle foglie
        int nDrawned=1, nNodes = nodes.size();
        for (int n=0;n<nNodes;n++){
            if(! nodes.get(n).getDrawned()){
                if (isLeaf(nodes.get(n))){
                   int nSibling=nodes.get(n).getParent().getChildren().size();
                   int depth=nodes.get(n).getDepth(),
                           y=rCircle+(depth-1)*(rCircle*2+ySpaceBetweenCircle)+rCircle;
                   for (int s=0; s<nSibling; s++){
                       if (isLeaf(nodes.get(n).getParent().getChildren().get(s))){
                         int x=rCircle+(nDrawned-1)*(rCircle*2+xSpaceBetweenCircle)+rCircle;
                            nodes.get(n).getParent().getChildren().get(s).setDrawned(true);
                            nodes.get(n).getParent().getChildren().get(s).setPosition(x, y);
                            nDrawned++;
                       }
                   }

                }
            }
        }

        //setta le coordinate x,y dei nodi padre
        nDrawned--;
        while (nDrawned < nNodes) {
            for (int n=0; n < nNodes; n++) {
                if(! nodes.get(n).getDrawned()){
                    int nSibling = nodes.get(n).getChildren().size();
                    boolean allSiblingDrawned = true;
                    for (int c=0; c<nSibling;c++) {
                        if (! nodes.get(n).getChildren().get(c).getDrawned()){
                          allSiblingDrawned = false;
                          break;
                        }
                    }

                    if (allSiblingDrawned) {
                        int depth = nodes.get(n).getDepth(),
                            y=rCircle+(depth-1)*(rCircle*2+ySpaceBetweenCircle)+rCircle;
                        if (nSibling == 1)
                            nodes.get(n).setPosition(nodes.get(n).getChildren().get(0).getX(), y);
                        else
                            nodes.get(n).setPosition(((nodes.get(n).getChildren().get(--nSibling).getX()+nodes.get(n).getChildren().get(0).getX())/2), y);

                        nodes.get(n).setDrawned(true);
                        nDrawned++;
                    }
                }
            }
        }

        return nodes;
    }

    // pos=true --> x, pos=false --> y
    private int maxPos(ArrayList<TreeNode> nodes,boolean pos) {
        int ret=0,swapRet=0;

        for (int n=0; n<nodes.size(); n++){
            if (pos)
                swapRet=nodes.get(n).getX();
            else
                swapRet=nodes.get(n).getY();

            if (ret<swapRet){
                ret=swapRet;
            }
        }

        return ret;
    }

    private boolean isOdd(int n){
        boolean ret=false;

        if (n%2 > 0) {
            ret = true;
        }

        return ret;
    }

    private String getGreek(int nGreekNodes){
        String gLetters="αβγδεζηθικλμνξοπρςτυφχψω";

        return ""+gLetters.charAt(nGreekNodes);
    }

    //calcola di quanto deve essere spostata la label di un nodo rispetto il
    //centro di questo, alla lunghezza della parola e al tipo di lettera
    private int howMuchMove(String letters, int x, String id){
        int ret = 0;

        for (int i=0; i<id.length();i++){
            if(letters.indexOf(id.charAt(i)) > -1 ) {
                    ret += x;
            }
        }

        return ret;
    }

    private int countDepth(TreeNode node){
        int count=0;
        TreeNode swapNode = node.getParent();

        while (swapNode != null){
            count++;
            swapNode = swapNode.getParent();
        }

        return ++count;
    }

    private int maxDepth(ArrayList<TreeNode> tree){
        int ret=0;
        for (int i=0; i<tree.size();i++){
            int newRet = tree.get(i).getDepth();
            if (ret<newRet)
                ret = newRet;
        }

        return ret;
    }

    private boolean isLeaf(TreeNode node){
        boolean ret=false;

        if (node.getChildren().size() == 0)
            ret=true;

        return ret;
    }

    private int countLeaf(TreeNode root){
        int count = 0;

        if (isLeaf(root)) {
            count++;
        } else {
            for (int i=0; i<root.getChildren().size(); i++ ){
                count += countLeaf(root.getChildren().get(i));
            }
        }

        return count;
    }
}
