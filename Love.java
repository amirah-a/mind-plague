import java.awt.Graphics2D;

public class Love extends Emotion{

    public Love(GamePanel p) {
        super(p);
        loadAnimations();
        //currIdleImage = idleImages[0];
        currAnimation = animations.get("idle_right");
        unlocked = false; //this character needs to be unlocked
    }

    @Override
    protected void loadAnimations() {
        Animation animation = new Animation(panel, this.width, this.height);
        
        //load idle animations 
        animation = loadAnimationFrames("images/love_idle_r.png", 15, false);
        animations.put("idle_right", animation);
        animation = loadAnimationFrames("images/love_idle_l.png", 15, false);
        animations.put("idle_left", animation);
    }


    @Override
    public void draw(Graphics2D g2) {
       currAnimation.draw(g2, x, y);
        
    }
    
    
}
