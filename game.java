import ecs100.*;
import java.util.*;
import java.io.*;

import java.awt.*;
import javax.swing.*;

/** 
 *  This is the main game class, where we set up the game state
 * 
 */
public class game{
    // Game variables
    int windowWidth    = 800;
    int windowHeight   = 600;
    int textWidth      = 0;
    int graphicsWidth;
    int graphicsHeight;
    double textRatio;
    
    JFrame windowFrame;
    JRootPane windowRoot;
    JMenuBar menu;
    Graphics2D graphicsPane;
    JPanel inputPanel;
    /**      */
    public void game(){
        UI.initialise();
        
        // Get the more complicated objects for complex graphics (if needed)
        windowFrame = UI.getFrame();
        graphicsPane = UI.getGraphics();
        windowRoot = windowFrame.getRootPane();
        menu = windowRoot.getJMenuBar();
        inputPanel = (JPanel)windowRoot.getComponent(0);
        

        
        UI.setWindowSize(windowWidth,windowHeight);
        windowFrame.setResizable(false);
        
        // Disable the menubar at the top
        menu.setEnabled(false);
        menu.setVisible(false);
        
        // Control the input panel on the left
        //inputPanel.setSize(new Dimension(100,500));
        
        UI.addButton("Spawn Next Level", UI::quit);
        UI.addButton("Quit", UI::quit);
        
        

        
        // Call after all the buttons / etc are added.
        // Ratio where 0.0 = no text panel and 1.0 = no graphics panel
        if(textWidth == 0)
            textRatio = 0.0;
        else
            textRatio = (double)textWidth / (double)(windowWidth);
        UI.setDivider(textRatio);
        
        
        // Once the window is set up, lets get some of the graphics values
        // Note that resizing the window will not modify this - it will just hide parts!
        graphicsWidth = UI.getCanvasHeight();
        graphicsHeight = UI.getCanvasWidth();
        
        // Don't draw grpahics immediately. 
        UI.setImmediateRepaint(false);
        
        
        windowFrame.pack();
    }



    public static void main(String[] args){
        game obj = new game();
    }    

}
