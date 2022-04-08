import java.awt.Graphics;

public class Fear extends Emotion {

    public Fear(GamePanel p) {
        super(p);
        loadImages();
        currIdleImage = idleImages[0];
        unlocked = true; //this character is unlocked by default
    }

    @Override
    protected void loadImages() {
       idleImages[0] = ImageManager.loadImage("images/fear_r.png");
       idleImages[1] = ImageManager.loadImage("images/fear_l.png");    
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }

    
    public void draw(Graphics g2) {
       g2.drawImage(currIdleImage, x, y, null);    
    }    
}
