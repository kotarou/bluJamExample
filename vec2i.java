
/**
 * class vec2
 * A 2D vector for showing position.
 * 
 * @author kotarou
 */
public class vec2i
{
    public final int x;
    public final int y;

    public vec2i(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public vec2i(double x, double y)
    {
        this.x = (int)x;
        this.y =(int) y;
    }
    
    public vec2d toVec2d(){
        return new vec2d((double)this.x, (double)this.y);
    }
    
    public String toString(){
        return "(x,y): (" + this.x + ", " + this.y +")";
    }
}


