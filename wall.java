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
    
    public wall(int locX, int locY, gameBoard parent)
    {
        // The anchor is top left = default.
        
        this.locX = locX;
        this.locY = locY;
        
        this.parent = parent;
        
        this.posX = this.getPosXFromLoc();
        this.posY = this.getPosYFromLoc();
        
        // The size of this implmentation of wall is 1x1
        if(this.valid() != 0)
            throw new RuntimeException("Invalid wall!");
        
        this.setupCoverNodes();
        
        for(node n : this.nodes)
            n.passable = false;
    }
    
    public void render(Graphics2D panel){
        panel.setColor(this.color);
        panel.fillRect((int)this.posX, (int)this.posY, this.parent.NODE_SIZE, this.parent.NODE_SIZE);
    }


}
