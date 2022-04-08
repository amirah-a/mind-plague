import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel {
   
	private GameThread gameThread;

	private Emotion[] emotions;  //hold all emotions
	private Emotion currEmotion;

	SoundManager soundManager;

	private Background background;

	private BufferedImage image;

	public GamePanel () {
		emotions = new Emotion[5];
		currEmotion = null;

		image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
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
		}
	}

	public void gameUpdate () {

       
	}


	public void gameRender () {				// draw the game objects 
		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		background.draw(imageContext);

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
}