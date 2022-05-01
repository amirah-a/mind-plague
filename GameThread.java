import javax.swing.JPanel;

/**
   The thread that manages each game.
*/

public class GameThread implements Runnable {

	private GamePanel gamePanel;
	private boolean isRunning;
	private boolean isPaused;
	private boolean outcome;
	SoundManager soundManager;

	public GameThread (GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		isRunning = false;
		isPaused = false;
		outcome = false;
		soundManager = SoundManager.getInstance();
	}

	
	boolean isRunning () {
		return isRunning;
	}

	public void setIsRunning(boolean isRunning){
		this.isRunning = isRunning;
	}

	public void setState(boolean state){
		outcome = state;
	}


	public void pauseGame() {

		if (isRunning) {
			if (isPaused)
				isPaused = false;
			else
				isPaused = true;
		}
	}


	public void endGame() {
		isRunning = false;
	}


	private void gameUpdate() {
		gamePanel.gameUpdate();
	}


	private void gameRender() {
		gamePanel.gameRender();
	}


	public void run () {
		try {
			isRunning = true;
			while (isRunning) {
				if (!isPaused)
					gameUpdate();
				gameRender();
				Thread.sleep (50);	
			}
			if(!isRunning){
				soundManager.stopClip("background");
				gamePanel.gameOverScreen(outcome);
			}
		}
		catch(InterruptedException e) {}
	}

}