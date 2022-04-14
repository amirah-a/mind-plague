import java.awt.Graphics2D;
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

        door = ImageManager.loadBufferedImage(fileName);

        locked = true;
    }

    public void draw(Graphics2D g2){
        g2.drawImage(door, x, y, width, height, null);
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

    public boolean getIsLocked(){
        return locked;
    }

    // used to reset the door status when unlocked
    public void setIsLocked(boolean status){
        locked = status;
    }
}
