import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Egg {
    protected GamePanel panel;
    
    protected int x, y;
    protected int dx, dy;
    protected int width, height;

    protected HashMap<String, Animation> animations;
    protected Animation currAnimation;

    private String eggType; //can be basic, fear, love, etc

    public Egg(GamePanel p, String type){
        panel = p;

        animations = new HashMap<String, Animation>();
        width = 48;
        height = 48;

        eggType = type;
        loadAnimations();
    }

    private void loadAnimations(){
        Animation animation = new Animation(panel, this.width, this.height);
        switch (eggType) {
            case "basic":
                animation = loadAnimationFrames("images/basic_idle_egg_r.png", 8, false);
                animations.put("idle_right", animation);
                animation = loadAnimationFrames("images/basic_idle_egg_l.png", 8, false);
                animations.put("idle_left", animation);
            case "fear":
                animation = loadAnimationFrames("images/fear_idle_egg_r.png", 7, false);
                animations.put("idle_right", animation);
                animation = loadAnimationFrames("images/fear_idle_egg_l.png", 7, false);
                animations.put("idle_left", animation);

            case "love":
                animation = loadAnimationFrames("images/love_idle_egg_r.png", 9, false);
                animations.put("idle_right", animation);
                animation = loadAnimationFrames("images/love_idle_egg_l.png", 9, false);
                animations.put("idle_left", animation);
            case "rage":
                animation = loadAnimationFrames("images/rage_idle_egg_r.png", 9, false);
                animations.put("idle_right", animation);
                animation = loadAnimationFrames("images/rage_idle_egg_l.png", 9, false);
                animations.put("idle_left", animation);
            case "sad":
                animation = loadAnimationFrames("images/sadness_idle_egg_r.png", 7, false);
                animations.put("idle_right", animation);
                animation = loadAnimationFrames("images/sadness_idle_egg_l.png", 7, false);
                animations.put("idle_left", animation); 
            case "happy":
                animation = loadAnimationFrames("images/happy_idle_egg_r.png", 8, false);
                animations.put("idle_right", animation);
                animation = loadAnimationFrames("images/happy_idle_egg_l.png", 8, false);
                animations.put("idle_left", animation);
            default:
                throw new IllegalArgumentException("Invalid type of egg: " + eggType);
        }
    }

    public void update(){}

    public void draw(Graphics2D g2){}

    public void move(){}
    
    public boolean isType(String value){
        if(eggType.equals(value))
            return true;
        
            return false;
    }

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

    public boolean collidesWithBullet(Bullet b){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double bulletRect = b.getBoundingRectangle();
      
        return myRect.intersects(bulletRect);
    }

}
