import java.awt.Color;
import java.util.ArrayList;
/**
 * Abstract class gameObject
 * All objects that can be placed on the game board.
 * 
 * @author kotarou
 */
public abstract class gameObject
{
    // The anchor. 0 = TOPLEFT, 1 = CENTER
    public int anchor = 0;
    
    // (x,y) coordinates of the object's TOPLEFT in grid-space
    // Note that this does not honour the anchor position.
    public int locX;
    public int locY;
    
    // (x,y) coordinates of the object's anchor in canvas-space
    public double posX;
    public double posY;
   
    // Size of the object
    public int lWidth = 1;
    public int lHeight = 1;
    
    // Shape of the object, using a masking 2D array. 1 = Object. 0 = Empty.
    public int[][] shapeMask = null;
    
    // The nodes this object covers
    public ArrayList<node> nodes = null;
    
    // The parent board
    public gameBoard parent;
    
    public boolean passable = true;
    
    
    /**
     * Check if the object will fit on the game board.
     * @returns 
     *      0 if the object will fit
     *      1 if the object extends off the horizontal edges (east, west)
     *      2 if the object extends off the vertical edges (north, south)
     */
    public int valid(){
        // If the object will extend off the game board, return an error number.
        if(this.parent.NODES_PER_SIDE < (this.locX + lWidth))
            return 1;
        if(this.parent.NODES_PER_SIDE < (this.locY + lHeight))
            return 2;   
        return 0;
    }
    
    /**
     * Set up the ArrayList that details which nodes this object covers.
     */
    public void setupCoverNodes(){
        // Set the nodes List up first.
        this.nodes = new ArrayList<>();
        if(this.shapeMask == null)
        {
            for(int i = this.locX; i < this.locX+lWidth; i++)
                for(int j = this.locY; j < this.locY+lHeight; j++)
                    this.nodes.add(this.parent.nodes[j][i]);
        }
        else
        {
        // Check each entry in the mask, and if it is a 1, add the node that part of this object covers
        for(int i = 0; i < lWidth; i++)
            for(int j = 0; j < lHeight; j++)
                if(this.shapeMask[j][i] == 1)
                    this.nodes.add(this.parent.nodes[j][i]);
        }
    }
    
    public double getPosXFromLoc(){
        return this.parent.nodes[locY][locX].posX;
    }
    
    public double getPosYFromLoc(){
        return this.parent.nodes[locY][locX].posY;
    }
}