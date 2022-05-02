// need this for Layout Managers
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

// need this to respond to GUI events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// need this for GUI objects
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
	
public class GameWindow extends JFrame implements
						ActionListener, KeyListener, MouseListener{

	// declare buttons

	private JButton startB;
	private JButton pauseB;
	private JButton endB;
	private JButton restartB;
	private JButton exitB;

	private Container c;

	private JPanel mainPanel;
	private GamePanel gamePanel;
	SoundManager soundManager;

	public GameWindow() {
 
		setTitle ("Mind Plague");
		setSize (500, 580);

	    startB = new JButton ("Start Game");
	    pauseB = new JButton ("Pause Game");
	    endB = new JButton ("End Game");
		restartB = new JButton ("Restart Game");
		exitB = new JButton ("Exit");


		// add listener to each button (same as the current object)
		startB.addActionListener(this);
		pauseB.addActionListener(this);
		endB.addActionListener(this);
		restartB.addActionListener(this);
		exitB.addActionListener(this);
		
		// create mainPanel
		mainPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 0, 0);
		flowLayout.setVgap(2);
		mainPanel.setLayout(flowLayout);

		// create the gamePanel for game entities
		
		gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(500, 500));
		gamePanel.setBorder(null);
		gamePanel.setBackground(new Color(88,88,90));
		
		// create buttonPanel
		GridLayout gridLayout;
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
			soundManager.playClip("background", true);
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
			soundManager.playClip("shoot", false);
	
        }

		//switching characters
		if(keyCode == KeyEvent.VK_1 || keyCode == KeyEvent.VK_NUMPAD1){
			gamePanel.switchEmotion(0); // set to Fear
		}

		if(keyCode == KeyEvent.VK_2 || keyCode == KeyEvent.VK_NUMPAD2){
			if (GamePanel.LEVEL >= 2)
				gamePanel.switchEmotion(1); //set to Love
		}

		if(keyCode == KeyEvent.VK_3 || keyCode == KeyEvent.VK_NUMPAD3){
			if (GamePanel.LEVEL >= 3)
				gamePanel.switchEmotion(2); //set to Rage
		}

		if(keyCode == KeyEvent.VK_4 || keyCode == KeyEvent.VK_NUMPAD4){
			if (GamePanel.LEVEL >= 4)
				gamePanel.switchEmotion(3); //set to Sadness
		}

		if(keyCode == KeyEvent.VK_5 || keyCode == KeyEvent.VK_NUMPAD5){
			if (GamePanel.LEVEL >= 5)
				gamePanel.switchEmotion(4); //set to Happy
		}
	}

	public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if(keyCode == KeyEvent.VK_LEFT){
            gamePanel.getCurrEmotion().setDx(0); //movement
			gamePanel.getCurrEmotion().setAnimation(-3);
        }

        if(keyCode == KeyEvent.VK_RIGHT){
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