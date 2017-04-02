package utilities;

// change package name to fit your own package structure!

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

// SoundManager for Asteroids

public class SoundManager {

	// this may need modifying
	final static String path = "sounds/";


	public final static Clip bgSound = getClip("tetris");


    public static void main(String[] args) throws Exception {

            play(bgSound);
            Thread.sleep(10000);

    }


	// methods which do not modify any fields

	public static void play(Clip clip) {
		clip.setFramePosition(0);
		clip.start();
	}

	private static Clip getClip(String filename) {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream sample = AudioSystem.getAudioInputStream(new File(path
					+ filename + ".wav"));
			clip.open(sample);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clip;
	}


	public static void start() {
		play(bgSound);
	}


}
