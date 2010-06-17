//----------------------------------------------------------------------------//
//                                                                            //
//                              T r e e N o d e                               //
//                                                                            //
//  Copyright (C) Herve Bitteur 2000-2007. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Contact author at herve.bitteur@laposte.net to report bugs & suggestions. //
//----------------------------------------------------------------------------//

package it.unibo.cs.rgb.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gine
 */

public class TreeNode implements java.io.Serializable
{
    protected String id;
    protected String sigil;
    protected Integer depth;
    protected Boolean missing;
    protected Boolean drawned;
    protected Integer posX, posY;
    protected final List<TreeNode> children = new ArrayList<TreeNode>();
    protected TreeNode parent;

    public TreeNode (TreeNode parent, String id, String sigil, Boolean missing)
    {
        if (parent != null) {
            parent.addChild(this);
        }

        this.setDepth(0);
        this.setId(id);
        this.setSigil(sigil);
        this.setMissing(missing);
        this.setDrawned(false);
        this.setPosition(0,0);
    }

    public List<TreeNode> getChildren ()
    {
        return children;
    }

    public void setChildrenParent ()
    {
        for (TreeNode node : children) {
            node.setParent(this);
            node.setChildrenParent(); // Recursively
        }
    }

    public TreeNode getNextSibling ()
    {
        if (parent != null) {
            int index = parent.children.indexOf(this);

            if (index < (parent.children.size() - 1)) {
                return parent.children.get(index + 1);
            }
        }

        return null;
    }

    public void setParent (TreeNode parent)
    {
        this.parent = parent;
    }

    public TreeNode getParent ()
    {
        return parent;
    }

    public TreeNode getPreviousSibling ()
    {
        if (parent != null) {
            int index = parent.children.indexOf(this);

            if (index > 0) {
                return parent.children.get(index - 1);
            }
        }

        return null;
    }

    public synchronized void addChild (TreeNode node) {
        children.add(node);
        node.setParent(this);
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getId() {
        return  id;
    }

    public void setSigil (String sigil) {
        this.id = sigil;
    }

    public String getSigil() {
        return  sigil;
    }

    public void setMissing(Boolean missing) {
        this.missing = missing;
    }

    public Boolean getMissing() {
        return missing;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getDepth() {
        return depth;
    }
    
    public void setDrawned(Boolean drawned) {
        this.drawned = drawned;
    }

    public Boolean getDrawned() {
        return drawned;
    }

    public void setPosition(Integer x, Integer y) {
        this.posX = x;
        this.posY = y;
    }

    public Integer getX() {
        return posX;
    }

    public Integer getY() {
        return posY;
    }
}
