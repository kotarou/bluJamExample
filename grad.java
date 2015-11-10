import java.awt.Color;
import java.awt.Graphics2D;
/**
 * class grad
 * A grad is a token that moves around the gameboard, seeking coffee, 
 * entertaining distractions and eventual publication.
 * 
 * @author kotarou 
 */

public class grad implements renderable 
{
    // But wait you ask, why are these doubles while the game board is a grid!!??
    // The grid is only for placement of objects and visual nicety (and because it makes pathfinding easy).
    // Actual game locations are doubles.

    // The center of the token, not the border!
    public double posX;
    public double posY;
    public double radius;
    public Color tokenColor;

    private gameBoard board;

    /**
     * Constructor for objects of class grad
     */
    public grad(int posX, int posY, gameBoard board)
    {      
        // The starting position, as an offset from the top left of the game board        
        this.posX = board.nodes[posX][posY].posX+4;
        this.posY = board.nodes[posX][posY].posY+3;

        this.board = board;

        this.radius = 7;
        this.tokenColor = Color.BLUE;
    }

    public void render(Graphics2D panel){
        panel.setColor(this.tokenColor);
        panel.fillOval((int)(this.posX-this.radius), (int)(this.posY-this.radius), (int)this.radius*2, (int)this.radius*2);

    }

    public void attemptMove(int direction){
        // the int direction should be made into a direction class at some point
        node currentNode = this.getCurrentNode();
        double d = distToEdge(currentNode, direction);
        System.out.println("d: " + d + " PosX: " + this.posX + " PosY: " + this.posY);

        if(direction==0){
            if(currentNode.north != null || d > 10)
                this.move(0, 10);
            else if ((currentNode.north == null && d > 0) || currentNode.north != null)
                this.move(0, d);
        }
        if(direction==1){
            if(currentNode.east != null || d > 10)
                this.move(1, 10);
            else if ((currentNode.east == null && d > 0) || currentNode.east != null)
                this.move(1, d);
        }
        if(direction==2){
            if(currentNode.south != null || d > 10)
                this.move(2, 10);
            else if ((currentNode.south == null && d > 0) || currentNode.south != null)
                this.move(2, d);
        }
        if(direction==3){
            if(currentNode.west != null || d > 10)
                this.move(3, 10);
            else if ((currentNode.west == null && d > 0) || currentNode.west != null)
                this.move(3, d);
        }
    }

    public void move(int direction, double distance){
        if(direction == 0)
            this.posY -= distance;
        if(direction == 1)
            this.posX += distance;
        if(direction == 2)
            this.posY += distance;
        if(direction == 3)
            this.posX -= distance;
    }

    public node getCurrentNode(){
        // Hard coded values are bad. This method is a bit of a hack, I will clean it up later.
        int i = (int)Math.floor((this.posX - (board.left)) / 22);
        int j = (int)Math.floor((this.posY - (board.top)) / 22);
        if( i  < 0 || j < 0 )
            throw new RuntimeException("Invalid node");
        System.out.println("Node: " +i + j);
        return this.board.nodes[i][j];
    }

    public double distToEdge(node currentNode, int edge){
        // A node is bounded by node.top, node.left, node.top+height, node.left+width
        // dist to edge is positive if you are within the node, negative otherwise
        if(edge == 0)
            return (this.posY-(this.radius+currentNode.posY));
        if(edge == 1)
            return ((currentNode.posX+currentNode.NODE_SIZE)-(this.posX+this.radius));
        if(edge == 2)
            return ((currentNode.posY+currentNode.NODE_SIZE)-(this.posY+this.radius));
        if(edge == 3)
            return ((this.posX-this.radius)-currentNode.posX);

        return -10;
    }

}
