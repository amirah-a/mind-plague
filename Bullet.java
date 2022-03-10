import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Bullet {
    private GamePanel panel;

    private int x, y;
    private int width, height;
    private int dx;
    private Image bulletImg;

    public Bullet(GamePanel p, int x, int y){
        panel = p;
        this.x = x;
        this.y = y;

        bulletImg = ImageManager.loadImage("images/bullet.png");
        width = bulletImg.getWidth(null);
        height = bulletImg.getHeight(null);

        dx = 20;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void move(){
        x += dx;
    }

    public void draw(Graphics2D g2){
        g2.drawImage(bulletImg, x, y, width, height, null);
    }

}
