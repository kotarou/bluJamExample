import java.util.ArrayList;
/**
 * class gameBoard
 * The wrapper that will contain all the "in-game" elements.
 * 
 * @author kotarou 
 */

import java.awt.*;

public class gameBoard implements renderable
{
    final int NODES_PER_SIDE = 16;
    
    int left = 270;
    int top = 70;
    int width = 400;
    int height = 400;
    
    node[][] nodes;
    
    /**
     * Constructor
     */
    public gameBoard()
    {
        // Set up the interior nodes
        this.nodes = new node[NODES_PER_SIDE][NODES_PER_SIDE];
        for(int i = 0; i < NODES_PER_SIDE; i++){
            for(int j = 0; j < NODES_PER_SIDE; j++){
                //boolean topWall = false, bot
                //this.nodes[i][j] = new node();
            }
        }
    }

    public void render(Graphics2D panel){
        System.out.println("asdf");
        panel.fillRect(left, top, width, height);
    }
    
}
