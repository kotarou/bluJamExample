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
    
    double x = 0;
    ArrayList<renderable> renderComponents;
    ArrayList<tickable> tickComponents;
    
    JFrame windowFrame;
    JRootPane windowRoot;
    JMenuBar menu;
    // The graphics panel needs to be grabbed every frame. 
    Graphics2D graphicsPanel;
    JPanel inputPanel;
    JPopupMenu popup;
    
    
    gameBoard board;
    grad greg;
    
    String mouseAttachment;
    
    /**      */
    public void run(){
        // Game timing
        long lastLoopTime = System.nanoTime();

        mouseAttachment = "";
        
        renderComponents = new ArrayList<>();
        tickComponents = new ArrayList<>();
        
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
        
        UI.addButton("Spawn Next Level", UI::quit);
        UI.addButton("Restart", this::start);
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
        
        
        
        // Set up the in-game elements
        board = new gameBoard();
        greg = new grad(10, 20, board);
        renderComponents.add(board);
        renderComponents.add(greg);
        
        board.nodes[10][20].setType("start");
        board.nodes[17][17].setType("goal");
        board.nodes[30][30].setType("goal");
        board.nodes[18][18].setType("goal");
        
        tickComponents.add(greg);
        
        // Wall boundaries around level
        renderComponents.add(new wall(0, 0, board.NODES_PER_SIDE, 1, board));
        renderComponents.add(new wall(board.NODES_PER_SIDE-1, 0, 1, board.NODES_PER_SIDE, board));
        renderComponents.add(new wall(0, board.NODES_PER_SIDE-1, board.NODES_PER_SIDE, 1, board));
        renderComponents.add(new wall(0, 0, 1, board.NODES_PER_SIDE, board));
        
        int[][] mask = {{1, 1, 1, 1, 1, 1, 0, 0}, 
                        {0, 0, 0, 0, 0, 1, 0, 1},
                        {0, 1, 1, 1, 0, 1, 0, 1},
                        {0, 1, 0, 0, 0, 0, 0, 0},
                        {0, 1, 1, 1, 1, 1, 0, 1},
                        {0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 1, 1, 1, 1, 1, 1, 1},};
        renderComponents.add(new wall(1, 1, mask, board));

        
        renderComponents.add(new wall(14, 16, 10, 1, board));
        renderComponents.add(new wall(12, 11, 3, 11, board));
        renderComponents.add(new wall(14, 20, 10, 1, board));
        
        renderComponents.add(new container(50, 50, 2, "wall (1x5)"));
        renderComponents.add(new container(50, 100, 3, "wall (5x1)"));
        
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
        start();
        
        
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

    public void start(){
        //Stuff that needs to be at the end of setup.
        greg.goalNodes = new LinkedList<>();
        
        greg.addGoal(17, 17);
        greg.addGoal(30, 30);
        greg.addGoal(18, 18);
        greg.moveToLocation(20,10);
        greg.setupPath();
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
        }
        
        if(action.equals("pressed"))
        {
            // Find out if we have clicked in a container
            for(renderable r : renderComponents)
            {
                if(r instanceof container)
                {
                    // Ugly cast, but it works
                    container t = (container) r;
                    if( x < t.posX || x > t.posX+100 || y < t.posY || y > t.posY+40)
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
                            if( i < board.NODES_PER_SIDE){
                                board.nodes[locY][i].setType("wall");
                            }
                        }
                    }
                    if(mouseAttachment.equals("wall (1x5)"))
                    {
                        for(int i = locY; i < locY + 5; i++)
                        {
                            if( i < board.NODES_PER_SIDE){
                                board.nodes[i][locX].setType("wall");
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
        if(x < board.left || x > board.left + board.width)
            return -1;
        return (int)Math.floor((x - board.left) / (board.NODE_SIZE+board.W_OFFSET));
    }

    public int getLocYFromPos(double y){
        if(y < board.top || y > board.top + board.height)
            return -1;
        return (int)Math.floor((y - board.top) / (board.NODE_SIZE+board.H_OFFSET));
    }
    
    public String formatKey(String input){
        if(input.length() == 0)
            return "";
        return input.toLowerCase();
    }
    
    public int gameLogic(double dt){      
        for(tickable t : tickComponents)
            t.tick();

            return 0;
    }

    public int gameRender(){
        // Clear whatever was previously rendered. 
        // This does mean we will need to redraw every object (every frame)!
        UI.clearGraphics();
        graphicsPanel = UI.getGraphics();
        graphicsPanel.setColor(Color.BLACK);
        // FPS counter
        UI.drawString(String.format("FPS: %4.2f", fps), 20, 30);
        
        
        
        for(renderable r : renderComponents){
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



