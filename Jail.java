import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;


public class Jail {
    private GamePanel panel;
    private int x;
    private int y;
    private int width;
    private int height;
 
    private int dx;
    private int dy;

    private BufferedImage Platform;
 
    public Jail(GamePanel p, int xPos, int yPos){
        panel = p;
        x = xPos;
        y = yPos;
  
        dx = 8;
        dy = 0;
  
        width = 112;
        height = 120;

        Platform = ImageManager.loadBufferedImage("images/jail.png");
    }

    public void draw(Graphics2D g2){
        g2.drawImage(Platform, x, y, width, height, null);
    }
        
    public void move(int direction){
      
    }

}
