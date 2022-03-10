import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel {
   
	// private static int NUM_ALIENS = 3;

	private Player player;
	// SoundManager soundManager;

	private GameThread gameThread;

	private BufferedImage image;
   	private Image backgroundImage;


	public GamePanel () {
		player = null;
      	// 	soundManager = SoundManager.getInstance();

      	backgroundImage = ImageManager.loadImage ("images/background.png");
		image = new BufferedImage (650, 550, BufferedImage.TYPE_INT_RGB);

	}


	private void createGameEntities() {
		

		player = new Player(this); 
	}



	public void updatePlayer (int direction) {

		if (player != null) {
			player.move(direction);
            //player.updateAnimation();
		}

	}


	public boolean isOnBat (int x, int y) {
		//return bat.isOnBat(x, y);
        return false;
	}


	public void startGame() {				// initialise and start the game thread 

		Thread thread;

		if (gameThread == null) {
			//soundManager.playClip ("background", true);
			createGameEntities();
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


	public void gameUpdate () {
        player.updateAnimation();
	}


	public void gameRender () {				// draw the game objects 

		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		imageContext.drawImage(backgroundImage, 0, 0, null);	// draw the background image		
		
		player.draw(imageContext);

		Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for the panel
		g2.drawImage(image, 0, 0, 650, 550, null);

		imageContext.dispose();
		g2.dispose();

	}

}