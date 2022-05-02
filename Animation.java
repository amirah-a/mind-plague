import java.awt.Image;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Animation {

    private GamePanel panel;					// JPanel on which animation is being displayed
    private ArrayList<AnimFrame> frames;			// collection of frames for animation
    private int currFrameIndex;					// current frame being displayed
    private long animTime;					// time that the animation has run for already
    private long startTime;					// start time of the animation or time since last update
    private long totalDuration;					// total duration of the animation

   private int x;
   private int y;

   private int width;
   private int height;

   private boolean playOnce;
   private boolean active;
			

   private int dx;		// increment to move along x-axis
   private int dy;		// increment to move along y-axis


    /**
        Creates a new, empty Animation.
    */
    public Animation(GamePanel p, int width, int height) {
	    panel = p;
        frames = new ArrayList<AnimFrame>();
        totalDuration = 0;
        this.width = width;
        this.height = height;
        this.playOnce = false;  //will loop by default
        active = true;
        // start();

	    // x = 5;
        // y = 10;
        dx = 0;
        dy = 0;
    }

    public void setXY(int x, int y){
        this.x = x;
        this.y =y;
    }

    public void setPlayOnce(boolean value){
        playOnce = value;
    }
    
    /**
        Adds an image to the animation with the specified
        duration (time to display the image).
    */
    public synchronized void addFrame(Image image, long duration)
    {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }


    /**
        Starts this animation over from the beginning.
    */
    public synchronized void start() {
	    // x = 5;
	    // y = 10;
        animTime = 0;						// reset time animation has run for to zero
        currFrameIndex = 0;					// reset current frame to first frame
	    startTime = System.currentTimeMillis();			// reset start time to current time
        active = true;
    }


    /**
        Updates this animation's current image (frame), if
        neccesary.
    */
    public synchronized void update() {
        long currTime = System.currentTimeMillis();		// find the current time
	    long elapsedTime = currTime - startTime;		// find how much time has elapsed since last update
	    startTime = currTime;					// set start time to current time

        if (frames.size() > 1) {// add elapsed time to amount of time animation has run for
            animTime += elapsedTime;

            if (animTime >= totalDuration) {			// if the time animation has run for > total duration
                if(playOnce){
                    active = false;
                    return;
                }                     
                else{
                    animTime = animTime % totalDuration;		//    reset time animation has run for
                    currFrameIndex = 0;				//    reset current frame to first frame  
                }
                
            }

            while (animTime > getFrame(currFrameIndex).endTime) {
                currFrameIndex++;				// set frame corresponding to time animation has run for
            }
        }
        // x += dx;
        // y += dy;
    }

    public boolean isActive(){
        return active;
    }
    /**
        Gets this Animation's current image. Returns null if this
        animation has no images.
    */
    public synchronized Image getImage() {
        if (frames.size() == 0) {
            return null;
        }
        else {
            return getFrame(currFrameIndex).image;
        }
    }

    public void draw (Graphics2D g2, int x, int y) {				// draw the current frame on the graphics context
 
       //Graphics g = panel.getGraphics ();
       //Graphics2D g2 = (Graphics2D) g;

        if(active)
            g2.drawImage(getImage(), x, y, width, height, null);
  
       //g2.dispose();
    }

    public void draw(Graphics2D g2){
        if(active)
            g2.drawImage(getImage(), x, y, width, height, null);
    }

    public int getNumFrames() {					// find out how many frames in animation
	return frames.size();
    }

    private AnimFrame getFrame(int i) {				// returns ith frame in the collection
        return frames.get(i);
    }


    private class AnimFrame {					// inner class for the frames of the animation

        Image image;
        long endTime;

        public AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }
}
