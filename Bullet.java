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
    private int originalX, originalY;
    private int width, height;
    private int dx;
    private Image bulletImg;
    private String bulletType;

    public Bullet(GamePanel p, int x, int y, String type, int dx){
        panel = p;
        this.x = x;
        this.y = y;
        originalX = x;
        originalY = y;

        bulletType = type;
        loadBulletImage(type);
        width = bulletImg.getWidth(null);
        height = bulletImg.getHeight(null);

        this.dx = dx;
    }

    private void loadBulletImage(String type){
        //System.out.println(type.equals("fear"));
        if(type.equals("fear"))
            bulletImg = ImageManager.loadImage("images/fear_bullet.png");
        if(type.equals("love"))
            bulletImg = ImageManager.loadImage("images/love_bullet.png");
        if(type.equals("rage"))
            bulletImg = ImageManager.loadImage("images/rage_bullet.png");
        if(type.equals("sadness"))
            bulletImg = ImageManager.loadImage("images/sadness_bullet.png");
        if(type.equals("happy"))
            bulletImg = ImageManager.loadImage("images/happy_bullet.png");
        
        if(type.equals("egg")){
            bulletImg = ImageManager.loadImage("images/egg_bullet.png");
        }
    }

    public boolean isType(String value){
        if(bulletType.equals(value))
            return true;

        return false;
    }

    public String getType(){
        return bulletType;
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

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double (x, y, width, height);
    }

    public boolean collidesWithBullet(Bullet b){
        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double bulletRect = b.getBoundingRectangle();
      
        return myRect.intersects(bulletRect);
    }
    
    public boolean passedDistance(){
        if(Math.abs(originalX - x) > 200)
            return true;
        return false;
    }

    public void moveDown(String type, int dy){
        if(y < 400 && type.equals("egg"))
            y += dy;
    }

}
