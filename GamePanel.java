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

	//eggs
	private LinkedList<Egg> eggEnemies;
	private Egg tempE;

	private LinkedList<Bullet> bullets;
	private Bullet tempB;
	SoundManager soundManager;

	private Background background;
	private BufferedImage image;
	private int emotionIndex;

	public static int LEVEL = 1;	// there are 6 levels - starting at 0

	private Door door, openDoor, closedDoor;
	private boolean open;
	private Platform[] platforms;
	private Portal portal;
	private Key key;
	private boolean droppedKey, pickedUpKey;

	private Random random;
	private int keyChance;

	private int eggsRem;
	private int[] score;
	private int ON_SCREEN_EGGS = 5; // we only want 5 eggs on the screen at a time
	
	private Jail jail;
    private Prisoner prisoner;

	private long spawnTimeElapsed;

	public GamePanel () {
		emotions = new Emotion[5];
		currEmotion = null;

		eggEnemies = new LinkedList<Egg>();
		tempE = null;

		bullets = new LinkedList<Bullet>();
		tempB = null;

		image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		open = false;  // door is not opened - level not finished

		eggsRem = 3;
		score = new int[5]; // 5 scores for 5 levels - not level 0
		platforms = new Platform[5];
		key = null;

		jail = null;
		prisoner = null;
		emotionIndex = 0; // fear
		
		random = new Random();
		keyChance = 0;
		droppedKey = false;
		pickedUpKey = false;

		spawnTimeElapsed = 0;
	}

	public void switchEmotion(int emotionIndex){
		currEmotion = emotions[emotionIndex];
	}

	public Emotion getCurrEmotion(){
		return currEmotion;
	}

	private void createGameEntities() {
		emotions[0] = new Fear(this);
		emotions[1] = new Love(this);
		emotions[2] = new Rage(this);
		emotions[3] = new Sadness(this);
		emotions[4] = new Happy(this);
		
		background = new Background(this, "images/Scrolling_BG.png", 8);	
		
		openDoor = new Door(this, 2200, 390, 79, 56, "images/door_open.png");	
		closedDoor = new Door(this, 2200, 390, 51 , 56, "images/door_closed.png");
		door = closedDoor;

		// increase the xPos to place platform further away
		platforms[0] = new Platform(this, 600, 300, 93, 54, "images/short_platform.png"); 
		platforms[1] = new Platform(this, 175, 250, 162, 54, "images/platform.png");
		platforms[2] = new Platform(this, 1250, 350, 114, 54, "images/medium_platform.png"); 
		platforms[3] = new Platform(this, 1350, 250, 162, 54, "images/platform.png");
		platforms[4] = new Platform(this, 1900, 250, 166, 54, "images/long_platform.png"); 

		portal = new Portal(this, 1995, 125, 89, 150, "images/portal.png");

		if (LEVEL > 0){
			jail = new Jail(this, 2100, 385);
			prisoner = new Prisoner(this, 2110, 400, LEVEL);
		}

		addEgg(createEgg()); // start with one enemy
			
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

	public void addEgg(Egg e){
		eggEnemies.add(e);
	}

	public void removeEgg(Egg e){
		eggEnemies.remove(e);
	}

	public Egg createEgg(){
		Egg e;
		String type;
		int dx;
		int typeInt = random.nextInt(LEVEL + 1);
		switch(typeInt -1){
			case 0:
				type = "basic";
				break;
			case 1:
				type = "fear";
				break;
			case 2:
				type = "love";
				break;
			case 3:
				type = "rage";
				break;
			case 4:
				type = "sadness";
				break;
			case 5: 
				type = "happy";
				break;
			default:
				type = "basic";
		}
		typeInt = random.nextInt(2) + 1;
		switch(typeInt -1){
			case 0: 
				dx = -2;
				break;
			case 1:
				dx = 2;
				break;
			default:
				dx = -2;
		}

		e = new Egg(this, type, 150, 400, dx);
		return e;
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

	public void updateObjects(int direction){
		if(background != null){
			background.move(direction);
			//currEmotion.move(direction);
			door.move(direction);
			
			if (LEVEL > 0){
				jail.move(direction);
				prisoner.move(direction);
			}
			
			if (background.getBGX()*-1 < 1832 && background.getBGX() != 0){ // check to stop platfrom from going beyond bg
				for(int i=0; i<5; i++)
					platforms[i].move(direction);
				
				portal.move(direction);
				
				for(int k=0; k < eggEnemies.size(); k++){
					tempE = eggEnemies.get(k);
					tempE.move(direction);
				}
			}			
		}
	}

	public void gameUpdate () {
		spawnTimeElapsed++;

		currEmotion.update();
		if(prisoner != null)
			prisoner.update();
		
		if(key!=null){
			if(currEmotion.collidesWithKey(key)){
				currEmotion.setHasKey(true);
				pickedUpKey = true;
			}
		}

		// insert check here to see if player has collected a key
		// I left the boundaries in so that when the player moves to the end of the screen
		// they can see the jail move	
		if (emotions[LEVEL].isUnlocked() && background.getBGX()*-1 >= 1645){ // jail only moves if player has collected the key and is within sight of the jail
			if(jail != null)
				jail.moveUp();
		}

		// TODO: in this method check if level is unlocked 
		// if it is unlocked, set value of 'open' to true in here to change door image

		// TODO: check if level is unlocked to unlock new emotion:
		emotions[LEVEL].setUnlocked(true); 

		//bullet handling
		for(int i=0; i<bullets.size(); i++){
			tempB = bullets.get(i);
			tempB.move();

			if(tempB.getX() > this.getWidth())
				removeBullet(tempB);
		}

		//platform collisions
		for(int j=0; j<platforms.length; j++){
			if(currEmotion.isOnTopPlatform(platforms[j])){
				//System.out.println("Is on platform");
				if(currEmotion.isFalling()){
					currEmotion.setDy(0);
					currEmotion.setFalling(false);
				}
			}else{
				if(!currEmotion.isFalling() && !currEmotion.isJumping()){
					currEmotion.setGravity(0.0);
					currEmotion.setFalling(true);
				}
			}

			if(currEmotion.hitBottom(platforms[j])){
				//currEmotion.setDy(0);
				if(currEmotion.isJumping()){
					currEmotion.setJumping(false);
					currEmotion.setGravity(0.0);
					currEmotion.setFalling(true);
				}
			}
		}

		//egg handling
		for(int k=0; k < eggEnemies.size(); k++){
			tempE = eggEnemies.get(k);
			tempE.update();

			//spawn enemies if we can about every 30 loop runs
			if(eggEnemies.size() < Math.min(eggsRem, ON_SCREEN_EGGS) && spawnTimeElapsed > 30){
				addEgg(createEgg());
				spawnTimeElapsed=0;
			}

			//collision
			for(int i=0; i<bullets.size(); i++){
				tempB = bullets.get(i);
				if(tempE.collidesWithBullet(tempB)){
					if(tempE.isType(tempB.getType()) || tempE.isType("basic")){
						removeBullet(tempB);
						if(tempE.getHealth() > 0)
							tempE.decreaseHealth();
						else{
							keyChance = random.nextInt(eggsRem) + 1;
							if(keyChance == 1 && !droppedKey){
								key = new Key(this,tempE.getX(), tempE.getY());
								droppedKey = true;
							}
							removeEgg(tempE);
							eggsRem--;
						}
					}	
				}
			}
		}

		if(eggEnemies.size()==0 && eggsRem>0)
			addEgg(createEgg());
		
		
		if(LEVEL <= 6){ // door is unopened and there are remaining levels

			if(LEVEL == 0){	// introductory level

			}

			if (LEVEL == 1){
				
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

		/* Level-specific objects*/

		if (LEVEL > 0){
			prisoner.draw(imageContext);
			jail.draw(imageContext);
		}

		if (LEVEL == 0){ // introduction level
			imageContext.drawString("Enemies Left: "+String.valueOf(eggsRem), 375, 20);
		}

		if (LEVEL == 1){

		}


		/* Common objects*/
		
		for(int i=0; i < bullets.size(); i++){	//bullet handling
			tempB = bullets.get(i);
			tempB.draw(imageContext);
		}

		for(int j=0; j<eggEnemies.size(); j++){
			tempE = eggEnemies.get(j);
			tempE.draw(imageContext);
		}

		imageContext.drawString("Level: "+String.valueOf(LEVEL), 15, 20);
		
		if(!open){ // checks if door is opened - eg level 0 killed all 3 eggs
			door = closedDoor;
		}
		else{
			door = openDoor;
		}


		for(int i=0; i<5; i++)
			platforms[i].draw(imageContext);

		portal.draw(imageContext);
		door.draw(imageContext);

		if(currEmotion != null){
			currEmotion.draw(imageContext);
		}

		if(droppedKey && !pickedUpKey){
			key.draw(imageContext);
		}

		if(pickedUpKey && currEmotion.HasKey()){
			key.draw(imageContext, this.getWidth()/2, 0);
		}
	
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, null);
		

		imageContext.dispose();
		g2.dispose();
	}

	public void gameOverScreen(){
	
	}

	// will uncomment after first game play to ensure no issues with this method
	public void clearLevel(){		// reset any level variables 
		// background.resetBackground();
		// currEmotion.resetEmotion();
		// eggsRem = 0;
		// open = false;
	}
}