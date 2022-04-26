import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Egg {
    protected GamePanel panel;
    
    protected int x, y;
    private int startX, startY; //used for ai walking motion
    protected int dx, dy;
    protected int width, height;

    protected HashMap<String, Animation> animations;
    protected Animation currAnimation;

    private boolean attackMode;

    private String eggType; //can be basic, fear, love, etc

    public Egg(GamePanel p, String type, int x, int y, int dx){
        panel = p;

        this.x = x;
        this.y = y;
        startX = x;
        startY = y;
        this.dx = dx;

        attackMode = false;

        animations = new HashMap<String, Animation>();
        width = 50;
        height = 50;

        eggType = type;
        loadAnimations();
        currAnimation = animations.get("idle_right");
    }

    public boolean getAttackMode(){
        return attackMode;
    }

    public void setAttackMode(boolean value){
        attackMode = value;
    }

    private void loadAnimations(){
        Animation animation = new Animation(panel, this.width, this.height);
        if(eggType.equals("basic")){
            animation = loadAnimationFrames("images/basic_idle_egg_r.png", 8, false);
            animations.put("idle_right", animation);
            animation = loadAnimationFrames("images/basic_idle_egg_l.png", 8, false);
            animations.put("idle_left", animation);
        }else if(eggType.equals("fear")){
            animation = loadAnimationFrames("images/fear_idle_egg_r.png", 7, false);
            animations.put("idle_right", animation);
            animation = loadAnimationFrames("images/fear_idle_egg_l.png", 7, false);
            animations.put("idle_left", animation);
        }else if(eggType.equals("love")){
            animation = loadAnimationFrames("images/love_idle_egg_r.png", 9, false);
            animations.put("idle_right", animation);
            animation = loadAnimationFrames("images/love_idle_egg_l.png", 9, false);
            animations.put("idle_left", animation);
        }else if(eggType.equals("rage")){
            animation = loadAnimationFrames("images/rage_idle_egg_r.png", 9, false);
            animations.put("idle_right", animation);
            animation = loadAnimationFrames("images/rage_idle_egg_l.png", 9, false);
            animations.put("idle_left", animation);
        }else if(eggType.equals("sad")){
            animation = loadAnimationFrames("images/sadness_idle_egg_r.png", 7, false);
            animations.put("idle_right", animation);
            animation = loadAnimationFrames("images/sadness_idle_egg_l.png", 7, false);
            animations.put("idle_left", animation);
        }else{
            animation = loadAnimationFrames("images/happy_idle_egg_r.png", 8, false);
            animations.put("idle_right", animation);
            animation = loadAnimationFrames("images/happy_idle_egg_l.png", 8, false);
            animations.put("idle_left", animation);
        }
                
    }
 

    public void update(){
        currAnimation.update();
        walk();
    }

    public void draw(Graphics2D g2) {
        currAnimation.draw(g2, x, y);
        //g2.drawRect(x-100, y-100, 250, 250);    
    }

    public void walk(){
        if(Math.abs(startX - x) > 150){
            startX = x; //new starting point
            dx = -dx; // opposite direction
        }

        x+=dx;
    }

    
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

    public boolean withinAttackRange(Emotion e){
        Rectangle2D.Double areaRect = new Rectangle2D.Double(x-100, y-100, 250, 250);
        Rectangle2D.Double emotionRect = e.getBoundingRectangle();

        if(areaRect.intersects(emotionRect))
            return true;
        return false;
    }

}
