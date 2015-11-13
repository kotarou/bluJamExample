import java.awt.Graphics2D;
import java.awt.Color;
/**
 * class container
 * This is the class that controls the "storage" of placeable objects
 * 
 * @author kotarou
 */
public class container implements renderable
{
    public vec2i pos;
    public int capacity;
    public String type;
    public int current;
    
    /**
     * Constructor for objects of class container
     */
    public container(int x, int y, int capacity, String type)
    {
        this.pos = new vec2i(x, y);
        this.capacity = capacity;
        this.current = capacity;
        this.type = type;
    }


    public void render(Graphics2D panel)
    {
        panel.setColor(Color.BLACK);
        panel.drawRect(this.pos.x, this.pos.y, 150, 40);
        panel.drawString(type + ": " + this.current + " of " + this.capacity, this.pos.x + 5, this.pos.y + 15); 
    }
}
