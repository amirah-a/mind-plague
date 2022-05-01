import javax.sound.sampled.AudioInputStream;		// for playing sound clips
import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;				// for storing sound clips

public class SoundManager {				// a Singleton class
	HashMap<String, Clip> clips;
	private static SoundManager instance = null;	// keeps track of Singleton instance


	private SoundManager () {
		clips = new HashMap<String, Clip>();
		Clip clip;

		clip = loadClip("sounds/background.wav");
		clips.put("background", clip);
		
		clip = loadClip("sounds/collect-key.wav");
        clips.put("key", clip);

		clip = loadClip("sounds/eagle-attack.wav");
		clips.put("eagle-attack", clip);

		clip = loadClip("sounds/enemy-shoot.wav");
		clips.put("enemy-attack", clip);

		clip = loadClip("sounds/game-successful.wav");
		clips.put("win", clip);

		clip = loadClip("sounds/level-complete.wav");
		clips.put("success", clip);

		clip = loadClip("sounds/player_dead.wav");
        clips.put("hit", clip);

		clip = loadClip("sounds/player-shoot.wav");
		clips.put("shoot", clip);

		
        clip = loadClip("sounds/power-up.wav");
        clips.put("power-up", clip);

		clip = loadClip("sounds/player_dead.wav");
        clips.put("player_dead", clip);

		clip = loadClip("sounds/game-failed.wav");
		clips.put("lose", clip);
	}


	public static SoundManager getInstance() {	// class method to retrieve instance of Singleton
		if (instance == null)
			instance = new SoundManager();
		
		return instance;
	}		


    public Clip loadClip (String fileName) {	// gets clip from the specified file
 		AudioInputStream audioIn;
		Clip clip = null;

		try {
    			File file = new File(fileName);
    			audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL()); 
    			clip = AudioSystem.getClip();
    			clip.open(audioIn);
		}
		catch (Exception e) {
 			System.out.println ("Error opening sound files: " + e);
		}
    		return clip;
    }


	public Clip getClip (String title) {

		return clips.get(title);
	}


    public void playClip(String title, Boolean looping) {
            Clip clip = getClip(title);
            if (clip != null) {
                clip.setFramePosition(0);
                if (looping)
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                else
                    clip.start();
            }
    }


    public void stopClip(String title) {
        Clip clip = getClip(title);
        if (clip != null) {
            clip.stop();
        }
    }

}