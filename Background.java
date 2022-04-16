import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Background {
    private Image bgImage;
    private int bgImageWidth;

    private GamePanel panel;

    private int bgX;
    private int bg1X;
    private int bg2X;
    private int bgDX;

    private int orginalBgX;

    public Background(GamePanel panel, String imageFile, int bgDX) {
    
        this.panel = panel;
        this.bgImage = ImageManager.loadImage(imageFile);
        bgImageWidth = bgImage.getWidth(null)-bgDX;	// get width of the background
    
        //System.out.println ("bgImageWidth = " + bgImageWidth);
    
        if (bgImageWidth < panel.getWidth())
          System.out.println("Background width < panel width");
    
        this.bgDX = bgDX;
        orginalBgX = bgDX; // this is used to reset the background to the start;
    
      }
    
    
      public void move (int direction) {
    
        if (direction == 3){
            if(bgX < 0)
                moveRight();
        }else if (direction == 4){
            if(bgX > 500 - bgImageWidth )
                moveLeft();
        }
            
      }
    
    
      public void moveLeft() {
    
        bgX = bgX - bgDX;
    
        // bg1X = bg1X - bgDX;
        // bg2X = bg2X - bgDX;
    
        String mess = "Moving background left: bgX=" + bgX + " bg1X=" + bg1X + " bg2X=" + bg2X;
        System.out.println (mess);
    
        // if (bg1X < (bgImageWidth * -1)) {
        //     System.out.println ("Background change: bgX = " + bgX); 
        //     bg1X = 0;
        //     bg2X = bgImageWidth;
        // }
    
      }
    
    
      public void moveRight() {
    
        bgX = bgX + bgDX;
                    
        // bg1X = bg1X + bgDX;
        // bg2X = bg2X + bgDX;
    
        String mess = "Moving background right: bgX=" + bgX + " bg1X=" + bg1X + " bg2X=" + bg2X;
        System.out.println (mess);
    
        // if (bg1X > 0) {
        //     System.out.println ("Background change: bgX = " + bgX); 
        //     bg1X = bgImageWidth * -1;
        //     bg2X = 0;
        // }
    
       }
     
    
      public void draw (Graphics2D g2) {
        g2.drawImage(bgImage, bgX, 0, null);
        g2.drawImage(bgImage, bgX, 0, null);
      }

      public int getBGX(){
        return bgX;
      }

      public void resetBackground(){  // restarts the background at the beginning of the new level
        bgX = orginalBgX;
      }
}
