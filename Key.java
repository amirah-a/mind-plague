import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;


public class Key {
    private GamePanel panel;
    private int x;
    private int y;
    private int width;
    private int height;
 
    private int dx;
    private int dy;

    private BufferedImage Key;
 
    public Key(GamePanel p, int xPos, int yPos){
        panel = p;
        x = xPos;
        y = yPos;
  
        dx = 8;
        dy = 0;

        Key = ImageManager.loadBufferedImage("images/key.png");
        width = Key.getWidth();
        height = Key.getHeight();
    }

    public void draw(Graphics2D g2){
        g2.drawImage(Key, x, y, width, height, null);
    }

    public void draw(Graphics2D g2, int x, int y){
        g2.drawImage(Key, x, y, width, height, null);
    }
        
    public void move(int direction){
      
        if (direction == 3) {		// move left
            // if (x < 550)
                moveRight();
        }	
        else				// move right
        if (direction == 4) {
            // if(x > -160)
                moveLeft();
        }

    }

    public void moveLeft(){
        x = x - dx;
    }

    public void moveRight(){
        x = x + dx;
    }

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, width, height);
    }

}
