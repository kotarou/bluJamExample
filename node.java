/**
 * class node
 * Each node represents a single "cell" in the game board.
 * 
 * @author kotarou 
 */

import java.awt.*;

public class node implements renderable
{
    int left, top;
    int width = 20;
    int height = 20;
    int hOffset = 1;
    int wOffset = 1;
    int posX = 0;
    int posY = 0;
    
    int wallThickness = 2;
    //int borderWidth = 1;
    
    Color wallColor = Color.WHITE;
    Color nodeColor = Color.RED;
        
    gameBoard parent;
    
    // Top, right, bottom, left borders.
    // 1 = impassable, 0 = passable.
    public node north, east, south, west;
    
    public int contents = 0;
    
    /**
     * Constructor
     */
    public node(gameBoard parent, int posX, int posY)
    {
        this.parent = parent;
      
        this.posX = posX;
        this.posY = posY;
        
        this.left = parent.left + (posX*width) + ((posX+1)*wOffset);
        this.top = parent.top + (posY*height) + ((posY+1)*hOffset);
        

            
    }

   /**
    * void createlinks()
    * This method creates an effective 2D linkedlist of nodes within the parent gameboard.
    * Each node will link via a north/east/south/west relation to another
    * node if one exists, or null if it doesn't (ie, at a border).
    */
   public void createLinks(){
        // north relation
        if(this.posY > 0)
            this.north = this.parent.nodes[this.posX][this.posY-1];
        else
            this.north = null;
        
        // east relation
        if(this.posX < this.parent.NODES_PER_SIDE - 1)
            this.east = this.parent.nodes[this.posX+1][this.posY];
        else
            this.east = null;
         
        // south relation
        if(this.posY < this.parent.NODES_PER_SIDE - 1)
            this.south = this.parent.nodes[this.posX][this.posY+1];
        else
            this.south = null; 
            
        // west relation
        if(this.posX > 0)
            this.west = this.parent.nodes[this.posX-1][this.posY];
        else
            this.west = null;    

    }

    
    
    public void render(Graphics2D panel){
        System.out.println("asdf");
        panel.setColor(nodeColor);
        panel.fillRect(left, top, width, height);
        panel.setColor(wallColor);
        if(this.wallNorth())
            panel.fillRect(left,top,width,wallThickness);
        if(this.wallEast())
            panel.fillRect(left+width-wallThickness,top,wallThickness,height);
        if(this.wallSouth())
            panel.fillRect(left,top+height-wallThickness,width,wallThickness);
        if(this.wallWest())
            panel.fillRect(left,top,wallThickness,height);
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
