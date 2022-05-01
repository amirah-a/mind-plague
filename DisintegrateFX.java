import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class DisintegrateFX implements ImageFX {

	private static final int WIDTH = 204;		// width of the image
	private static final int HEIGHT = 120;		// height of the image

	private GamePanel panel;

	private int x;
	private int y;

	private BufferedImage spriteImage;		// image for sprite effect
	private BufferedImage copy;			// copy of image
	private boolean active;	
    
	Graphics2D g2;

	int time, timeChange;				// to control when the image is grayed


	public DisintegrateFX (GamePanel p) {
		panel = p;

		time = 0;				// range is 0 to 70
		timeChange = 10;				// how to increment time in game loop

		spriteImage = ImageManager.loadBufferedImage("images/pelican_idle.png");
		copy = ImageManager.copyImage(spriteImage);	//  make a copy of the original image
		active = true;
	}

	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}

  	public void eraseImageParts(BufferedImage im, int interval) {

    		int imWidth = im.getWidth();
    		int imHeight = im.getHeight();

    		int [] pixels = new int[imWidth * imHeight];
    		im.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i = 0; i < pixels.length; i = i + interval) {
      		pixels[i] = 0;    // make transparent (or black if no alpha)
		}
  
    		im.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
  	}


	public void draw (Graphics2D g2) {

		if (time == 10)
			eraseImageParts(copy, 11);
		else
		if (time == 20)
			eraseImageParts(copy, 7);
		else
		if (time == 30)
			eraseImageParts(copy, 5);
		else
		if (time == 40)
			eraseImageParts(copy, 3);
		else
		if (time == 50)
			eraseImageParts(copy, 2);
		else
		if (time == 60)
			eraseImageParts(copy, 1);
		else
		if (time == 70)
			copy = ImageManager.copyImage(spriteImage);

		g2.drawImage(copy, x, y, WIDTH, HEIGHT, null);
		System.out.println("x, y" + Integer.toString(x) + ", " + Integer.toString(y));
	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, WIDTH, HEIGHT);
	}


	public void update() {				// modify time
		active = true;
		time = time + timeChange;

		if (time > 70){
			time = 0;
			active = false;
		}			
			
	}

	public boolean isActive(){
		return active;
	}

}