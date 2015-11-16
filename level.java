import java.util.LinkedList;
import java.util.ArrayList;
/**
 * class level
 * Controller for each level
 * 
 * @author kotarou
 */
public class level
{
    private game parent;
    public double scoreMultiplier = 1.0;
    gameBoard board;
    grad token;
    public double maxScore;
    public double score;
    public boolean passed;
    ArrayList<renderable> renderComponents;
    ArrayList<tickable> tickComponents;
    public boolean active = false;
    ArrayList<vec2i> goalNodes;
    //public int 
    int lives = 0;
    vec2i start;
    /**
     * Constructor for objects of class level
     */
    public level(game parent, double multi, vec2i start, ArrayList<vec2i> goals, double gradEnergy, int[] walls, int lives)
    {
        this.maxScore = gradEnergy * scoreMultiplier;
        this.parent = parent;
        this.passed = false;
        
        this.lives = lives;
        
        renderComponents = new ArrayList<>();
        tickComponents = new ArrayList<>();
        this.start = start;
        board = new gameBoard();
        token = new grad(start, board, this, gradEnergy);
        renderComponents.add(board);
        renderComponents.add(token);
        
        // Set up the in-game elements
        board.nodes[start.y][start.x].setType("start");
        for(vec2i loc : goals)
            board.nodes[loc.y][loc.x].setType("goal");


        goalNodes = goals;
        tickComponents.add(token);
        
        // Wall boundaries around level
        renderComponents.add(new wall(0, 0, board.NODES_PER_SIDE, 1, board));
        renderComponents.add(new wall(board.NODES_PER_SIDE-1, 0, 1, board.NODES_PER_SIDE, board));
        renderComponents.add(new wall(0, board.NODES_PER_SIDE-1, board.NODES_PER_SIDE, 1, board));
        renderComponents.add(new wall(0, 0, 1, board.NODES_PER_SIDE, board));
        
        /*int[][] mask = {{1, 1, 1, 1, 1, 1, 0, 0}, 
                        {0, 0, 0, 0, 0, 1, 0, 1},
                        {0, 1, 1, 1, 0, 1, 0, 1},
                        {0, 1, 0, 0, 0, 0, 0, 0},
                        {0, 1, 1, 1, 1, 1, 0, 1},
                        {0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 1, 1, 1, 1, 1, 1, 1},};
        renderComponents.add(new wall(1, 1, mask, board));

        
        renderComponents.add(new wall(14, 16, 10, 1, board));
        renderComponents.add(new wall(12, 11, 3, 11, board));
        renderComponents.add(new wall(14, 20, 10, 1, board));*/
        
        renderComponents.add(new container(50, 70, walls[0], "wall (1x5)"));
        renderComponents.add(new container(50, 120, walls[1], "wall (5x1)"));
    }
    
    public void start(boolean run){
        //this.passed = false;
        if(this.lives <= 0)
        {
            System.out.println("You have lost!");
            return;
        }
        this.active = run;
        token.goalNodes.clear();
        for(vec2i loc : this.goalNodes)
            token.addGoal(loc);
        
        
        token.energy = token.maxEnergy;
        
        if(run)
        {
            token.moveToLocation(this.start);
            token.setupPath();
        }
    }
    
    public void end(boolean office){
        if(office)       
        {
            System.out.println("Grad student has reached office. You failed!");
            this.score = 0;
            this.lives -= 1;
        }
        else
        {
            System.out.println("The grad ran out of energy.... Congrats!");
            if(!this.passed)
            {
                this.parent.score += this.score;
            }
                
            this.passed = true;
        }
        this.active = false;
    }

    public void tick(){
        if(!this.active)
            return;
        if(!passed)
            this.score = this.maxScore - (this.token.energy*this.scoreMultiplier);
        for(tickable t : this.tickComponents)
            t.tick();
    }
    
}
