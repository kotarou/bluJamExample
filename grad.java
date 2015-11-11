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
    public double posX;
    public double posY;
    public double radius;
    public Color tokenColor;
    public int velocity = 10;
    
    //public PriorityQueue<node> pathableNodes;
    public ArrayList<node> nodeList;
    public ArrayList<Double> distList;
    public ArrayList<node> prevList;
    
    public LinkedList<node> path;
    
    public node currentNode;
    
    public node goalNode;
    
    public boolean active;
    
    /**
     * Constructor for objects of class grad
     */
    public grad(int locX, int locY, gameBoard board)
    {      
        this.active = true;
        
        this.anchor = 1;
        // The grad token can be in multiple nodes, depending on its exact position.
        // This does not count as covering a node.
        // A grad is circular (for now) and has no mask
        
        this.parent = board;
        
        this.locX = locX;
        this.locY = locY;
        
        // For initial setup, we stick it in the center of a node
        this.posX = this.getPosXFromLoc() + ( this.parent.NODE_SIZE / 2);
        this.posY = this.getPosYFromLoc() + ( this.parent.NODE_SIZE / 2);

        this.radius = 3;
        this.tokenColor = Color.BLUE;
        
        nodeList = new ArrayList<>();
        distList = new ArrayList<>();
        prevList = new ArrayList<>();
        
        path = new LinkedList<>();
        // End location: (17,17)
        // First, set up the list of nodes.
        //pathableNodes = new PriorityQueue<>();
        for(int i = 0; i < parent.NODES_PER_SIDE; i++){
            for(int j = 0; j < parent.NODES_PER_SIDE; j++){
                //pathableNodes.offer(parent.nodes[i][j]);
                if(parent.nodes[i][j].passable){
                    nodeList.add(parent.nodes[i][j]);

                }
            }
        }
 
        currentNode = parent.nodes[this.locX][this.locY];
        currentNode.pathingDist = 0.0;
    }

    public void render(Graphics2D panel){
        panel.setColor(this.tokenColor);
        panel.fillOval((int)(this.posX-this.radius), (int)(this.posY-this.radius), (int)this.radius*2, (int)this.radius*2);

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
    
    public void setupPath(){
        
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
        
        node tNode = goalNode;
        
        while(tNode.pathingPrev != null)
        {
            path.addFirst(tNode);
            tNode = tNode.pathingPrev;
        }
        //System.out.println(path);
    }
    
    
    public void tick(){
        if(!this.active)
            return;
       
        if(currentNode.locX == goalNode.locX && currentNode.locY == goalNode.locY)
        {
            System.out.println("Made it!");
            this.active = false;
            return;
        }
        
        if(path.size() > 0)
            this.moveToNode(path.poll());

        
        /*    
        
        if(goalNode.north != null && goalNode.north.passable && !pathableNodes.contains(goalNode.north) && !closedNodes.contains(goalNode.north))
            pathableNodes.offer(goalNode.north);
        
        if(goalNode.east != null && goalNode.east.passable && !pathableNodes.contains(goalNode.east) && !closedNodes.contains(goalNode.east))
            pathableNodes.offer(goalNode.east);
        
        if(goalNode.south != null && goalNode.south.passable && !pathableNodes.contains(goalNode.south) && !closedNodes.contains(goalNode.south))
            pathableNodes.offer(goalNode.south);
        
        if(goalNode.west != null && goalNode.west.passable && !pathableNodes.contains(goalNode.west) && !closedNodes.contains(goalNode.west))
            pathableNodes.offer(goalNode.west);
        */
        }
        
            
        /*// If we have made it, stop

        //System.out.println("At: "+this.locX + " " + this.locY);
        // Add any new reachable nodes
        addNodes(currentNode);
        // Move to the best node 
        // Needs to be a pathable node!
        currentNode = pathableNodes.poll();
        this.moveToNode(currentNode); 
    }
    
    
    */
    public void moveToNode(node target){
        this.locX = target.locX;
        this.locY = target.locY;
        
        // For initial setup, we stick it in the center of a node
        this.posX = this.getPosXFromLoc() + ( this.parent.NODE_SIZE / 2);
        this.posY = this.getPosYFromLoc() + ( this.parent.NODE_SIZE / 2);
        
    }
    
}