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
    
    //public int 

    /**
     * Constructor for objects of class level
     */
    public level(game parent, double multi)
    {
        this.maxScore = 100.0 * scoreMultiplier;
        this.parent = parent;
        this.passed = false;
        
        renderComponents = new ArrayList<>();
        tickComponents = new ArrayList<>();
        
        board = new gameBoard();
        token = new grad(10, 20, board, this);
        renderComponents.add(board);
        renderComponents.add(token);
        
        // Set up the in-game elements
        board.nodes[10][20].setType("start");
        board.nodes[17][17].setType("goal");
        board.nodes[30][30].setType("goal");
        board.nodes[18][18].setType("goal");
        
        tickComponents.add(token);
        
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
        
        renderComponents.add(new container(50, 60, 2, "wall (1x5)"));
        renderComponents.add(new container(50, 110, 3, "wall (5x1)"));
    }
    
    public void start(){
        this.passed = false;
        this.active = true;
        token.goalNodes = new LinkedList<>();
        token.energy = token.maxEnergy;
        
        token.addGoal(17, 17);
        token.addGoal(30, 30);
        token.addGoal(18, 18);
        token.moveToLocation(20,10);
        token.setupPath();
    }
    
    public void end(boolean office){
        if(office)       
        {
            System.out.println("Grad student has reached office. You failed!");
        }
        else
        {
            System.out.println("The grad ran out of energy.... Congrats!");
            this.parent.score += this.score;
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
