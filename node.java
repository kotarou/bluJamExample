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
    int hOffset = 2;
    int wOffset = 2;
    
    gameBoard parent;
    
    // Top, right, bottom, left borders.
    // 1 = impassable, 0 = passable.
    public int[] borders = {0,0,0,0};
    
    public int contents = 0;
    
    /**
     * Constructor
     */
    public node(gameBoard parent)
    {
        this.parent = parent;
        this.left = parent.left;
        this.top = parent.top;
        
    }

    public void render(Graphics2D panel){
        System.out.println("asdf");
        panel.fillRect(left, top, width, height);
    }
    
    public boolean wallNorth(){
        return this.borders[0] == 0;
    }    
    public boolean wallSouth(){
        return this.borders[2] == 0;
    }    
    public boolean wallEast(){
        return this.borders[1] == 0;
    }    
    public boolean wallWest(){
        return this.borders[3] == 0;
    }
    
}
