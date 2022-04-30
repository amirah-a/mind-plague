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
    public static int dx, dy, prevDx;
    protected int orginalX, orginalY;

    protected boolean jumping = false, falling = false;

    protected double gravity = 0.0;

    protected Image currIdleImage;
    protected Image[] idleImages;

    //protected Animation[] animations;
    protected Animation currAnimation;

    protected HashMap<String, Animation> animations;

    protected boolean unlocked;
    protected boolean hasKey;

    protected static int health;

    public Emotion(GamePanel p){
        panel = p;
        
        //idleImages = new Image[2];
        animations = new HashMap<String, Animation>();
        width = 48;
        height = 48;
        hasKey = false;

        // used to reset the emotion's position at the start of a new level
        orginalX = x;
        orginalY = y;
        //dx = 2;

        health = 5;
    }

    protected abstract void loadAnimations();
    public abstract void draw(Graphics2D g2);

    public boolean HasKey(){
        return hasKey;
    }

    public void setHasKey(boolean value){
        hasKey = value;
    }

    public void setGravity(double gravity){
        this.gravity = gravity;
    }

    public boolean isJumping(){
        return jumping;
    }

    public boolean isFalling(){
        return falling;
    }

    public void setJumping(boolean value){
        jumping = value;
    }

    public void setFalling(boolean value){
        falling = value;
    }

    public void setDx(int dx){
        prevDx = this.dx;
        this.dx = dx;
    }

    public void setDy(int dy){
        this.dy = dy;
    }

    public int getDx(){
        return dx;
    }

    public int getprevDx(){
        return prevDx;
    }

    public int getHealth(){
        return health;
    }
    public void decreaseHealth(){
        health-=1;
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

    public void update(){
        currAnimation.update();

        if(x < 0){
            setDx(0);
            x = 0;
        }else if(x>panel.getWidth() -60){
            setDx(0);
            x = panel.getWidth() -60;
        }
            
        //movement
        x+=dx;  
        y+=dy;

        
        
        if(y + 100 > panel.getHeight()){
            dy = 0;
            if(falling)
                falling = false;
            //gravity = 0.0;
        }    
        

        if(jumping){
            gravity -= 0.22;
            setDy((int)-gravity);
            if(gravity <= 0.0){
                jumping = false;
                falling = true;
            }
        }
        if(falling){
            gravity += 0.3;
            setDy((int)gravity);    
        }
    }

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, width, height);
    }

    public boolean collidesWithEgg(Egg e){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double eggRect = e.getBoundingRectangle();

        return myRect.intersects(eggRect);
    }

    public boolean collidesWithKey(Key k){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double keyRect = k.getBoundingRectangle();

        return myRect.intersects(keyRect);
    }

    public boolean collidesWithJail(Jail jail) {
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double jailRect = jail.getBoundingRectangle();

        return myRect.intersects(jailRect);
    }

    public boolean collidesWithDoor(Door d){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double doorRect = d.getBoundingRectangle();

        return myRect.intersects(doorRect);
    }
    public boolean isOnTopPlatform(Platform p){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double platRect = p.getBoundingRectangle();

        //if objects are colliding and emotion is above the platform
        if(myRect.intersects(platRect) && y + height > p.getY()  && y + height < p.getY() + 15){
            //System.out.println("on platform");
            return true;
        }
        //System.out.println("here");
        return false;
    }

    public boolean collidesWithBullet(Bullet bullet) {
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double bulletRect = bullet.getBoundingRectangle();

        return myRect.intersects(bulletRect);
    }

    public boolean hitBottom(Platform p){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double platRect = p.getBoundingRectangle();

        if(myRect.intersects(platRect) && y < p.getY() + 50 && y > p.getY()){
            return true;
        }

        return false;
    }

    // for levels
    public void setUnlocked(boolean value){
        unlocked = value;
    }

    public boolean isUnlocked(){
        return unlocked;
    }

    public void resetEmotion(){
        x = orginalX;
        y = orginalY;
        health = 5;
    }
    public void setAnimation(int direction) {

        if (direction == -3){
            currAnimation = animations.get("idle_left");
        }

        if (direction == -4){
            currAnimation = animations.get("idle_right");
        }

        if (direction == 3){
            currAnimation = animations.get("walk_left");
        }

        if (direction == 4){
            currAnimation = animations.get("walk_right");
        }
    }

    public int getX() {
        return x;
    }  

}
