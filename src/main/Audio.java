package main;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio {

    protected static File buttonClick; // done
    protected static File ballOut; // done
    protected static File gameStart; // done
    protected static File smash; // done
    protected static File smashBrick; // done
    protected static File winMusic; // done
    protected static File loseMusic; // done
    protected static File growBar; // done
    protected static File decreaseBar; // done
    protected static File newBall; // done

    public static void loadFiles() {
        buttonClick = new File("src/resources/audio/ButtonClick.wav");
        ballOut = new File("src/resources/audio/BallOut.wav");
        gameStart = new File("src/resources/audio/GameStart.wav");
        smash = new File("src/resources/audio/Smash.wav");
        smashBrick = new File("src/resources/audio/SmashBrick.wav");
        winMusic = new File("src/resources/audio/WinGame.wav");
        loseMusic = new File("src/resources/audio/LoseGame.wav");
        growBar = new File("src/resources/audio/GrowBar.wav");
        decreaseBar = new File("src/resources/audio/DecreaseBar.wav");
        newBall = new File("src/resources/audio/NewBall.wav");

    }

    public static void play(File file) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception ignored) {

        }
    }

}
