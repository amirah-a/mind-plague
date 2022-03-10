import java.util.LinkedList;
import java.awt.Graphics2D;

public class BulletHandler {
    private LinkedList<Bullet> bullets;
    private GamePanel panel;

    private Bullet tempB;

    public BulletHandler(GamePanel p){
        bullets = new LinkedList<Bullet>();
        panel = p;
    }

    public void update(){
        for(int i = 0; i < bullets.size(); i++){
            tempB = bullets.get(i);
            tempB.move();

            if(tempB.getX() > panel.getWidth() || tempB.getX() < 0)
                removeBullet(tempB);
        }
    }

    public void draw(Graphics2D g2){
        for(int i = 0; i < bullets.size(); i++){
            tempB = bullets.get(i);

            tempB.draw(g2);
        }
    }

    public void addBullet(Bullet b){
        bullets.add(b);
    }

    public void removeBullet(Bullet b){
        bullets.remove(b);
    }
}
