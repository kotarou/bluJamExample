

/**
 * class vec2
 * A 2D vector for showing position.
 * 
 * @author kotarou
 */
public class vec2d
{
    public final double x;
    public final double y;

    public vec2d(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    public vec2d(int x, int y)
    {
        this.x = (double)x;
        this.y = (double)y;
    }
    // I miss python @property tags
    
    public vec2i toVec2i(){
        return new vec2i((int)this.x, (int)this.y);
    }
}