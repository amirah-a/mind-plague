import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Enemy {
    private GamePanel panel;
    
    private int x, y;
    private int width, height;
    private int dx, dy;

    private Random random;
    private Animation enemy;

    private String path;

    public Enemy(GamePanel p, int width, int height, int enemyType){
        panel = p;

        random = new Random();
        
        this.width = width;
        this.height = height;
        x = panel.getWidth() + random.nextInt(50);
        y = random.nextInt(panel.getHeight() - 350) + 355 - height;
        path = null;
        setEnemyType(enemyType);

        enemy = new Animation(panel, this.width, this.height, false);
        loadAnimationFrames(enemy, path, 6, true);
        dx = 2; //default speed
        dy = 0;
    }

    public void setXY(int x, int y){
        this.x = x;
        this.y =y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void loadAnimationFrames(Animation animation,String path, int amt, boolean loadReverse){
        //load attack animation
        Image stripImage = ImageManager.loadImage(path);
        int imageWidth = (int) stripImage.getWidth(null) / amt;
        int imageHeight = stripImage.getHeight(null);

        if(!loadReverse){
           for(int i=0; i < amt; i++){
            BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) frameImage.getGraphics();

            g.drawImage(stripImage, 0, 0, imageWidth, imageHeight, i*imageWidth, 0, (i*imageWidth)+imageWidth, imageHeight, null);
        
            animation.addFrame(frameImage, 100);
            } 
        }else{
            for(int i=amt-1; i >= 0; i--){
                BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) frameImage.getGraphics();
    
                g.drawImage(stripImage, 0, 0, imageWidth, imageHeight, i*imageWidth, 0, (i*imageWidth)+imageWidth, imageHeight, null);
            
                animation.addFrame(frameImage, 150);
            } 
        }
    }

    public void move(){
        x -= dx;
        enemy.update();
    }

    public void draw(Graphics2D g2){
        enemy.draw(g2, x, y);
    }

    public void setEnemyType(int enemyType){ 
        switch(enemyType) {
            case 1:
              path = "images/Biker_run.png";
              break;
            case 2:
              path = "images/Punk_run.png";
              break;
            default:
              path = "images.Biker_run.png";
        }
    }

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, width, height);
    }

    public boolean collidesWithBullet(Bullet b){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double bulletRect = b.getBoundingRectangle();
      
        return myRect.intersects(bulletRect);
    }

    public boolean collidesWithEnemy(Enemy e){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double enemyRect = e.getBoundingRectangle();
      
        return myRect.intersects(enemyRect);
    }

}
