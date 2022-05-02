import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Prisoner{

    private GamePanel panel;
    
    private int x, y;
    private int width, height;
    
    private int dx, dy;
    private int originalX, originalY;

    private Animation animation;
    private boolean unlocked;

    public Prisoner(GamePanel p, int x, int y, int character) {
        panel = p;
 
        animation = new Animation(panel, this.width, this.height);
        width = 48;
        height = 48;

        this.x = x;
        this.y = y;
        originalX = x;
        originalY = y;

        dx = 8;

        loadAnimations(character);
    }

    public void loadAnimations(int character) {
        // Love
        if (character == 1){
            animation = loadAnimationFrames("images/love_idle_l.png", 15, false);
        }

        // Rage
        else if (character == 2){
            animation = loadAnimationFrames("images/rage_idle_l.png", 9, false);
        }

        // Sadness
        else if (character == 3){
            animation = loadAnimationFrames("images/sadness_idle_l.png", 9, false);
        }

        // Happy
        else if (character == 4){
            animation = loadAnimationFrames("images/happy_idle_l.png", 9, false);
        }
        System.out.println("character: " + character);
    }

    public void update() {
        animation.update();
    }

    public void draw(Graphics2D g2) {
        animation.draw(g2, x, y);         
     }

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, width, height);
    }

    
    public boolean collidesWithEgg(Egg e){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double eggRect = e.getBoundingRectangle();

        return myRect.intersects(eggRect);
    }  

    public void move(int direction){
      
        if (direction == 3) {		// move left
            moveRight();
        }	
        else				// move right
        if (direction == 4) {
            if(x > 320)
                moveLeft();
        }
    }

    public void moveLeft(){
        x = x - dx;
    }

    public void moveRight(){
        x = x + dx;
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

    public void reset(){
        x = originalX;
        y = originalY;
    }
}
