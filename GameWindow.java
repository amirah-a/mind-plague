import javax.swing.*;			// need this for GUI objects
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.awt.*;			// need this for Layout Managers
import java.awt.event.*;		// need this to respond to GUI events
	
public class GameWindow extends JFrame implements
						ActionListener,
					   KeyListener,
					   MouseListener
{

	// declare instance variables for user interface objects

	// declare labels 

	private JLabel statusBarL;
	private JLabel keyL;
	private JLabel mouseL;

	// declare buttons

	private JButton startB;
	private JButton pauseB;
	private JButton endB;
	private JButton restartB;
	// private JButton focusB;
	private JButton exitB;

	private Container c;

	private JPanel mainPanel;
	private GamePanel gamePanel;
	SoundManager soundManager;

	public GameWindow() {
 
		setTitle ("Mind Plague");
		setSize (500, 580);

		// create labels

		statusBarL = new JLabel ("Application Status: ");
		keyL = new JLabel("Key Pressed: ");
		mouseL = new JLabel("Location of Mouse Click: ");

	    startB = new JButton ("Start Game");
	    pauseB = new JButton ("Pause Game");
	    endB = new JButton ("End Game");
		restartB = new JButton ("Restart Game");
	    //     focusB = new JButton ("Focus on Key");
		exitB = new JButton ("Exit");


		// add listener to each button (same as the current object)

		startB.addActionListener(this);
		pauseB.addActionListener(this);
		endB.addActionListener(this);
		restartB.addActionListener(this);
		// focusB.addActionListener(this);
		// exitB.addActionListener(this);
		
		// create mainPanel

		mainPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 0, 0);
		flowLayout.setVgap(2);
		mainPanel.setLayout(flowLayout);

		GridLayout gridLayout;

		// create the gamePanel for game entities

		gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(500, 500));
		gamePanel.setBorder(null);
		gamePanel.setBackground(new Color(88,88,90));
		// create buttonPanel

		JPanel buttonPanel = new JPanel();
		gridLayout = new GridLayout(1, 4);
		gridLayout.setHgap(8);
		gridLayout.setVgap(4);
		buttonPanel.setLayout(gridLayout);

		// add buttons to buttonPanel

		buttonPanel.add (startB);
		buttonPanel.add (pauseB);
		buttonPanel.add (endB);
		buttonPanel.add (restartB);
		buttonPanel.setBackground(new Color(88,88,90));

		// add sub-panels with GUI objects to mainPanel and set its colour
		mainPanel.add(gamePanel);
		mainPanel.add(buttonPanel);
		mainPanel.setBackground(new Color(88,88,90));

		// set up mainPanel to respond to keyboard and mouse

		gamePanel.addMouseListener(this);
		mainPanel.addKeyListener(this);

		// add mainPanel to window surface

		c = getContentPane();
		c.add(mainPanel);

		// set properties of window

		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		soundManager = SoundManager.getInstance();
	}


	// implement single method in ActionListener interface

	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		

		if (command.equals(startB.getText())) {
			gamePanel.startGame();
		}

		if (command.equals(pauseB.getText())) {
			gamePanel.pauseGame();
		}

		if (command.equals(endB.getText())) {
			gamePanel.endGame();
            System.exit(0);
		}

		if (command.equals(restartB.getText()))
			gamePanel.restartGame();

		mainPanel.requestFocus();
	}


	// implement methods in KeyListener interface

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_UP) {
			if(!gamePanel.getCurrEmotion().isJumping()){
				gamePanel.getCurrEmotion().setJumping(true);
				gamePanel.getCurrEmotion().setGravity(10.0);
			}
				
		}

        if(keyCode == KeyEvent.VK_LEFT){
            gamePanel.updateObjects(3);
			gamePanel.getCurrEmotion().setDx(-2);
			gamePanel.getCurrEmotion().setAnimation(3);
        }

        if(keyCode == KeyEvent.VK_RIGHT){
            gamePanel.updateObjects(4);
			gamePanel.getCurrEmotion().setDx(2);
			gamePanel.getCurrEmotion().setAnimation(4);
        }


        if(keyCode == KeyEvent.VK_SPACE){
            int x = Emotion.x + 40;
            int y = Emotion.y + 13;
            gamePanel.addBullet(gamePanel.createBullet(x, y));
	
        }

		//switching characters
		if(keyCode == KeyEvent.VK_1 || keyCode == KeyEvent.VK_NUMPAD1){
			gamePanel.switchEmotion(0); // set to Fear
		}

		if(keyCode == KeyEvent.VK_2 || keyCode == KeyEvent.VK_NUMPAD2){
			if (GamePanel.LEVEL >= 1)
				gamePanel.switchEmotion(1); //set to Love
		}

		if(keyCode == KeyEvent.VK_3 || keyCode == KeyEvent.VK_NUMPAD3){
			if (GamePanel.LEVEL >= 2)
				gamePanel.switchEmotion(2); //set to Rage
		}

		if(keyCode == KeyEvent.VK_4 || keyCode == KeyEvent.VK_NUMPAD4){
			if (GamePanel.LEVEL >= 3)
				gamePanel.switchEmotion(3); //set to Sadness
		}

		if(keyCode == KeyEvent.VK_5 || keyCode == KeyEvent.VK_NUMPAD5){
			if (GamePanel.LEVEL >= 4)
				gamePanel.switchEmotion(4); //set to Happy
		}
	}

	public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if(keyCode == KeyEvent.VK_LEFT){
            //Player.isWalking = false;
			gamePanel.getCurrEmotion().setDx(0); //movement
			gamePanel.getCurrEmotion().setAnimation(-3);
        }

        if(keyCode == KeyEvent.VK_RIGHT){
            //Player.isWalking = false;
			gamePanel.getCurrEmotion().setDx(0);  //no movement
			gamePanel.getCurrEmotion().setAnimation(-4);
        }

		if(keyCode == KeyEvent.VK_SPACE){
            soundManager.stopClip("player_attack");
        }
	}

	public void keyTyped(KeyEvent e) {

	}

	// implement methods in MouseListener interface

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
	}


	public void mouseEntered(MouseEvent e) {
	
	}

	public void mouseExited(MouseEvent e) {
	
	}

	public void mousePressed(MouseEvent e) {
	
	}

	public void mouseReleased(MouseEvent e) {
	
	}

}