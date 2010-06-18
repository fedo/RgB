/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.tei;

import it.unibo.cs.rgb.util.TreeNode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gine
 */
public class TeiSvg {
    int rCircle = 40;
    ArrayList<HashMap> witlist;

    public TeiSvg(ArrayList<HashMap> witlist) {
        this.witlist = witlist;
    }
    // verifica che l'oggetto passato sia un albero e che abbia tutti gli
    // elementi che servono per generare l'albero
    public boolean canGetSvg(){
        boolean ret = false;

        int wLength=witlist.size(), derCounter=0, sigilCounter=0, idCounter=0, missingCounter=0;

        for (int i=0; i<wLength; i++){
            if (witlist.get(i).get("der") != null)
                derCounter++;

            if (witlist.get(i).get("sigil") != null)
                sigilCounter++;

            if (witlist.get(i).get("id") != null)
                idCounter++;

            if (witlist.get(i).get("missing") != null)
                missingCounter++;
        }

        if (derCounter == (wLength-1) && sigilCounter == wLength && idCounter == wLength && missingCounter == wLength) {
            int counter=0;
            for (int d=0; d<wLength; d++)
                for (int i=0; i<wLength; i++)
                    if(witlist.get(d).get("der") != null)
                        if (((String)witlist.get(i).get("id")).equalsIgnoreCase((String) witlist.get(d).get("der")))
                            counter++;

            if (counter == (wLength-1))
                ret=true;
        }

        return ret;
    }
    // disegna svg con un array di TreeNode
    public String getSvg(){
        int footerSpace=80,yText=5, nGreekNodes = 0;
        int xCirclePos, yCirclePos,xFirstPLinePos,yFirstPLinePos,xSecondPLinePos,ySecondPLinePos;
        String labelId, out="", classStyle,cssUri="http://ltw1001.web.cs.unibo.it/svg.css";
        ArrayList<TreeNode> nodes = getTree();

        int maxX=maxPos(nodes, true);
        int maxY=maxPos(nodes, false);

        out += "<?xml-stylesheet type=\"text/css\" href=\""+cssUri+"\" ?>"+"\n";
        out += "<html xmlns:svg=\"http://www.w3.org/2000/svg\">"+"\n";
        out += "<body>"+"\n";
        out += "<svg:svg width=\""+(maxX+footerSpace)+"\" height=\""+(maxY+footerSpace)+"\" version=\"1.1\" >"+"\n";

        //disegna le linee
        for (int i = 0; i < nodes.size(); i++){
             if (! isLeaf(nodes.get(i))) {
                int nSibling=nodes.get(i).getChildren().size();
                for (int s=0;s<nSibling;s++) {
                    xFirstPLinePos=nodes.get(i).getX();
                    yFirstPLinePos=nodes.get(i).getY();
                    xSecondPLinePos=nodes.get(i).getChildren().get(s).getX();
                    ySecondPLinePos=nodes.get(i).getChildren().get(s).getY();

                    String line="<svg:line id=\""+nodes.get(i).getId()+"\" x1=\""+xFirstPLinePos+"\" y1=\""+yFirstPLinePos+"\" x2=\""+xSecondPLinePos+"\" y2=\""+ySecondPLinePos+"\"/>";
                    out += line+"\n";
                 }
              }
        }

        //disegna cerchi e testo
        for (int i = 0; i < nodes.size(); i++){
            if (nodes.get(i).getMissing()) {
                labelId = getGreek(nGreekNodes);
                classStyle="witness-greek";
                nGreekNodes++;
            } else {
                labelId=nodes.get(i).getSigil();
                classStyle="witness";
            }

            xCirclePos = nodes.get(i).getX();
            yCirclePos = nodes.get(i).getY();

            String text="<svg:text class=\""+classStyle+"\" id=\""+nodes.get(i).getId()+"\" y=\""+yText+"\">"+ labelId +"</svg:text>",
                   circle="<svg:circle class=\""+classStyle+"\" id=\""+nodes.get(i).getId()+"\" r=\""+rCircle+"\"/>",
                   group="<svg:g transform=\"translate("+xCirclePos+" "+yCirclePos+")\">";

            out += group+"\n";
            out += circle+"\n";
            out += text+"\n";
            out += "</svg:g>"+"\n";
        }

        out += "</svg:svg>"+"\n";
        out += "</body>"+"\n";
        out += "</html>"+"\n";

        return out;
    }
    // genera l'albero a partire dal hashmap ricevuta con il costruttore
    private ArrayList<TreeNode> getTree(){
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        int ySpaceBetweenCircle = 60, xSpaceBetweenCircle = 60;

        //creo i nodi
        for (int i=0; i<witlist.size(); i++){
            String id= (String) witlist.get(i).get("id");
            String sigil= (String) witlist.get(i).get("sigil");
            if ((Boolean) witlist.get(i).get("missing"))
                nodes.add(new TreeNode(null, id, sigil, true));
            else
                nodes.add(new TreeNode(null, id, sigil, false));
        }

        //collego i nodi fra loro cosi' che siano un albero generico
        for (int i=0; i<nodes.size(); i++){
            if (witlist.get(i).get("der") != null){
                String parent= (String) witlist.get(i).get("der");

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
    //TODO:
    public boolean hasDtd(){
        return true;
    }
    //TODO:
    public boolean isValid(){
        return true;
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
    //preleva la lettera greca da usare per i nodi missing=true in base ad un counter
    private String getGreek(int nGreekNodes){
        String gLetters="αβγδεζηθικλμνξοπρςτυφχψω";

        return ""+gLetters.charAt(nGreekNodes);
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

    private boolean isLeaf(TreeNode node){
        boolean ret=false;

        if (node.getChildren().size() == 0)
            ret=true;

        return ret;
    }
}