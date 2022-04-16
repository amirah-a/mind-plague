import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.System.Logger.Level;
import java.util.LinkedList;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Font;
import java.awt.Color;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel {
	
	private static final int MAX_LEVEL = 6;
	private GameThread gameThread;

	private Emotion[] emotions;  //hold all emotions
	private Emotion currEmotion;

	private LinkedList<Bullet> bullets;
	private Bullet tempB;
	SoundManager soundManager;

	private Background background;
	private BufferedImage image;

	public static int LEVEL = 0;	// there are 6 levels - starting at 0

	private Door door, openDoor, closedDoor;
	private boolean open;
	private Platform platform;

	private int eggsRem;
	private int[] score;

	public GamePanel () {
		emotions = new Emotion[5];
		currEmotion = null;

		bullets = new LinkedList<Bullet>();
		tempB = null;

		image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		open = false;  // door is not opened - level not finished

		eggsRem = 3;
		score = new int[5]; // 5 scores for 5 levels - not level 0
	
	}

	public void switchEmotion(int emotionIndex){
		currEmotion = emotions[emotionIndex];
	}

	private void createGameEntities() {
		emotions[0] = new Fear(this);
		emotions[1] = new Love(this);
		emotions[2] = new Rage(this);
		emotions[3] = new Sadness(this);
		emotions[4] = new Happy(this);
		
		background = new Background(this, "images/Scrolling_BG.png", 8);	
		
		openDoor = new Door(this, 2000, 390, 79, 56, "images/door_open.png");	
		closedDoor = new Door(this, 2000, 390, 51 , 56, "images/door_closed.png");
		door = closedDoor;

		// increase the xPos to place platform further away
		platform = new Platform(this, 1000, 250, 162, 54, "images/platform.png"); 
	}

	public void addBullet(Bullet b){
		bullets.add(b);
	}

	public Bullet createBullet(int x, int y){
		Bullet bullet;
		if(currEmotion instanceof Fear){
			System.out.println("Fear");
			bullet = new Bullet(this, x, y, "fear");
			return bullet;
		}
			
		if(currEmotion instanceof Love){
			System.out.println("Love");
			bullet = new Bullet(this, x, y, "love");
			return bullet;
		}
			
		if(currEmotion instanceof Rage){
			System.out.println("Rage");
			bullet = new Bullet(this, x, y, "rage");
			return bullet;
		}
			
		if(currEmotion instanceof Sadness){
			System.out.println("Sadness");
			bullet = new Bullet(this, x, y, "sadness");
			return bullet;
		}	
		else{
			System.out.println("Happy");
			bullet = new Bullet(this, x, y, "happy");
			return bullet;
		}
	}

	public void removeBullet(Bullet b){
		bullets.remove(b);
	}

	public void startGame() {				// initialise and start the game thread 

		Thread thread;

		if (gameThread == null) {
			//soundManager.playClip ("background", true);
			createGameEntities();
			currEmotion = emotions[0];
			gameThread = new GameThread (this);
			thread = new Thread (gameThread);			
			thread.start();
		}
	}


	public void restartGame() {				// initialise and start a new game thread 

		Thread thread;

		if (gameThread == null || !gameThread.isRunning()) {
			//soundManager.playClip ("background", true);
			createGameEntities();
			gameThread = new GameThread (this);
			thread = new Thread (gameThread);			
			thread.start();
			//animation2.start();
		}
	}


	public void pauseGame() {				// pause the game (don't update game entities)
		gameThread.pauseGame();
	}


	public void endGame() {					// end the game thread
		gameThread.endGame();
		//soundManager.stopClip ("background");
	}

	public void updatePlayer(int direction){
		if(background != null){
			background.move(direction);
			currEmotion.move(direction);
			door.move(direction);
			platform.move(direction);
		}
	}

	public void gameUpdate () {
		currEmotion.update();
		
		if(!open && LEVEL <= 6){ // door is unopened and there are remaining levels

			if(LEVEL == 0){	// introductory level
				// check for collisions and other game play

				//bullet handling
				for(int i=0; i<bullets.size(); i++){
					tempB = bullets.get(i);
					tempB.move();

					if(tempB.getX() > this.getWidth())
						removeBullet(tempB);
				}

				// increment level
			}

			if (LEVEL == 1){
				// check for collisions and other game play
				// adjust score[LEVEL-1] -> collects 5 sets of scores, doesnt include the basic intro level
				// increment level
			}
		}

		else if(open && LEVEL <= 6){	// door is opened and there are remaining levels to play
			clearLevel();
		}


	}


	public void gameRender () {				// draw the game objects 
		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		background.draw(imageContext);
	
		Font font = new Font ("Roboto", Font.BOLD, 15);
		imageContext.setFont (font);
		imageContext.setColor(Color.black);

		if (LEVEL == 0){ // introduction level
			imageContext.drawString("Enemies Left: "+String.valueOf(3-eggsRem), 375, 20);
		}

		//bullet handling
		for(int i=0; i < bullets.size(); i++){
			tempB = bullets.get(i);
			tempB.draw(imageContext);
		}

		imageContext.drawString("Level: "+String.valueOf(LEVEL), 15, 20);
			if(!open){ // checks if door is opened - eg level 0 killed all 3 eggs
				door = closedDoor;
			}
			else{
				door = openDoor;
			}

			door.draw(imageContext);
			platform.draw(imageContext);
			
			if(currEmotion != null){
				currEmotion.draw(imageContext);
			}
		
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, null);
		

		imageContext.dispose();
		g2.dispose();
	}

	public void gameOverScreen(){
	
	}

	public void clearLevel(){		// reset any level variables
		background.resetBackground();
		currEmotion.resetEmotion();
		eggsRem = 0;
		open = false;
	}
}