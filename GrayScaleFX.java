import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GrayScaleFX implements ImageFX {

	private static final int WIDTH = 800;		// width of the image
	private static final int HEIGHT = 400;		// height of the image
	// private static final int YPOS = 250;		// vertical position of the image

	private GamePanel panel;

	private int x;
	private int y;
	private boolean active;
	private BufferedImage spriteImage;		// image for sprite effect
	private BufferedImage copy;			// copy of image

	Graphics2D g2;

	int time, timeChange;				// to control when the image is grayed
	boolean originalImage, grayImage;


	public GrayScaleFX (GamePanel p) {
		panel = p;
		x = 0;
		y = 0;
		time = 0;				// range is 0 to 10
		timeChange = 10;				// set to 1
		originalImage = true;
		grayImage = false;
		
		active = true;

		spriteImage = ImageManager.loadBufferedImage("images/BG.png");
		copy = ImageManager.copyImage(spriteImage);		
							//  make a copy of the original image

	}

	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}


	private int toGray (int pixel) {

  		int alpha, red, green, blue, gray;
		int newPixel;

		alpha = (pixel >> 24) & 255;
		red = (pixel >> 16) & 255;
		green = (pixel >> 8) & 255;
		blue = pixel & 255;

		// Calculate the value for gray

		gray = (red + green + blue) / 3;

		// Set red, green, and blue channels to gray

		red = green = blue = gray;

		newPixel = blue | (green << 8) | (red << 16) | (alpha << 24);

		return newPixel;
	}


	public void draw (Graphics2D g2) {

		copy = ImageManager.copyImage(spriteImage);	
							//  copy original image

		if (originalImage) {			// draw copy (already in colour) and return
			g2.drawImage(copy, x, y, WIDTH, HEIGHT, null);
			return;
		}
							// change to gray and then draw
		int imWidth = copy.getWidth();
		int imHeight = copy.getHeight();

    		int [] pixels = new int[imWidth * imHeight];
    		copy.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i=0; i<pixels.length; i++) {
			if (grayImage)
				pixels[i] = toGray(pixels[i]);
		}
  
    		copy.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);	

		g2.drawImage(copy, x, y, WIDTH, HEIGHT, null);

	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, WIDTH, HEIGHT);
	}


	public void update() {				// modify time and change the effect if necessary
		active = true;
		time = time + timeChange;

		if (time < 20) {			// original image shown for 20 units of time
			originalImage = true;
			grayImage = false;
		}
		else
		if (time < 40) {			// gray scale image shown for 20 units of time
			originalImage = false;
			grayImage = true;
		}
		else {		
			time = 0;
			active = false;
		}
	}

	public boolean isActive(){
		return active;
	}

}