/**
 * class node
 * Each node represents a single "cell" in the game board.
 * 
 * @author kotarou 
 */

import java.awt.*;

public class node extends gameObject implements renderable, Comparable<node>
{

    public double pathingDist = Double.MAX_VALUE-10;
    public node pathingPrev = null;
    public String type;
    
    public final int NODE_SIZE;
    
    int hOffset = 1;
    int wOffset = 1;
    
    Color nodeColor;
        
    public node north, east, south, west;
    
   
    public boolean isBorder = false;
    
    /**
     * Constructor
     */
    public node(gameBoard parent, int locX, int locY) {
        this(parent, locX, locY, "floor");
    }
    
    public node(gameBoard parent, int locX, int locY, String type)
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

        this.type = type;   
        
        this.setType();
    }

    public void setType(){
        this.setType(this.type);
    }
    
    public void setType(String type){
        // This is a doubleup in some cases.
        this.type = type;
        
        if(this.type.equals("floor"))
            this.nodeColor = Color.WHITE;
        if(this.type.equals("goal"))
            this.nodeColor = Color.RED;
        if(this.type.equals("start"))
            this.nodeColor = Color.BLUE;
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
            this.north = this.parent.nodes[this.locY-1][this.locX];
        else
            this.north = null;
        
        // east relation
        if(this.locX < this.parent.NODES_PER_SIDE - 1)
            this.east = this.parent.nodes[this.locY][this.locX+1];
        else
            this.east = null;
         
        // south relation
        if(this.locY < this.parent.NODES_PER_SIDE - 1)
            this.south = this.parent.nodes[this.locY+1][this.locX];
        else
            this.south = null; 
            
        // west relation
        if(this.locX > 0)
            this.west = this.parent.nodes[this.locY][this.locX-1];
        else
            this.west = null;    

    }

    
    
    public void render(Graphics2D panel){
        panel.setColor(this.nodeColor);
        panel.fillRect((int)this.posX, (int)this.posY, NODE_SIZE, NODE_SIZE);
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
    
    /*
     * Basic heurisitc for path finding
     */
    public int compareTo(node other){
        // TODO use a non-hardcoded goal
        return (int)(Math.sqrt(Math.pow((this.locX-17),2) + Math.pow((this.locY-17),2)) -
                Math.sqrt(Math.pow((other.locX-17),2) + Math.pow((other.locY-17),2)));
    }
    
}
