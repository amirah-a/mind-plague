import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class Jail {
    private GamePanel panel;
    private int x;
    private int y;
    private int width;
    private int height;
 
    private int dx;
    private int dy;
    private int originalX, originalY;

    private BufferedImage jail;
 
    public Jail(GamePanel p, int xPos, int yPos){
        panel = p;
        x = xPos;
        y = yPos;
        
        originalX = xPos;
        originalY = yPos;
        
        dx = 8;
        dy = 2;
  
        width = 60;
        height = 65;

        jail = ImageManager.loadBufferedImage("images/jail.png");
    }

    public void draw(Graphics2D g2){
        g2.drawImage(jail, x, y, width, height, null);
    }
        
        
    public void move(int direction){
      
        if (direction == 3) {		// move left
            moveRight();
        }	
        else				// move right
        if (direction == 4) {
            if(x > 320)
                moveLeft();
        }
    }

    public void moveLeft(){
        x = x - dx;
    }

    public void moveRight(){
        x = x + dx;
    }

    public void moveUp(){
        if(y > 325)
            y = y - dy;
    }

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void reset(){
        x = originalX;
        y = originalY;
    }
}
