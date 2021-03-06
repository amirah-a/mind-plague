import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

public class Egg {
    protected GamePanel panel;
    
    protected int x, y;
    private int startX, startY; //used for ai walking motion
    protected int dx, dy, bgDx;
    private int oppDx;
    private int originalX, originalY;
    protected int width, height;

    private boolean jumping, falling;
    private double gravity = 0.0;

    protected HashMap<String, Animation> animations;
    protected Animation currAnimation;

    private Image[] healthBars;
    private int health;

    private long pauseTimeElapsed;
    private long attackTimeElapsed;

    private boolean attackMode;

    private Random random;

    private String eggType; //can be basic, fear, love, etc

    public Egg(GamePanel p, String type, int x, int y, int dx){
        panel = p;

        this.x = x;
        this.y = y;

        originalX = x;
        originalY = y;
        
        startX = x;
        startY = y;
        this.dx = dx;
        oppDx = -dx;
        bgDx = 8;

        attackMode = false;

        animations = new HashMap<String, Animation>();
        width = 50;
        height = 50;

        jumping = false;
        falling = false;

        healthBars = new Image[6];
        loadImages();
        health = 5;


        pauseTimeElapsed =0;
        attackTimeElapsed = 0;

        random = new Random();

        eggType = type;
        loadAnimations();
        currAnimation = animations.get("idle_right");
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

    public void setGravity(double gravity){
        this.gravity = gravity;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setDx(int dx){
        this.dx = dx;
        oppDx = -dx;
    }

    public void setDy(int dy){
        this.dy = dy;
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
        }else if(eggType.equals("sadness")){
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
        if (x > 0 &&  x <= panel.getWidth()-currAnimation.getImage().getWidth(null) )
            walk();
        else{
            dx = oppDx;
            oppDx = -dx;
        }
        y+=dy;
        
        if(y + 100 > panel.getHeight()){
            dy = 0;
            if(falling)
                falling = false;
        }    
        

        if(jumping){
            gravity -= 0.3;
            setDy((int)-gravity);
            if(gravity <= 0.0){
                jumping = false;
                falling = true;
            }
        }
        if(falling){
            gravity += 0.1;
            setDy((int)gravity);    
        }
    }

    public void draw(Graphics2D g2) {
        currAnimation.draw(g2, x, y);
        g2.drawImage(healthBars[health], x, y - healthBars[health].getHeight(null), null);    
    }

    public boolean attack(){
        attackTimeElapsed++;
        if(attackTimeElapsed>50){
            attackTimeElapsed = 0;
            int attackChance = random.nextInt(4);
            if(attackChance == 0){
                return true;
            } 
        }
        return false;
    }

    public void walk(){
        if(Math.abs(startX - x) > 150){
            dx = 0;
            pauseTimeElapsed++;
            if(pauseTimeElapsed > 25){
                startX = x; //new starting point
                pauseTimeElapsed = 0;
                dx = oppDx;
                oppDx= -dx;
            }
        }
        x+=dx;
    }


    public void move(int direction){
      
        if (direction == 3) {		// move left
           moveRight();
        }	
        else				// move right
        if (direction == 4) {
            moveLeft();
        }
    }

    public void moveLeft(){
        x = x - bgDx;
    }

    public void moveRight(){
        x = x + bgDx;
    }    
    
    public boolean isType(String value){
        if(eggType.equals(value))
            return true;
        
            return false;
    }

    public void decreaseHealth(){
        health--;
    }

    public int getHealth(){
        return health;
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

    public boolean withinAttackRange(Emotion e){
        Rectangle2D.Double areaRect = new Rectangle2D.Double(x-100, y-100, 250, 250);
        Rectangle2D.Double emotionRect = e.getBoundingRectangle();

        if(areaRect.intersects(emotionRect))
            return true;
        return false;
    }

    public boolean isOnTopPlatform(Platform p){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double platRect = p.getBoundingRectangle();

        //if objects are colliding and emotion is above the platform
        if(myRect.intersects(platRect) && y + height > p.getY()  && y + height < p.getY() + 15){
            return true;
        }
        return false;
    }

    public boolean hitBottom(Platform p){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double platRect = p.getBoundingRectangle();

        if(myRect.intersects(platRect) && y < p.getY() + 54){
            return true;
        }

        return false;
    }


    public void reset(){
        x = originalX;
        y = originalY;
    }

}
