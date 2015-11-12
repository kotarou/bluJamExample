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
    public int posX;
    public int posY;
    public int capacity;
    public String type;
    public int current;
    
    /**
     * Constructor for objects of class container
     */
    public container(int x, int y, int capacity, String type)
    {
        this.posX = x;
        this.posY = y;
        this.capacity = capacity;
        this.current = capacity;
        this.type = type;
    }


    public void render(Graphics2D panel)
    {
        panel.setColor(Color.BLACK);
        panel.drawRect(this.posX, this.posY, 150, 40);
        panel.drawString(type + ": " + this.current + " of " + this.capacity, this.posX + 5, this.posY + 15); 
    }
}
