/**
 * class gameBoard
 * The wrapper that will contain all the "in-game" elements.
 * 
 * @author kotarou 
 */

import java.awt.*;

public class gameBoard implements renderable
{
    int left = 270;
    int top = 70;
    int width = 400;
    int height = 400;
    
    /**
     * Constructor
     */
    public gameBoard()
    {
        
    }

    public void render(Graphics2D panel){
        System.out.println("asdf");
        panel.fillRect(left, top, width, height);
    }
    
}
