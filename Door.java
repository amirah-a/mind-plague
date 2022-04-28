import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;


public class Door {
    private GamePanel panel;
    private int x;
    private int y;
    private int width;
    private int height;
 
    private int dx;
    private int dy;
    private int originalX, originalY;
    private BufferedImage door;
    
    private boolean locked;
 
    public Door(GamePanel p, int xPos, int yPos, int w, int h, String fileName){
        panel = p;
        x = xPos;
        y = yPos;
  
        dx = 8;
        dy = 0;
  
        width = w;
        height = h;

        originalX = xPos;
        originalY = yPos;


        door = ImageManager.loadBufferedImage(fileName);

        locked = true;
    }

    public void draw(Graphics2D g2){
        g2.drawImage(door, x, y, width, height, null);
    }
        

    public void move(int direction){
      
        if (direction == 3) {		// move left
            if (x > 500-door.getWidth())
                moveRight();
        }	
        else				// move right
        if (direction == 4) {
            if(x > 420)
                moveLeft();
        }
        // System.out.println("Door: " + x);
    }

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void moveLeft(){
        x = x - dx;
    }

    public void moveRight(){
        x = x + dx;
    }

    public void reset(){
        x = originalX;
        y = originalY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return x;
    }

    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }

}
