/**
 * class node
 * Each node represents a single "cell" in the game board.
 * 
 * @author kotarou 
 */

import java.awt.*;

public class node extends gameObject implements renderable
{

    public final int NODE_SIZE;
    
    int hOffset = 1;
    int wOffset = 1;
    
    int wallThickness = 2;
    //int borderWidth = 1;
    
    Color wallColor = Color.WHITE;
    Color nodeColor = Color.RED;
        
    // Top, right, bottom, left borders.
    // 1 = impassable, 0 = passable.
    public node north, east, south, west;
    
    public int contents = 0;
    
    /**
     * Constructor
     */
    public node(gameBoard parent, int locX, int locY)
    {
        // The anchor of a node is the default, top left
        // A node covers no other nodes, and it seems meaningless for a node to cover itself.
        // A node needs no mask or shape.
        
        this.parent = parent;
        
        NODE_SIZE = parent.NODE_SIZE;
        
        this.locX = locX;
        this.locY = locY;
        
        this.posX = parent.left + (this.locX*NODE_SIZE) + ((this.locX+1)*wOffset);
        this.posY = parent.top + (this.locY*NODE_SIZE) + ((this.locY+1)*hOffset);
        
        

        

            
    }

   /**
    * void createlinks()
    * This method creates an effective 2D linkedlist of nodes within the parent gameboard.
    * Each node will link via a north/east/south/west relation to another
    * node if one exists, or null if it doesn't (ie, at a border).
    */
   public void createLinks(){
        // north relation
        if(this.locY > 0)
            this.north = this.parent.nodes[this.locX][this.locY-1];
        else
            this.north = null;
        
        // east relation
        if(this.locX < this.parent.NODES_PER_SIDE - 1)
            this.east = this.parent.nodes[this.locX+1][this.locY];
        else
            this.east = null;
         
        // south relation
        if(this.locY < this.parent.NODES_PER_SIDE - 1)
            this.south = this.parent.nodes[this.locX][this.locY+1];
        else
            this.south = null; 
            
        // west relation
        if(this.locX > 0)
            this.west = this.parent.nodes[this.locX-1][this.locY];
        else
            this.west = null;    

    }

    
    
    public void render(Graphics2D panel){
        panel.setColor(nodeColor);
        int px = (int)this.posX;
        int py = (int)this.posY;
        panel.fillRect(px, py, NODE_SIZE, NODE_SIZE);
        panel.setColor(wallColor);
        if(this.wallNorth())
            panel.fillRect(px,py,NODE_SIZE,wallThickness);
        if(this.wallEast())
            panel.fillRect(px+NODE_SIZE-wallThickness,py,wallThickness,NODE_SIZE);
        if(this.wallSouth())
            panel.fillRect(px,py+NODE_SIZE-wallThickness,NODE_SIZE,wallThickness);
        if(this.wallWest())
            panel.fillRect(px,py,wallThickness,NODE_SIZE);
    }
    
    public boolean wallNorth(){
        return this.north == null;
    }    
    public boolean wallSouth(){
        return this.south == null;
    }    
    public boolean wallEast(){
        return this.east == null;
    }    
    public boolean wallWest(){
        return this.west == null;
    }
    
}
