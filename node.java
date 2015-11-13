/**
 * class node
 * Each node represents a single "cell" in the game board.
 * 
 * @author kotarou 
 */

import java.awt.*;

public class node extends gameObject implements renderable//, Comparable<node>
{

    public double pathingDist = Double.MAX_VALUE-10;
    public node pathingPrev = null;
    public String type;
    
    public final int NODE_SIZE;
       
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
        
        this.loc = new vec2i(locX, locY);
        
        this.pos = new vec2d(parent.left + (this.loc.x*NODE_SIZE) + ((this.loc.x+1)*parent.H_OFFSET),
                                parent.top + (this.loc.y*NODE_SIZE) + ((this.loc.y+1)*parent.W_OFFSET));

        this.type = type;   
        
        this.setType();
    }

    public void resetPathing(){
        pathingDist = Double.MAX_VALUE-10;
        pathingPrev = null;
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
        if(this.type.equals("wall")){
            this.nodeColor = Color.BLACK;
            this.passable = false;
        }
    }
    
   /**
    * void createlinks()
    * This method creates an effective 2D linkedlist of nodes within the parent gameboard.
    * Each node will link via a north/east/south/west relation to another
    * node if one exists, or null if it doesn't (ie, at a border).
    */
   public void createLinks(){
        // north relation
        if(this.loc.y > 0)
            this.north = this.parent.nodes[this.loc.y-1][this.loc.x];
        else
            this.north = null;
        
        // east relation
        if(this.loc.x < this.parent.NODES_PER_SIDE - 1)
            this.east = this.parent.nodes[this.loc.y][this.loc.x+1];
        else
            this.east = null;
         
        // south relation
        if(this.loc.y < this.parent.NODES_PER_SIDE - 1)
            this.south = this.parent.nodes[this.loc.y+1][this.loc.x];
        else
            this.south = null; 
            
        // west relation
        if(this.loc.x > 0)
            this.west = this.parent.nodes[this.loc.y][this.loc.x-1];
        else
            this.west = null;    

    }

    
    
    public void render(Graphics2D panel){
        panel.setColor(this.nodeColor);
        panel.fillRect((int)this.pos.x, (int)this.pos.y, NODE_SIZE, NODE_SIZE);
    }
    
    public boolean wallNorth(){
        return this.north == null || !this.north.passable;
    }    
    public boolean wallSouth(){
        return this.south == null || !this.south.passable;
    }    
    public boolean wallEast(){
        return this.east == null || !this.east.passable;
    }    
    public boolean wallWest(){
        return this.west == null || !this.west.passable;
    }
    
    /*
     * Basic heurisitc for path finding
     */
    /*public int compareTo(node other){
        // TODO use a non-hardcoded goal
        return (int)(Math.sqrt(Math.pow((this.loc.x-17),2) + Math.pow((this.loc.y-17),2)) -
                Math.sqrt(Math.pow((other.loc.x-17),2) + Math.pow((other.loc.y-17),2)));
    }*/
    
}
