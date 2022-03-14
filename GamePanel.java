import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;
import java.awt.Graphics2D;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel {
   
	private static int MAX_ENEMIES = 5; //max number of enemies allowed on screen;

	private Player player;
	private LinkedList<Bullet> bullets;
	private LinkedList<Enemy> enemies;
	private Bullet tempB;
	private Enemy tempE;
    //private BulletHandler playerBulletHandler;
	// SoundManager soundManager;

	private GameThread gameThread;

	private BufferedImage image;
   	private Image backgroundImage;

	private Random random;


	public GamePanel () {
		player = null;
		bullets = new LinkedList<Bullet>();
		enemies = new LinkedList<Enemy>();
		tempB = null;
		tempE = null;

		random = new Random();
        
      	// 	soundManager = SoundManager.getInstance();

      	backgroundImage = ImageManager.loadImage ("images/background.png");
		image = new BufferedImage (650, 550, BufferedImage.TYPE_INT_RGB);
	}


	private void createGameEntities() {
		player = new Player(this);
		for(int i=0; i<MAX_ENEMIES; i++){
			enemies.add(new Enemy(this, getPlayer().getPWidth() + 10, getPlayer().getPHeight(), random.nextInt(2)+1));
			//no enemy overlap
			for(int k=0; k<enemies.size(); k++){
				tempE = enemies.get(i);
				if(tempE.collidesWithEnemy(enemies.get(k)) && enemies.size() > 1){
					tempE.setXY(tempE.getX() + 35, tempE.getY());
				}
			}
		} 
	}

    public Player getPlayer(){
        return player;
    }

	public void addEnemy(Enemy e){
		enemies.add(e);
	}

    public void addBullet(Bullet b){
		bullets.add(b);
	}

	public void removeEnemy(Enemy e){
		enemies.remove(e);
	}

	public void removeBullet(Bullet b){
		bullets.remove(b);
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

		for(int i=0; i<bullets.size(); i++){
			tempB = bullets.get(i);
			tempB.move();

			if(tempB.getX() > this.getWidth() || tempB.getX() < 0)
                removeBullet(tempB);
		}

		if(enemies.size() < MAX_ENEMIES){
			enemies.add(new Enemy(this, getPlayer().getPWidth() + 10, getPlayer().getPHeight(), random.nextInt(2)+1));
		}

		for(int i=0; i<enemies.size(); i++){
			tempE = enemies.get(i);
			tempE.move();

			if(player.collidesWithEnemy(tempE)){
				removeEnemy(tempE);
				tempE = enemies.get((i+1)%enemies.size());
			}
			//check if enemy got shot by any bullets on screen
			for(int j=0; j<bullets.size(); j++){
				tempB = bullets.get(j);
				if(tempE.collidesWithBullet(tempB)){
					removeEnemy(tempE);
					removeBullet(tempB);
				}	
			}

			
		}

	}


	public void gameRender () {				// draw the game objects 

		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		imageContext.drawImage(backgroundImage, 0, 0, null);	// draw the background image		
		
		player.draw(imageContext);

		for(int i=0; i<bullets.size(); i++){
			tempB = bullets.get(i);
			tempB.draw(imageContext);
		}
        
		for(int i =0; i<enemies.size(); i++){
			tempE = enemies.get(i);
			tempE.draw(imageContext);
		}

		Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for the panel
		g2.drawImage(image, 0, 0, 650, 550, null);

		imageContext.dispose();
		g2.dispose();

	}

}