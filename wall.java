import java.awt.Color;
import java.awt.Graphics2D;
/**
 * Class wall
 * A simple impassable object
 * 
 * @author kotarou
 */
public class wall extends gameObject implements renderable
{
    public Color color = Color.YELLOW;
    
    public wall(int locX, int locY, int lWidth, int lHeight, gameBoard parent)
    {
        // The anchor is top left = default.
        
        this.locX = locX;
        this.locY = locY;
        
        this.parent = parent;
        
        this.posX = this.getPosXFromLoc();
        this.posY = this.getPosYFromLoc();
        
        this.lWidth = lWidth;
        this.lHeight = lHeight;
        
        if(this.valid() != 0)
            throw new RuntimeException("Invalid wall!");
        
        this.setupCoverNodes();
        
        for(node n : this.nodes)
            n.passable = false;
    }
    
        public wall(int locX, int locY, int[][] mask, gameBoard parent)
    {
        // The anchor is top left = default.
        
        this.locX = locX;
        this.locY = locY;
        
        this.parent = parent;
        
        this.posX = this.getPosXFromLoc();
        this.posY = this.getPosYFromLoc();
        
        // rotating this 90 dgerees so it is actually useful
        this.shapeMask = mask; //new int[mask[0].length][mask.length];
        /*for(int i = 0; i < mask[0].length; i++)
            for(int j = 0; j < mask.length; j++)
            {
                this.shapeMask[i][j] = mask[j][i];
            }*/
        
        this.lWidth = this.shapeMask[0].length;
        this.lHeight = this.shapeMask.length;
        
        if(this.valid() != 0)
            throw new RuntimeException("Invalid wall!");
        
        this.setupCoverNodes();
        
        for(node n : this.nodes)
            n.passable = false;
    }
    
    
    
    
    public void render(Graphics2D panel){
        panel.setColor(this.color);
        
        for(int i = 0; i < lWidth; i++)
            for(int j = 0; j < lHeight; j++)
            {
                double px = this.parent.nodes[locY+j][locX+i].posX;
                double py = this.parent.nodes[locY+j][locX+i].posY;
                if(this.shapeMask == null || this.shapeMask[j][i] == 1)
                    panel.fillRect((int)px, (int)py, this.parent.NODE_SIZE, this.parent.NODE_SIZE);
            }
            
            
    }


}
