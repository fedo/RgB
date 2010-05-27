/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.test;

import it.unibo.cs.rgb.gwt.tei.TeiCollection;
import it.unibo.cs.rgb.util.TreeNode;
import java.util.ArrayList;
import org.w3c.dom.NodeList;

/**
 *
 * @author gine
 */
public class TreeTest {
    public static void main(String[] args) {
        System.out.println("Test tree");

        TeiCollection collection = new TeiCollection();
        collection.init("src/java/resources/collection1/parallel segmentation/");
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        NodeList witness = (NodeList) collection.getTeiDocument(0).xpathQueryNL("//witList/witness");

        for (int i=0; i<witness.getLength(); i++){
            String id= witness.item(i).getAttributes().getNamedItem("id").getTextContent();
            if (witness.item(i).getAttributes().getNamedItem("missing") != null)
                nodes.add(new TreeNode(null, id, true));
            else
                nodes.add(new TreeNode(null, id, false));
               //System.out.println(id);
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

        

        for (int i=0; i<nodes.size(); i++){
           System.out.println("node id:"+nodes.get(i).getId()+" node depth:"+nodes.get(i).getDepth());
        }
        
        System.out.println(""+nodes.get(0).getChildren().get(0).getId());
        System.out.println(""+nodes.get(1).getParent().getId());
        System.out.println(""+nLeaf);

    }

    private static int countDepth(TreeNode node){
        int count=0;
        TreeNode swapNode = node.getParent();

        while (swapNode != null){
            count++;
            swapNode = swapNode.getParent();
        }

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
}