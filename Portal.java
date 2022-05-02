import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Portal {
    private GamePanel panel;
    private int x;
    private int y;
    private int width;
    private int height;
 
    private int dx;
    private int dy;
    private int originalX, originalY;
    private BufferedImage platform;
    
    public Portal(GamePanel p, int xPos, int yPos, int w, int h, String fileName){
        panel = p;
        x = xPos;
        y = yPos;
  
        dx = 8;
        dy = 0;
  
        width = w;
        height = h;

        originalX = xPos;
        originalY = yPos;
        
        platform = ImageManager.loadBufferedImage(fileName);
    }

    public void draw(Graphics2D g2){
        g2.drawImage(platform, x, y, width, height, null);
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

        // System.out.println("Portal x: " + x);
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
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
