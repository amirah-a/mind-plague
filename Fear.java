import java.awt.Graphics2D;

public class Fear extends Emotion {

    public Fear(GamePanel p) {
        super(p);
        loadAnimations();
        currAnimation = animations.get("idle_right");
        unlocked = true; //this character is unlocked by default
    }

    @Override
    protected void loadAnimations() {
        Animation animation = new Animation(panel, this.width, this.height);
        
        //load idle animations 
        animation = loadAnimationFrames("images/fear_idle_r.png", 8, false);
        animations.put("idle_right", animation);
        animation = loadAnimationFrames("images/fear_idle_l.png", 8, false);
        animations.put("idle_left", animation);

        animation = loadAnimationFrames("images/fear_walk_l.png", 7, false);
        animations.put("walk_left", animation);
        animation = loadAnimationFrames("images/fear_walk_r.png", 7, true);
        animations.put("walk_right", animation);
   
    }

    
    public void draw(Graphics2D g2) {
       currAnimation.draw(g2, x, y);    
    }    
}
