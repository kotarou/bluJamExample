/**
 * Main class game
 * This is the main game class, where we set up the game state
 * 
 * @author kotarou
 */

import ecs100.*;
import java.util.*;
import java.io.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//import java.lang.reflect.*;

public class game{
    // Game variables
    final int TARGET_FPS = 60;
    final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;   
    final int WINDOW_WIDTH    = 800;
    final int WINDOW_HEIGHT   = 600;
        
    int textWidth      = 0;
    int graphicsWidth;
    int graphicsHeight;
    double textRatio;
    
    int sleepTme = 0;
    boolean gameRunning = true;
    double fps = 0;
    int lastLoopTime = 0;;
    
    double score = 0;

    
    JFrame windowFrame;
    JRootPane windowRoot;
    JMenuBar menu;
    // The graphics panel needs to be grabbed every frame. 
    Graphics2D graphicsPanel;
    JPanel inputPanel;
    JPopupMenu popup;
    
    level currentLevel;
    LinkedList<level> levels;
    
    //gameBoard board;
    //grad greg;
    
    String mouseAttachment;
    
    /**      */
    public void run(){
        // Game timing
        long lastLoopTime = System.nanoTime();

        mouseAttachment = "";
        
        levels = new LinkedList<>();
        
        UI.initialise();
        
        // Get the more complicated objects for complex graphics (if needed)
        windowFrame = UI.getFrame();
        windowRoot = windowFrame.getRootPane();
        menu = windowRoot.getJMenuBar();
        inputPanel = (JPanel)windowRoot.getComponent(0);
        

        // Should make the window non-resizable, but currently fails to do so.
        // TODO: Make this actually work.
        UI.setWindowSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        windowFrame.setResizable(false);
        
        // Disable the menubar at the top
        menu.setEnabled(false);
        menu.setVisible(false);
        
        // Control the input panel on the left
        // TODO: Make this actually work.
        //inputPanel.setSize(new Dimension(100,500));
        
        UI.addButton("Next Level", this::runNextLevel);
        UI.addButton("Reset Walls", this::resetW);
        UI.addButton("Reset Grad", this::resetG);
        UI.addButton("Quit", UI::quit);
        

        // Call after all the buttons / etc are added.
        // Ratio where 0.0 = no text panel and 1.0 = no graphics panel
        if(textWidth == 0)
            textRatio = 0.0;
        else
            textRatio = (double)textWidth / (double)(WINDOW_WIDTH);
        UI.setDivider(textRatio);
        
        
        // Once the window is set up, lets get some of the graphics values
        // Note that resizing the window will not modify this - it will just hide parts!
        graphicsWidth = UI.getCanvasHeight();
        graphicsHeight = UI.getCanvasWidth();
        
        
        // Don't draw grpahics immediately. 
        UI.setImmediateRepaint(false);
        
        levels.add(new level(this, 1.0));
        levels.add(new level(this, 2.0));
        

        
        UI.setKeyListener(this::keyResponder);
        UI.setMouseMotionListener(this::mouseResponder);
        
        /*
        popup = new JPopupMenu();
        JMenuItem m1 = new JMenuItem("Wall");
        popup.add(m1);*/
        
        //windowFrame.addMouseListener(new mouseResponder());
        
        // May need some relection....
        /*Field field = UI.class.getDeclaredField("canvas");
        field.setAccessible(true);
        Object value = field.get(UI.theUI);
        System.out.println(value);*/
        //windowFrame.addMouseListener(new mouseResponder());
        
        windowFrame.pack();
        runNextLevel();
        
        
        while(gameRunning){
            int logicError=0, renderError=0;
            
            // Time since last update
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double dt = updateLength / ((double)OPTIMAL_TIME);
            
            // frame counter
            lastLoopTime += updateLength;
            //fps++
            fps = (double)1.0e9 / updateLength;
            
            // Update game logic
            logicError = gameLogic(dt);
            
            // Render
            if(logicError == 0)
                renderError = gameRender();
            
            // Post logic here
            if(renderError != 0)
                gameRunning = false;

            
            try{
                Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
            }
            catch(Exception e){
                // Smething went wrong!
                gameRunning = false;
            }
            
        }
    }

    public void runNextLevel(){
        if(levels.size() == 0)
        {
            System.out.println("No more levels!");
            return;
        }
        // Either this is the first lelvel, or we have passed the previous
        if(currentLevel == null || currentLevel.passed)
        {
            currentLevel = levels.pop();
            currentLevel.start();
        }
        else
        {
            System.out.println("You have not passed the current level!");
        }

        
    }
    
    public void resetW(){
        for(int i = 0; i < this.currentLevel.board.NODES_PER_SIDE; i++)
        {
            for(int j = 0; j < this.currentLevel.board.NODES_PER_SIDE; j++)
            {
                if(this.currentLevel.board.nodes[j][i].type.equals("wall"))
                    this.currentLevel.board.nodes[j][i].setType("floor");
            }
        }
        
        for(renderable r : currentLevel.renderComponents)
        {
                if(r instanceof container)
                {
                    // Ugly cast, but it works
                    container t = (container) r;
                    t.current = t.capacity;
                }
         }
    }
    
    public void resetG(){
        currentLevel.start();
    }
    
    public void keyResponder(String input){
        input = formatKey(input);
        
        /*if(input.equals("w"))
            ((grad)this.renderComponents.get(1)).attemptMove(0);
        if(input.equals("d"))
            ((grad)this.renderComponents.get(1)).attemptMove(1);
        if(input.equals("s"))
            ((grad)this.renderComponents.get(1)).attemptMove(2);
        if(input.equals("a"))
            ((grad)this.renderComponents.get(1)).attemptMove(3); */
        
        System.out.println(input);
    }
    
    public void mouseResponder(String action, double x, double y){
        // Fimd out what tile is under the mouse
        int locX = getLocXFromPos(x);
        int locY = getLocYFromPos(y);
        
        
        
        if(action.equals("clicked"))
        {
            System.out.println("Mouse x: " + x + " y: " + y + " action: " + action);
            System.out.println("Node type: " + currentLevel.board.nodes[locY][locX].type);
        }
        
        if(action.equals("pressed"))
        {
            // Find out if we have clicked in a container
            for(renderable r : currentLevel.renderComponents)
            {
                if(r instanceof container)
                {
                    // Ugly cast, but it works
                    container t = (container) r;
                    if( x < t.pos.x || x > t.pos.x+100 || y < t.pos.y || y > t.pos.y+40)
                        continue;
                    else
                    {
                        // We have clicked within the container
                        if(t.current > 0)
                        {
                            t.current -= 1;
                            mouseAttachment = t.type;
                        }
                    }
                }
            }
            
        }
        
         if(action.equals("released"))
        {
            if(mouseAttachment.length() > 0)
            {
                System.out.println("Locx: " + locX + " LocY: " + locY);
                if(locX > 0 && locY > 0) 
                {
                    if(mouseAttachment.equals("wall (5x1)"))
                    {
                        for(int i = locX; i < locX + 5; i++)
                        {
                            if( i < currentLevel.board.NODES_PER_SIDE){
                                currentLevel.board.nodes[locY][i].setType("wall");
                            }
                        }
                    }
                    if(mouseAttachment.equals("wall (1x5)"))
                    {
                        for(int i = locY; i < locY + 5; i++)
                        {
                            if( i < currentLevel.board.NODES_PER_SIDE){
                                currentLevel.board.nodes[i][locX].setType("wall");
                            }
                        }
                    }                        
                    
                    mouseAttachment = "";
                }
            }
            //popup.setVisible(false);
        }    
    }
    
    public int getLocXFromPos(double x){
        if(x < currentLevel.board.left || x > currentLevel.board.left + currentLevel.board.width)
            return -1;
        return (int)Math.floor((x - currentLevel.board.left) / (currentLevel.board.NODE_SIZE+currentLevel.board.W_OFFSET));
    }

    public int getLocYFromPos(double y){
        if(y < currentLevel.board.top || y > currentLevel.board.top + currentLevel.board.height)
            return -1;
        return (int)Math.floor((y - currentLevel.board.top) / (currentLevel.board.NODE_SIZE+currentLevel.board.H_OFFSET));
    }
    
    public String formatKey(String input){
        if(input.length() == 0)
            return "";
        return input.toLowerCase();
    }
    
    public int gameLogic(double dt){      
        currentLevel.tick();
        return 0;
    }

    public int gameRender(){
        // Clear whatever was previously rendered. 
        // This does mean we will need to redraw every object (every frame)!
        UI.clearGraphics();
        graphicsPanel = UI.getGraphics();
        graphicsPanel.setColor(Color.BLACK);
        // FPS counter
        UI.drawString(String.format("FPS: %4.2f", this.fps), 20, 20);
        UI.drawString(String.format("Score: %4.2f", this.score), 20, 30);
        UI.drawString(String.format("Score (this level): %4.2f", this.currentLevel.score), 20, 40);
        UI.drawString(String.format("Grad energy: %4.2f", currentLevel.token.energy), 20, 50);
        
        
        
        for(renderable r : currentLevel.renderComponents){
            r.render(graphicsPanel);
        }
        
        // As immediate mode is set to false, we will need to explicitly call repaint
        UI.repaintGraphics();

        return 0;
    }
    
    public static void main(String[] args){
        game obj = new game();
        obj.run();
    }    

    
    
    
    
}



