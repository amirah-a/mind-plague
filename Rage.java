import java.awt.Graphics;

public class Rage extends Emotion{

    public Rage(GamePanel p) {
        super(p);
        loadImages();
        currIdleImage = idleImages[0];
        unlocked = false; //this character has to be unlocked
    }

    @Override
    protected void loadImages() {
        idleImages[0] = ImageManager.loadImage("images/rage_r.png");
        idleImages[1] = ImageManager.loadImage("images/rage_l.png");
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw(Graphics g2) {
        g2.drawImage(currIdleImage, x, y, null);
        
    }
    
}
