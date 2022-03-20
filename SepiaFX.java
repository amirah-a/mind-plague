import java.util.Random;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;


public class SepiaFX implements ImageFX {

	// private static final int WIDTH = 120;		// width of the image
	// private static final int HEIGHT = 120;		// height of the image
	// private static final int YPOS = 0;		// vertical position of the image

	private GamePanel panel;

	private int x;
	private int y;
	private int width, height;

	private BufferedImage spriteImage;		// image for sprite effect
	private BufferedImage copy;			// copy of image

	Graphics2D g2;

	int time, timeChange;				// to control when the image is grayed
	boolean originalImage, sepiaImage;

	private boolean active;


	public SepiaFX (GamePanel p) {
		panel = p;

		Random random = new Random();
		x = 0;
		y = 0;

		width = panel.getWidth();
		height = panel.getHeight();

		time = 10;				// range is 0 to 10
		timeChange = 1;				// set to 1
		originalImage = true;
		sepiaImage = false;

		active = true;

		spriteImage = ImageManager.loadBufferedImage("images/background.png");
		copy = ImageManager.copyImage(spriteImage);		
							//  make a copy of the original image

	}


	private int toSepia (int pixel) {

  		int alpha, red, green, blue, gray;
		int newPixel;

		alpha = (pixel >> 24) & 255;
		red = (pixel >> 16) & 255;
		green = (pixel >> 8) & 255;
		blue = pixel & 255;

		int newRed = (int) (0.393 * red + 0.769 * green + 0.189 * blue);
		int newGreen = (int) (0.349 * red + 0.686 * green + 0.168 * blue);
		int newBlue = (int) (0.272 * red + 0.534 * green + 0.131 * blue);

		newRed = Math.min(255, newRed);
		newGreen = Math.min(255, newGreen);
		newBlue = Math.min(255, newBlue);

		// Set red, green, and blue channels

		newPixel = newBlue | (newGreen << 8) | (newRed << 16) | (alpha << 24);

		return newPixel;
	}

	public Image imageToSepia(){
		copy = ImageManager.copyImage(spriteImage);	
		//  copy original image

		if (originalImage) {			// draw copy (already in colour) and return
			return copy;
		}
				// change to gray and then draw
		int imWidth = copy.getWidth();
		int imHeight = copy.getHeight();

		int [] pixels = new int[imWidth * imHeight];
		copy.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i=0; i<pixels.length; i++) {
		if (sepiaImage)
		pixels[i] = toSepia(pixels[i]);
		}

		copy.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);	
		return copy;
	}

	public void draw (Graphics2D g2) {

		copy = ImageManager.copyImage(spriteImage);	
							//  copy original image

		if (originalImage) {			// draw copy (already in colour) and return
			g2.drawImage(copy, x, y, width, height, null);
			return;
		}
							// change to gray and then draw
		int imWidth = copy.getWidth();
		int imHeight = copy.getHeight();

    		int [] pixels = new int[imWidth * imHeight];
    		copy.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i=0; i<pixels.length; i++) {
			if (sepiaImage)
				pixels[i] = toSepia(pixels[i]);
		}
  
    		copy.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);	

		g2.drawImage(copy, x, y, width, height, null);

/*
		BufferedImage dest = new BufferedImage (imWidth, imHeight,
							BufferedImage.TYPE_INT_ARGB);

    		Graphics2D g2d = dest.createGraphics();

		// Vertical Flip?

		g2d.drawImage(copy, imWidth, 0, 0,imHeight,
			 	   0, 0, imWidth, imHeight, null);

*/ 		

		// Horizontal Flip?

/*
		g2d.drawImage(copy, 0, imHeight, imWidth, 0,
			 	   0, 0, imWidth, imHeight, null);

		g2.drawImage(dest, x, y, WIDTH, HEIGHT, null);
*/

	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, width, height);
	}


	public void update() {				// modify time and change the effect if necessary
		active = true;
		time = time + timeChange;

		if (time < 30) {			// original image shown for 30 units of time
			originalImage = true;
			sepiaImage = false;
		}
		else
		if (time < 60) {			// gray scale image shown for 40 units of time
			originalImage = false;
			sepiaImage = true;
		}
		else {
			time = 0;		
			active = false;
		}
	}

	public boolean getActive(){
		return active;
	}

}