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
            x = x - dx;
            
            if (x < 0)
              x = 0;
        }	
        else				// move right
        if (direction == 4) {
            x = x + dx;
            
            if (x+75 > panel.getWidth())
              x = panel.getWidth() - 60;
        }

    }

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, width, height);
    }

}
