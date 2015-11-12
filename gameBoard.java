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
    public final int NODES_PER_SIDE = 36;
    public final int NODE_SIZE = 10;
    
    public final int H_OFFSET = 1;
    public final int W_OFFSET = 1;
    
    int left = 270;
    int top = 70;
    int width = 400;
    int height = 400;
    
    public node[][] nodes;
    
    /**
     * Constructor
     */
    public gameBoard()
    {
        // Create the interior nodes
        this.nodes = new node[NODES_PER_SIDE][NODES_PER_SIDE];
        for(int i = 0; i < NODES_PER_SIDE; i++){
            for(int j = 0; j < NODES_PER_SIDE; j++){
                this.nodes[j][i] = new node(this, i, j);
            }
        }
        for(int i = 0; i < NODES_PER_SIDE; i++){
            for(int j = 0; j < NODES_PER_SIDE; j++){
                this.nodes[j][i].createLinks();
            }
        }
    }

    public void render(Graphics2D panel){
        panel.fillRect(left, top, width, height);
        for(int i = 0; i < NODES_PER_SIDE; i++){
            for(int j = 0; j < NODES_PER_SIDE; j++){
                this.nodes[j][i].render(panel);
            }
        }
    }
    
    
}
