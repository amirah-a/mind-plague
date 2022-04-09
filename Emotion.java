import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;


public abstract class Emotion {
    
    protected GamePanel panel;

    public static int x = 50, y = 400; // all emotions should have the same x y
    protected int width, height;

    protected int dx, dy;

    protected Image currIdleImage;
    protected Image[] idleImages;

    //protected Animation[] animations;
    protected Animation currAnimation;

    protected HashMap<String, Animation> animations;

    protected boolean unlocked;

    public Emotion(GamePanel p){
        panel = p;
        
        //idleImages = new Image[2];
        animations = new HashMap<String, Animation>();
        width = 48;
        height = 48;
    }

    protected abstract void loadAnimations();
    public abstract void update();
    public abstract void draw(Graphics2D g2);

    public Animation loadAnimationFrames(String path, int amt, boolean loadReverse){
        Animation animation = new Animation(panel, this.width, this.height);
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

        return animation;
    }

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, width, height);
    }

    public boolean collidesWithEnemy(Enemy e){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double enemyRect = e.getBoundingRectangle();

        return myRect.intersects(enemyRect);
    }

    // for levels
    public void setUnlocked(boolean value){
        unlocked = value;
    }

    public boolean isUnlocked(){
        return unlocked;
    }

}
