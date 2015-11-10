import java.awt.Graphics2D;

/**
 * Interface renderable
 * All objects that can be rendered on the graphics panel should implement this.
 * 
 * @author kotarou
 */
public interface renderable
{
    public boolean visible = true;

    /**
     * Render the object. 
     */
    public void render(Graphics2D panel);

}
