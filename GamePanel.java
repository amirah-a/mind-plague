import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
	private LinkedList<Bullet> enemyBullets;
	private Egg tempE;
	private Bullet tempEnemyB;

	private LinkedList<Life> lifePowerups;
	private Life tempLife;

	private LinkedList<Bullet> bullets;
	private Bullet tempB;
	SoundManager soundManager;

	private Background background;
	private BufferedImage image;

	public static int LEVEL=0;	// there are 6 levels - starting at 0

	//Pelican
	private Pelican[] pelican;
	private Pelican currPelican;
	private LinkedList<Bullet> pelicanBullets;
	private Bullet tempPB;

	private Door[] door;
	private boolean open;
	private Platform[] platforms;
	private Image[] health;
	private Portal portal;
	private Key key;
	private boolean droppedKey, pickedUpKey;

	private Random random;
	private int keyChance;
	private int lifeChance;

	private int eggsRem;
	private int ON_SCREEN_EGGS = 5; // we only want 5 eggs on the screen at a time
	
	private Jail jail;
    private Prisoner prisoner;

	private long spawnTimeElapsed;

	private DisintegrateFX pelicanFX;
	private boolean disintegrate;

	private int[] score;


	public GamePanel () {
		emotions = new Emotion[5];
		currEmotion = null;

		eggEnemies = new LinkedList<Egg>();
		tempE = null;
		enemyBullets = new LinkedList<Bullet>();
		tempEnemyB = null;
		pelicanBullets = new LinkedList<Bullet>();
		tempPB = null;

		lifePowerups = new LinkedList<Life>();
		tempLife = null;		

		bullets = new LinkedList<Bullet>();
		tempB = null;

		image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		open = false;  // door is not opened - level not finished

		eggsRem =  Math.min(3+2*LEVEL, 10);
		score = new int[5]; // 5 scores for 5 levels - not level 0
		platforms = new Platform[5];
		key = null;

		jail = null;
		prisoner = null;
		
		random = new Random();
		keyChance = 0;
		lifeChance = 0;
		droppedKey = false;
		pickedUpKey = false;

		door = new Door[2];

		pelican = new Pelican[2];
		currPelican = null;

		spawnTimeElapsed = 0;
		health = new Image[6];

		pelicanFX = new DisintegrateFX(this);
		disintegrate = false;

		soundManager = SoundManager.getInstance();
	}

	public void switchEmotion(int emotionIndex){
		currEmotion = emotions[emotionIndex];
	}

	public Emotion getCurrEmotion(){
		return currEmotion;
	}

	private void createGameEntities() {
		emotions[0] = new Fear(this);
		emotions[0].setUnlocked(true); // default
		emotions[1] = new Love(this);
		emotions[2] = new Rage(this);
		emotions[3] = new Sadness(this);
		emotions[4] = new Happy(this);
		
		background = new Background(this, "images/Scrolling_BG.png", 8);	
		
		door[0] = new Door(this, 2200, 390, 79, 56, "images/door_open.png");	
		door[1] = new Door(this, 2200, 390, 51 , 56, "images/door_closed.png");

		// if pelican isAttacking index=1 else index=0
		pelican[0] = new Pelican(this, 2000, 330, 204, 120, "images/pelican_idle.png");	
		pelican[1] = new Pelican(this, 2000, 330, 204 , 120, "images/pelican_attack.png");
		currPelican = pelican[0];

		// increase the xPos to place platform further away
		platforms[0] = new Platform(this, 600, 300, 93, 54, "images/short_platform.png"); 
		platforms[1] = new Platform(this, 175, 250, 162, 54, "images/platform.png");
		platforms[2] = new Platform(this, 1250, 350, 114, 54, "images/medium_platform.png"); 
		platforms[3] = new Platform(this, 1350, 250, 162, 54, "images/platform.png");
		platforms[4] = new Platform(this, 1900, 250, 166, 54, "images/long_platform.png"); 

		portal = new Portal(this, 1995, 125, 89, 150, "images/portal.png");

	
		jail = new Jail(this, 2100, 385);
		prisoner = new Prisoner(this, 2110, 400, LEVEL);

		addEgg(createEgg()); // start with one enemy

		loadImages();

		for (int i=0; i<5; i++){ // initialze score
			score[i]=0;
		}
			
	}

	public void addBullet(Bullet b){
		bullets.add(b);
	}

	public void addEnemyBullet(Bullet b){
		enemyBullets.add(b);
		soundManager.playClip("enemy-attack", false);
	}

	public void addPelicanBullet(Bullet b){
		pelicanBullets.add(b);
	}

	public Bullet createBullet(int x, int y){
		Bullet bullet;
		int dx;
		if(currEmotion.getDx() == 0){
			if(currEmotion.getprevDx() == 0){
				dx = 20;
			}else{
				dx = currEmotion.getprevDx()*10;
			}	
		}else{
			dx = currEmotion.getDx()*10;
		}

		if(currEmotion instanceof Fear){
			// System.out.println("Fear");
			bullet = new Bullet(this, x, y, "fear", dx);
			return bullet;
		}
			
		if(currEmotion instanceof Love){
			// System.out.println("Love");
			bullet = new Bullet(this, x, y, "love", dx);
			return bullet;
		}
			
		if(currEmotion instanceof Rage){
			// System.out.println("Rage");
			bullet = new Bullet(this, x, y, "rage", dx);
			return bullet;
		}
			
		if(currEmotion instanceof Sadness){
			// System.out.println("Sadness");
			bullet = new Bullet(this, x, y, "sadness", dx);
			return bullet;
		}	
		else{
			// System.out.println("Happy");
			bullet = new Bullet(this, x, y, "happy", dx);
			return bullet;
		}
	}

	public Bullet createEnemyBullet(int x, int y, int dx){
		Bullet bullet;
		bullet = new Bullet(this, x, y, "egg", dx);	
		return bullet;
	}

	public void removeBullet(Bullet b){
		bullets.remove(b);
	}

	public void removeEnemyBullet(Bullet b){
		enemyBullets.remove(b);
	}

	public void removePelicanBullet(Bullet b){
		pelicanBullets.remove(b);
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

		int typeInt = random.nextInt(LEVEL+1);
		switch(typeInt){
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
				dx = -3;
				break;
			case 1:
				dx = 3;
				break;
			default:
				dx = -3;
		}

		e = new Egg(this, type, portal.getX()-(int)spawnTimeElapsed, portal.getY()+10, dx);
		return e;
	}

	public int getEggBulletDx(Egg e){
		if(currEmotion.x >= e.getX())
			return 20;
		return -20;
	}

    public void loadImages(){
		String prefix ="images/healthbar_";

		for (int i=0; i<6; i++){
    	    health[i] = ImageManager.loadImage(prefix + i + ".png");
		}
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
			soundManager.playClip ("background", true);
			clearLevel();
			createGameEntities();
			gameThread = new GameThread (this);
			thread = new Thread (gameThread);			
			thread.start();
			currEmotion.getAnimation().start();

		}
	}


	public void pauseGame() {				// pause the game (don't update game entities)
		gameThread.pauseGame();
		soundManager.stopClip("background");
	}


	public void endGame() {					// end the game thread
		gameThread.endGame();
		//soundManager.stopClip ("background");
	}

	public void updateObjects(int direction){
		if(background != null){
			background.move(direction);
			//currEmotion.move(direction);
			for(int i=0; i<2; i++)
				door[i].move(direction);
			
			
			if (LEVEL > 0 && LEVEL < 5){
				if (jail != null && prisoner != null){
					jail.move(direction);
					prisoner.move(direction);
				}
				
			}
			
			if (LEVEL == 5){
				for(int i=0; i<2; i++){
					pelican[i].move(direction);
				}
			}

			if (background.getBGX()*-1 < 1832 && background.getBGX() != 0){ // check to stop platfrom from going beyond bg
				for(int i=0; i<5; i++)
					platforms[i].move(direction);
				
				for(int k=0; k < eggEnemies.size(); k++){
					tempE = eggEnemies.get(k);
					tempE.move(direction);
				}
				portal.move(direction);
				
				if (key != null)
					key.move(direction);

				for(int j=0; j<lifePowerups.size(); j++){
					tempLife=lifePowerups.get(j);
					tempLife.move(direction);
				}
			}	
					
		}
	}

	public void gameUpdate () {
		spawnTimeElapsed++;

		if(disintegrate)
			pelicanFX.update();
		if(!pelicanFX.isActive())
			disintegrate = false;

		currEmotion.update();
		if(prisoner != null && LEVEL < 5)
			prisoner.update();
		
		if(key!=null){
			if(currEmotion.collidesWithKey(key)){
				soundManager.playClip("key", false);
				currEmotion.setHasKey(true);
				pickedUpKey = true;
			}
		}
		
		if (pickedUpKey && LEVEL == 0){
			open = true;
		}

		if (pickedUpKey && currEmotion.collidesWithJail(jail)){
			if (LEVEL > 0 && LEVEL < 6)
				jail.moveUp();
				emotions[LEVEL].setUnlocked(true);
		}

		if(eggsRem == 0 && emotions[LEVEL].isUnlocked()){
			open = true;
		}

		if (LEVEL == 5 && pelicanBullets != null && currPelican.getIsActive()){
			for (int i=0; i<pelicanBullets.size(); i++){
				tempPB = pelicanBullets.get(i);
				tempPB.moveDown("egg", 8);
			}
		}
		

		//player bullet handling
		for(int i=0; i<bullets.size(); i++){
			tempB = bullets.get(i);
			tempB.move();

			// attack pelican
			if (LEVEL == 5){
				if (currPelican.collidesWithBullet(tempB) && currPelican.getIsActive()){
					removeBullet(tempB);
					
					if (currPelican.getHealth() > 0){
						if (currEmotion.getX() < currPelican.getX()+10) // cannot shoot boss from behind
							currPelican.decreaseHealth();
					}

					if (currPelican.getHealth() <= 0 && eggsRem > 0){
						pelicanFX.setXY(currPelican.getX(), currPelican.getY()-5);
						disintegrate = true;
						currPelican.setXY(1000, 2000); // moves the pelican and all its related objects out of frame
						currPelican.setIsActive(false); 	// stops the pelican and its related objects from being updated

						if (LEVEL > 0 && LEVEL < 6)
							score[LEVEL-1]+=50;
						
					}

					if (eggsRem == 0 && !currPelican.getIsActive()){
						gameThread.setIsRunning(false);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
						}
						gameThread.setState(true);
					}
				}
			}

			if(tempB.passedDistance())
				removeBullet(tempB);
		}

		// if it collides with an egg
		for(int k=0; k < eggEnemies.size(); k++){

			tempE = eggEnemies.get(k);

			if (currEmotion.collidesWithEgg(tempE)){
				soundManager.playClip("hit", false);
				currEmotion.decreaseHealth();

				if (currEmotion.getHealth() == 0){
					gameThread.setIsRunning(false);
					gameThread.setState(false);
				}
			}
		}

		//enemy bullet handling
		for(int a=0; a<enemyBullets.size(); a++){
			tempEnemyB = enemyBullets.get(a);
			tempEnemyB.move();

			if (currEmotion.collidesWithBullet(tempEnemyB)){
				soundManager.playClip("hit", false);
				removeEnemyBullet(tempEnemyB);
				currEmotion.decreaseHealth();
				
				if (currEmotion.getHealth() == 0){
					gameThread.setIsRunning(false);
					gameThread.setState(false);
				}
			}

			if(tempEnemyB.passedDistance())
				removeEnemyBullet(tempEnemyB);
		}

		//platform collisions
		for(int j=0; j<platforms.length; j++){
			if(currEmotion.isOnTopPlatform(platforms[j])){
				
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

			//bullet collision
			for(int i=0; i<bullets.size(); i++){
				tempB = bullets.get(i);
				
				if(tempE.collidesWithBullet(tempB)){
					
					if(tempE.isType(tempB.getType()) || tempE.isType("basic")){
						removeBullet(tempB);
						if(tempE.getHealth() > 0)
							tempE.decreaseHealth();
						else{
							removeEgg(tempE);
							
							if (eggsRem > 0)
								eggsRem--;
								
							int chance = eggsRem;
							if (eggsRem <= 0)
								chance = random.nextInt(ON_SCREEN_EGGS) % (ON_SCREEN_EGGS - 1 + 1) + 1;
							System.out.println(eggsRem);

							keyChance = random.nextInt(chance);
							lifeChance = random.nextInt(3); //20% chance to drop heart
								// System.out.println(lifeChance);
							if(keyChance == 1 && !droppedKey){
								key = new Key(this,tempE.getX(), tempE.getY());
								droppedKey = true;
							}
								if(lifeChance == 0)
									lifePowerups.add(new Life(this, tempE.getX() + 5, tempE.getY()));
								
								if (LEVEL > 0 && LEVEL < 6)
									score[LEVEL-1]+=10;

							}
						}
					}
				}	

			//plaform collision
			for(int j=0; j<platforms.length; j++){
				if(tempE.isOnTopPlatform(platforms[j])){
					//System.out.println("Is on platform");
					if(tempE.isFalling()){
						tempE.setDy(0);
						tempE.setFalling(false);
					}
				}else{
					if(!tempE.isFalling() && !tempE.isJumping()){
						tempE.setGravity(0.0);
						tempE.setFalling(true);
					}
				}
	
				if(tempE.hitBottom(platforms[j])){
					//currEmotion.setDy(0);
					if(tempE.isJumping()){
						tempE.setJumping(false);
						tempE.setGravity(0.0);
						tempE.setFalling(true);
					}
				}
			}
		
			//enemy attack
			if(tempE.attack()){
				addEnemyBullet(createEnemyBullet(tempE.getX() + 40, tempE.getY()+13, getEggBulletDx(tempE)));
			}

		}

		if(eggEnemies.size()==0 && eggsRem>0)
			addEgg(createEgg());

		//life powerup handling
		for(int i=0; i<lifePowerups.size(); i++){
			tempLife = lifePowerups.get(i);
			if(currEmotion.collidesWithLife(tempLife)){
				soundManager.playClip("power-up", false);
				currEmotion.increaseHealth();
				lifePowerups.remove(tempLife);
			}
		}

		// pelican attack
		if (LEVEL == 5){
			if (currPelican.attack() && currPelican.getIsActive()){
				soundManager.playClip("eagle-attack", false);
				currPelican = pelican[1];
				addPelicanBullet(createEnemyBullet(currPelican.getX()+30, currPelican.getOriginalY()+8, -20));
			}
			else{
				currPelican = pelican[0];
			}

			if (currPelican.getIsActive()){				
				for(int x=0; x<pelicanBullets.size(); x++){
					tempPB = pelicanBullets.get(x);
					tempPB.move();

					if (currEmotion.collidesWithBullet(tempPB)){
						soundManager.playClip("hit", false);
						removePelicanBullet(tempPB);
						currEmotion.decreaseHealth();

						if (currEmotion.getHealth() == 0){
							gameThread.setIsRunning(false);
							gameThread.setState(false);
						}
					}

					if(tempPB.passedDistance())
						removePelicanBullet(tempPB);
					}
				}
		}
		
		if (open && currEmotion.collidesWithDoor(door[0])){
			clearLevel(); 
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		 if (LEVEL == 5){

			if (eggsRem  == 0 && !currPelican.getIsActive()){
				gameThread.setIsRunning(false);
				gameThread.setState(true);

				System.out.println("you win");
			}
		}
	}

	public void gameRender () {				// draw the game objects 
		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		
		background.draw(imageContext);

		Font font = new Font ("Roboto", Font.BOLD, 15);
		imageContext.setFont (font);
		imageContext.setColor(Color.black);

		/* Level-specific objects*/

		if (LEVEL > 0 && LEVEL < 5){
			if (jail != null && prisoner != null){
				prisoner.draw(imageContext);
				jail.draw(imageContext);
			}
		}

		if (LEVEL == 5){
			if (currPelican.getHealth() >=0){
				currPelican.draw(imageContext);
				// System.out.println("Pelican x: " + currPelican.getX());
				if (pelicanBullets != null){
					for (int z=0; z<pelicanBullets.size(); z++){
						tempPB = pelicanBullets.get(z);
						tempPB.draw(imageContext);
						// System.out.println("Egg " + z + " x: " + tempPB.getX());
					}
				}	
			}

			if (disintegrate){
				pelicanFX.draw(imageContext);
			}	

		}


		/* Common objects*/
		portal.draw(imageContext);

		int door_index;
		if(!open){ // checks if door is opened - eg level 0 killed all 3 eggs	
			door_index=1;
		}
		else{
			
			door_index=0;
		}
		door[door_index].draw(imageContext);
		
		imageContext.drawString("Level: "+String.valueOf(LEVEL+1), 15, 20);
		imageContext.drawString("Enemies Left: "+String.valueOf(eggsRem), 375, 20);
		
		if (LEVEL > 0 && LEVEL < 6)
			imageContext.drawString("Score: "+String.valueOf(score[LEVEL-1]), 230, 60);

		for(int i=0; i<5; i++)
			platforms[i].draw(imageContext);


		if (bullets != null){		
			for(int i=0; i < bullets.size(); i++){	//bullet handling
				tempB = bullets.get(i);
				tempB.draw(imageContext);
			}
		}

		for(int j=0; j<eggEnemies.size(); j++){
			tempE = eggEnemies.get(j);
			tempE.draw(imageContext);
		}

		if (enemyBullets != null){
			for(int i =0; i<enemyBullets.size(); i++){
				tempEnemyB = enemyBullets.get(i);
				tempEnemyB.draw(imageContext);
			}
		}

		for(int i=0; i<lifePowerups.size(); i++){
			tempLife = lifePowerups.get(i);
			tempLife.draw(imageContext);
		}

		if(currEmotion != null){
			currEmotion.draw(imageContext);
		}

		if(droppedKey && !pickedUpKey){
			key.draw(imageContext);
		}

		if(pickedUpKey && currEmotion.HasKey()){
			key.draw(imageContext, this.getWidth()/2, 0);
		}

		int h;
		if (currEmotion.getHealth() < 0){
			h = 0;
		}
		else
			h = currEmotion.getHealth();
		imageContext.drawImage(health[h], 15, 40, 15, 195, null);

		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, null);
		

		imageContext.dispose();
		g2.dispose();
	}

	public void gameOverScreen(boolean outcome){
		Graphics2D imageContext = (Graphics2D) image.getGraphics();
		Font f = new Font ("ROBOTO", Font.BOLD, 30);

		background.draw(imageContext);
		// background.draw(imageContext, false) ;

		Rectangle2D.Double card = new Rectangle2D.Double(25,215,450, 100);
		imageContext.setColor(Color.BLACK);
		imageContext.fill(card);
		imageContext.draw(card);

		if (outcome){	// player wins
			imageContext.setFont (f);
			imageContext.setColor(Color.GREEN);
			soundManager.playClip("win", false);
		  	imageContext.drawString("CONGRATULATIONS!", 100, 250);
			imageContext.drawString("MIND PLAGUE DESTROYED", 50, 300);
		}
		else{	// player loses
			imageContext.setFont (f);
			imageContext.setColor(Color.RED);
			soundManager.playClip("lose", false);
		  	imageContext.drawString("GAME OVER", 160, 275);
		}

		imageContext.drawImage(currEmotion.getAnimation().getImage(), getWidth()/2, 400, 48, 48, null);
		
		Font font = new Font ("Roboto", Font.BOLD, 15);
		imageContext.setFont (font);
		imageContext.setColor(Color.black);

		int sum=0;
		for (int i=0; i<5; i++){
			sum+=score[i];
		}

		imageContext.drawString("Final score: " +String.valueOf(sum), 230, 60);
		
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, null);
		
		imageContext.dispose();
		g2.dispose();
	}	
	


	public void clearLevel(){		// reset any level variables 
		soundManager.playClip("success", false);
		background.resetBackground();
		currEmotion.resetEmotion();
		
		open = false;
		pickedUpKey = false;
		droppedKey = false;
		key = null;

		if (jail != null){
			jail.reset();
			prisoner.reset();

		}

		portal.reset();


		for(int i=0; i<2; i++){
			door[i].reset();
			pelican[i].reset();
		}

		for(int k=0; k < eggEnemies.size(); k++){
			tempE = eggEnemies.get(k);
			tempE.reset();
		}

		for(int i=0; i<5; i++){
			platforms[i].reset();
		}
		
		if(LEVEL < 6){
			LEVEL++;	
			prisoner.loadAnimations(LEVEL);
			eggsRem = Math.min(3+2*LEVEL, 10);	
		}

		bullets.removeAll(bullets);
		enemyBullets.removeAll(enemyBullets);
		pelicanBullets.removeAll(pelicanBullets);
		lifePowerups.removeAll(lifePowerups);
	}
}