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
    public vec2i loc;
    //public int locX;
    //public int locY;
    
    // (x,y) coordinates of the object's anchor in canvas-space
    public vec2d pos;
    //public double posX;
    //public double posY;
   
    // Size of the object
    public vec2i dimension;
    //public int lWidth = 1;
    //public int lHeight = 1;
    
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
        if(this.parent.NODES_PER_SIDE < (this.loc.x + this.dimension.x))
            return 1;
        if(this.parent.NODES_PER_SIDE < (this.loc.y + this.dimension.y))
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
            for(int i = this.loc.x; i < this.loc.x+this.dimension.x; i++)
                for(int j = this.loc.y; j < this.loc.y+this.dimension.y; j++)
                    this.nodes.add(this.parent.nodes[j][i]);
        }
        else
        {
        // Check each entry in the mask, and if it is a 1, add the node that part of this object covers
        for(int i = 0; i < this.dimension.x; i++)
            for(int j = 0; j < this.dimension.y; j++)
                if(this.shapeMask[j][i] == 1)
                    this.nodes.add(this.parent.nodes[j][i]);
        }
    }
    
    public vec2d getPosFromLoc(){
        // This is the third least likable line of code I have ever written.
        return new vec2d(this.parent.nodes[this.loc.y][this.loc.y].pos.x, this.parent.nodes[this.loc.y][this.loc.x].pos.y);
    }
    
    // These are still needed!
    public double getPosXFromLoc(vec2i l2){
        return this.parent.nodes[l2.y][l2.x].pos.x;
    }
    
    public double getPosYFromLoc(vec2i l2){
        return this.parent.nodes[l2.y][l2.x].pos.y;
    }
}