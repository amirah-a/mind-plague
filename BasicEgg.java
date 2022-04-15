import java.awt.Graphics2D;

public class BasicEgg extends Egg {

    public BasicEgg(GamePanel p) {
        super(p);
        loadAnimations();
        currAnimation = animations.get("idle_left");  //i think idle should be randomly chosen

    }

    @Override
    protected void loadAnimations() {
        Animation animation = new Animation(panel, this.width, this.height);
        
        //load idle animations 
        animation = loadAnimationFrames("images/basic_idle_egg_r.png", 8, false);
        animations.put("idle_right", animation);
        animation = loadAnimationFrames("images/basic_idle_egg_l.png", 8, false);
        animations.put("idle_left", animation);
        
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw(Graphics2D g2) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void move() {
        // TODO Auto-generated method stub
        
    }
    
}
