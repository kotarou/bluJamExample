import java.awt.Color;
import java.awt.Graphics2D;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 * class grad
 * A grad is a token that moves around the gameboard, seeking coffee, 
 * entertaining distractions and eventual publication.
 * 
 * @author kotarou 
 */

public class grad extends gameObject implements renderable, tickable 
{
    // But wait you ask, why are these doubles while the game board is a grid!!??
    // The grid is only for placement of objects and visual nicety (and because it makes pathfinding easy).
    // Actual game locations are doubles.

    // The center of the token, not the border!
    public vec2d pos;
    public vec2i loc;
    public double radius;
    public Color tokenColor;
    public int velocity = 10;
    
    //public PriorityQueue<node> pathableNodes;
    public ArrayList<node> nodeList;
    public ArrayList<Double> distList;
    public ArrayList<node> prevList;
    
    public LinkedList<node> path;
    
    public node currentNode;
    
    public LinkedList<node> goalNodes;
    public node currentGoal;
    
    public boolean active;
    
    private level plat;
    
    public double maxEnergy;
    public double energy;
    
    /**
     * Constructor for objects of class grad
     */
    public grad(vec2i loc, gameBoard board, level plat, double maxEnergy)
    {      
        this.active = true;
        this.plat = plat;
        this.anchor = 1;
        // The grad token can be in multiple nodes, depending on its exact position.
        // This does not count as covering a node.
        // A grad is circular (for now) and has no mask
        
        this.parent = board;
        
        this.loc = loc;
        
        // For initial setup, we stick it in the center of a node
        this.pos = new vec2d(this.getPosXFromLoc(this.loc) + ( this.parent.NODE_SIZE / 2),
                       this.getPosYFromLoc(this.loc) + ( this.parent.NODE_SIZE / 2));
         

        this.radius = 3;
        this.tokenColor = Color.BLUE;
        
        nodeList = new ArrayList<>();
        goalNodes = new LinkedList<>();
        path = new LinkedList<>();
        
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;

    }

    public void render(Graphics2D panel){
        panel.setColor(this.tokenColor);
        panel.fillOval((int)(this.pos.x-this.radius), (int)(this.pos.y-this.radius), (int)this.radius*2, (int)this.radius*2);

    }
    // Pathfinding!!
    
    // In keeping with the general idea of tower-defense style games, the path-finding abilities
    // of a grad are limited (on easy levels) to a greedy best-first pathfinding. 
    
    // The heuristic cost of getting to a node is the euclidean distance to the goal
    public node smallest(ArrayList<node> arr){
        node index = null;
        double smallest = Double.MAX_VALUE;
        for(node n : arr)
        {
            if(n.pathingDist <= smallest)
            {
                smallest = n.pathingDist;
                index = n;
            }
        }
        //System.out.println(smallest);
        return index;
    }
        
    public void addGoal(vec2i loc){
        System.out.println("Adding goal.");
        this.goalNodes.add(parent.nodes[loc.y][loc.x]);
    }
    
    public void setupPath(){
        this.active = true;
        this.currentGoal = goalNodes.pop();
        
        System.out.println("Current goal: " + this.currentGoal.loc);
        
        nodeList.clear();
        path.clear();
                
        // End location: (17,17)
        // First, set up the list of nodes.
        //pathableNodes = new PriorityQueue<>();
        for(int i = 0; i < parent.NODES_PER_SIDE; i++){
            for(int j = 0; j < parent.NODES_PER_SIDE; j++){
                parent.nodes[j][i].resetPathing();
                if(parent.nodes[j][i].passable){
                    nodeList.add(parent.nodes[j][i]);

                }
            }
        }
 
        currentNode = parent.nodes[this.loc.y][this.loc.x];
        currentNode.pathingDist = 0.0;
        
        while(nodeList.isEmpty()== false)
        {
            currentNode = smallest(nodeList);
            nodeList.remove(currentNode);
            
                    
            double alt = 0;
            //System.out.println("Node: ("+currentNode.locX+","+currentNode.locY+")");
            alt = currentNode.pathingDist + 1;
            if(currentNode.north != null && currentNode.north.passable && alt < currentNode.north.pathingDist)
            {
                currentNode.north.pathingDist = alt;
                currentNode.north.pathingPrev = currentNode;
            }
            if(currentNode.east != null && currentNode.east.passable && alt < currentNode.east.pathingDist)
            {
                currentNode.east.pathingDist = alt;
                currentNode.east.pathingPrev = currentNode;
            }
            if(currentNode.south != null && currentNode.south.passable && alt < currentNode.south.pathingDist)
            {
                currentNode.south.pathingDist = alt;
                currentNode.south.pathingPrev = currentNode;
            }
            if(currentNode.west != null && currentNode.west.passable && alt < currentNode.west.pathingDist)
            {
                currentNode.west.pathingDist = alt;
                currentNode.west.pathingPrev = currentNode;
            }
        }
        
        node tNode = currentGoal;
        
        while(tNode.pathingPrev != null)
        {
            path.addFirst(tNode);
            tNode = tNode.pathingPrev;
        }
        
        if(path.size() == 0)
        {
            //TODO: Make this actually do something
            // TODO: Fix bug where this occurs when it shouldn't. 
            System.out.println("Grad cannot do anything");
        }
        
    }
    
    
    public void tick(){      
        if(!this.active)
        {
            if(goalNodes.isEmpty())
            {
                this.plat.end(true);
                return;
            }
            else
            {
                this.setupPath();
            }
        }
       
        if(this.loc.x == currentGoal.loc.x && this.loc.y == currentGoal.loc.y)
        {
            System.out.println("Made it!");
            this.active = false;
            return;
        }
        
        if(path.size() > 0)
            if(this.energy > 0)
            {
                this.moveToNode(path.poll());
                this.energy -= 1;
            }
            else
            {
                System.out.println("Grad has run out of energy!");
                this.active = false;
                this.plat.end(false);
            }
        }

        
    public void moveToLocation(vec2i loc){
        this.moveToLocation(loc.x, loc.y);
    }
        
    public void moveToLocation(int x, int y){
        this.loc = new vec2i(x, y);
        //System.out.println("Location: (" + this.loc.x + "," + this.loc.y+")");
        // For initial setup, we stick it in the center of a node
        this.pos = new vec2d(this.getPosXFromLoc(this.loc) + ( this.parent.NODE_SIZE / 2),
                            this.getPosYFromLoc(this.loc) + ( this.parent.NODE_SIZE / 2));
        
        this.currentNode = parent.nodes[this.loc.y][this.loc.x];
    }
        
    public void moveToNode(node target){
        this.loc = target.loc;
        //System.out.println("Location: (" + this.loc.x + "," + this.loc.y+")");
        // For initial setup, we stick it in the center of a node
        this.pos = new vec2d(this.getPosXFromLoc(this.loc) + ( this.parent.NODE_SIZE / 2),
                            this.getPosYFromLoc(this.loc) + ( this.parent.NODE_SIZE / 2));
        
        this.currentNode = parent.nodes[this.loc.y][this.loc.x];
    }
    
}
