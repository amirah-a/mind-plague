import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

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
    private Random random;
    private boolean isActive;
    private long attackTimeElapsed;

    private Image[] healthBars;
    public static int health;
 
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
        random = new Random();
    
        healthBars = new Image[6];
        loadImages();
        health = 5;

        isActive = true;
    }

    public boolean getIsActive(){
        return isActive;
    }

    public void setIsActive(boolean status){
        isActive = status;
    }
    public void draw(Graphics2D g2){
        if (isActive){
            g2.drawImage(pelican, x, y, width, height, null);
            g2.drawImage(healthBars[health], x, y - healthBars[health].getHeight(null), null);
        }
    }
        
    public void move(int direction){
      if (isActive){
          if (direction == 3) {		// move left
            moveRight();
          }	
          else				// move right
          if (direction == 4) {
              if(x > 200)
                  moveLeft();
          }
      }  
    }

    public void moveLeft(){
        x = x - dx;
    }

    public void moveRight(){
        x = x + dx;
    }


    
    public int getOriginalY(){
        return originalY;
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

    public boolean attack(){
        attackTimeElapsed++;
        if(attackTimeElapsed>25){
            attackTimeElapsed = 0;
            int attackChance = random.nextInt(3);
            if(attackChance == 0){
                return true;
            } 
        }
        return false;
    }

    public int getDX(){
        return dx;
    }

    public void decreaseHealth(){
        health--;
    }

    public int getHealth(){
        return health;
    }

    public void loadImages(){
        healthBars[0] = ImageManager.loadImage("images/health0.png");
        healthBars[1] = ImageManager.loadImage("images/health1.png");
        healthBars[2] = ImageManager.loadImage("images/health2.png");
        healthBars[3] = ImageManager.loadImage("images/health3.png");
        healthBars[4] = ImageManager.loadImage("images/health4.png");
        healthBars[5] = ImageManager.loadImage("images/health5.png");
    }

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, width, height);
    }
    
    public boolean collidesWithBullet(Bullet b){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double bulletRect = b.getBoundingRectangle();
      
        return myRect.intersects(bulletRect);
    }
}
