import java.awt.Graphics2D;

public class Sadness extends Emotion{

    public Sadness(GamePanel p) {
        super(p);
        loadAnimations();
        //currIdleImage = idleImages[0];
        currAnimation = animations.get("idle_right");
        unlocked = false; //this character has to be unlocked
    }

    @Override
    protected void loadAnimations() {
        Animation animation = new Animation(panel, this.width, this.height);
        
        //load idle animations 
        animation = loadAnimationFrames("images/sadness_idle_r.png", 9, false);
        animations.put("idle_right", animation);
        
        animation = loadAnimationFrames("images/sadness_idle_l.png", 9, false);
        animations.put("idle_left", animation);

        animation = loadAnimationFrames("images/sadness_walk_l.png", 7, false);
        animations.put("walk_left", animation);
        
        animation = loadAnimationFrames("images/sadness_walk_r.png", 7, true);
        animations.put("walk_right", animation);
   
    }


    @Override
    public void draw(Graphics2D g2) {
        currAnimation.draw(g2, x, y);
    }
    
}
