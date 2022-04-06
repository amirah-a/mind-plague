import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class Emotion {
    
    protected GamePanel panel;

    public static int x = 50, y = 400; // all emotions should have the same x y
    protected int width, height;

    protected int dx, dy;

    protected Image currIdleImage;
    protected Image[] idleImages;

    public Emotion(GamePanel p){
        panel = p;
        
        idleImages = new Image[2];
        width = 48;
        height = 48;
    }

    protected abstract void loadImages();
    public abstract void update();
    public abstract void draw(Graphics g2);

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, width, height);
    }

    public boolean collidesWithEnemy(Enemy e){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double enemyRect = e.getBoundingRectangle();

        return myRect.intersects(enemyRect);
    }

}
