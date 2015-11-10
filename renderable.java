import java.awt.Graphics2D;

/**
 * Abstract class renderable
 * All objects that can be rendered on the grpahics panel should inherit this.
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
