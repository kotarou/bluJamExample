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
    
    public void render(Graphics2D panel){
        panel.setColor(this.color);
        for(int i = 0; i < lWidth; i++)
            for(int j = 0; j < lHeight; j++)
            {
                double px = this.parent.nodes[locX+i][locY+j].posX;
                double py = this.parent.nodes[locX+i][locY+j].posY;
                panel.fillRect((int)px, (int)py, this.parent.NODE_SIZE, this.parent.NODE_SIZE);
            }
            
            
    }


}
