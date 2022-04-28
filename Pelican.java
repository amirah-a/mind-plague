import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;


public class Pelican {
    private GamePanel panel;
    private int x;
    private int y;
    private int width;
    private int height;
 
    private int dx;
    private int dy;
    private int originalX, originalY;
    private BufferedImage pelican;
    
    private boolean locked;
 
    public Pelican(GamePanel p, int xPos, int yPos, int w, int h, String fileName){
        panel = p;
        x = xPos;
        y = yPos;
  
        dx = 8;
        dy = 0;
  
        width = w;
        height = h;

        originalX = xPos;
        originalY = yPos;

        pelican = ImageManager.loadBufferedImage(fileName);

        locked = true;
    }

    public void draw(Graphics2D g2){
        g2.drawImage(pelican, x, y, width, height, null);
    }
        
    public void move(int direction){
      
        if (direction == 3) {		// move left
            // if (x > 500-pelican.getWidth())
                moveRight();
        }	
        else				// move right
        if (direction == 4) {
            if(x > 200)
                moveLeft();
        }
        System.out.println("pelican: " + x);
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
